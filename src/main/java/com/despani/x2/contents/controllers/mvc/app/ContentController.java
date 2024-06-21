package com.despani.x2.contents.controllers.mvc.app;

import com.despani.x2.contents.beans.domains.DespContent;
import com.despani.x2.contents.beans.form.CommentForm;
import com.despani.x2.contents.beans.mongo.DespComment;
import com.despani.x2.contents.managers.ContentManager;
import com.despani.x2.contents.mapstruct.IContentMapper;
import com.despani.x2.core.annotations.Crumb;
import com.despani.x2.core.beans.DespResponse;
import com.despani.x2.core.beans.Pagination;
import com.despani.x2.core.xusers.beans.mongo.DespaniMongoUser;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.controllers.mvc.ABaseController;
import com.despani.x2.core.events.DespOnEditCommentEvent;
import com.despani.x2.core.events.DespOnNewCommentEvent;
import com.despani.x2.core.exceptions.base.DespSecurityException;
import com.despani.x2.core.managers.ApplicationManager;
import com.despani.x2.core.managers.base.IDespSecurityManager;
import com.despani.x2.core.mapstruct.IDespaniMapper;
import com.despani.x2.core.utils.DespGlobals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/app/content")
public class ContentController extends ABaseController {


    @Autowired
    public ContentManager contentManager;

    @Autowired
    public ApplicationManager appManager;

    @Autowired
    public IDespSecurityManager secMan;



    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @Crumb(label="Admin-Content",xdesc = "Admin Content", family="admin", parent = "Admin-main" )
    @RequestMapping({"/",""})
    public String adminHome(HttpServletRequest req, Model model, @RequestParam(value = "page",required=false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "limit",required=false, defaultValue = "10") Integer limitp){

        DespContent dummy = new DespContent();
        Pagination p = new Pagination(dummy,contentManager.countContent());
        p.calculate(pageNumber,limitp);
        model.addAttribute("p",p);
        List<DespContent> contetnToShow =  contentManager.getPublishedContentList(p.getLimite(),p.getOffsetMultiplier());

        model.addAttribute("allcontent",contetnToShow);
        // TODO FIX THIS
        return site(req, model, dummy.getObjectName()+"/list");
    }

    @RequestMapping({"/oid/{oid}"})
    public String editContent(HttpServletRequest req, Model model,@PathVariable("oid")Integer oid) throws Exception {
        DespContent content = contentManager.getContentByOid(oid, true);

        model.addAttribute("postMode", DespGlobals.getProperty("site.3.comment-post-mode"));
        model.addAttribute("content",content);
        return site(req,model, content.getObjectName()+"/view");
    }

    /** MESSAGES **/

    @MessageMapping("/message/comment/add/")
    @SendTo("/topic/comments")
    public DespComment addCommentViaMessage( CommentForm commentForm) {

        DespaniPrincipal currentUser = secMan.getCurrentUser();
        DespComment despComment = IContentMapper.MAPPER.toDespaniComment(commentForm);
        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(currentUser.getUser());
        despComment.setUser(despaniMongoUser);

        contentManager.createComment(despComment);


        return despComment;
    }


    @MessageMapping("/message/comment/add/camel")
    public void addCommentViaMessage2(CommentForm commentForm) throws DespSecurityException {

        if(!secMan.isUserAuthenticated()){
            throw new DespSecurityException();
        }
        DespaniPrincipal currentUser = secMan.getCurrentUser();
        DespComment despComment = IContentMapper.MAPPER.toDespaniComment(commentForm);
        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(currentUser.getUser());
        despComment.setUser(despaniMongoUser);

        contentManager.addCommentToContentAndNotify(despComment,commentForm.getMode());
    }

    @MessageMapping("/message/comment/edit/camel")
    public void editCommentViaMessage2(CommentForm commentForm) throws DespSecurityException {

        if(!secMan.isUserAuthenticated()){
            throw new DespSecurityException();
        }
        DespaniPrincipal currentUser = secMan.getCurrentUser();
        DespComment despComment = IContentMapper.MAPPER.toDespaniComment(commentForm);
        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(currentUser.getUser());
        despComment.setUser(despaniMongoUser);

        contentManager.editCommentToContentAndNotify(despComment,commentForm.getMode());
    }

    @GetMapping("/sse/comment")
    public ResponseEntity<SseEmitter> doSSEComment(HttpServletResponse response) throws InterruptedException, IOException {
        response.setHeader("Cache-Control", "no-store");

        SseEmitter emitter = new SseEmitter();
        // SseEmitter emitter = new SseEmitter(180_000L);

        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @EventListener
    public void onNeComment(DespOnNewCommentEvent commentEvent) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(commentEvent.getSource());

                // close connnection, browser automatically reconnects
                // emitter.complete();

                // SseEventBuilder builder = SseEmitter.event().name("second").data("1");
                // SseEventBuilder builder =
                // SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
                // emitter.send(builder);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }

    @EventListener
    public void onEditComment(DespOnEditCommentEvent commentEvent) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(commentEvent.getSource());

                // close connnection, browser automatically reconnects
                // emitter.complete();

                // SseEventBuilder builder = SseEmitter.event().name("second").data("1");
                // SseEventBuilder builder =
                // SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
                // emitter.send(builder);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }


    /** REST **/

    @RequestMapping(value="/rest/comment/add",method = RequestMethod.POST,produces={"application/json"})
    public @ResponseBody
    ResponseEntity<DespResponse> addComment(@AuthenticationPrincipal DespaniPrincipal user, @RequestBody CommentForm commentForm){

        DespComment despComment = IContentMapper.MAPPER.toDespaniComment(commentForm);
        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(user.getUser());
        despComment.setUser(despaniMongoUser);

        contentManager.addCommentToContentAndNotify(despComment, commentForm.getMode());

        DespResponse resp = new DespResponse(despComment);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;
    }


    @RequestMapping(value="/rest/comment/edit",method = RequestMethod.POST,produces={"application/json"})
    public @ResponseBody
    ResponseEntity<DespResponse> editComment(@AuthenticationPrincipal DespaniPrincipal user, @RequestBody CommentForm commentForm){

        DespComment despComment = IContentMapper.MAPPER.toDespaniComment(commentForm);
        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(user.getUser());
        despComment.setUser(despaniMongoUser);

        contentManager.editCommentToContentAndNotify(despComment, commentForm.getMode());

        DespResponse resp = new DespResponse(despComment);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;
    }



    @RequestMapping("/rest/comments/load/contentoid/{contentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> loadCommentsForContent(@PathVariable("contentoid") Integer contentoid ){

        DespResponse resp = new DespResponse(contentManager.loadCommentsforContent(contentoid));
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
       return  r;
    }




    @RequestMapping("/rest/comment/up/contentoid/{contentoid}/commentoid/{commentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> commentDoUp(@AuthenticationPrincipal DespaniPrincipal user, @PathVariable("contentoid") Integer contentoid, @PathVariable("commentoid") String commentoid ){

        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(user.getUser());
        contentManager.upVoteComment(commentoid,despaniMongoUser);


        DespResponse resp = new DespResponse(contentoid);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;

    }


    @RequestMapping("/rest/comment/down/contentoid/{contentoid}/commentoid/{commentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> commentDoDown(@AuthenticationPrincipal DespaniPrincipal user, @PathVariable("contentoid") Integer contentoid, @PathVariable("commentoid") String commentoid ){

        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(user.getUser());
        contentManager.downVoteComment(commentoid,despaniMongoUser);


        DespResponse resp = new DespResponse(contentoid);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;


    }


    @RequestMapping("/rest/comment/edit/contentoid/{contentoid}/commentoid/{commentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> commentDoEdit(@AuthenticationPrincipal DespaniPrincipal user, @PathVariable("contentoid") Integer contentoid, @PathVariable("commentoid") String commentoid ){

        DespResponse resp = new DespResponse(contentoid);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;

    }


    @RequestMapping("/rest/comment/report/contentoid/{contentoid}/commentoid/{commentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> commentDoReport(@AuthenticationPrincipal DespaniPrincipal user, @PathVariable("contentoid") Integer contentoid, @PathVariable("commentoid") String commentoid ){

        DespaniMongoUser despaniMongoUser = IContentMapper.MAPPER.toMongoUser(user.getUser());
        contentManager.reportComment(commentoid,despaniMongoUser);

        DespResponse resp = new DespResponse(contentoid);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;

    }

    @RequestMapping("/rest/comment/delete/contentoid/{contentoid}/commentoid/{commentoid}")
    public @ResponseBody
    HttpEntity<DespResponse> commentDoDelete(@AuthenticationPrincipal DespaniPrincipal user, @PathVariable("contentoid") Integer contentoid, @PathVariable("commentoid") String commentoid ){


        contentManager.deleteComment(commentoid);

        DespResponse resp = new DespResponse(contentoid);
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r =  new ResponseEntity<>(resp, HttpStatus.OK);
        return r;

    }

}
