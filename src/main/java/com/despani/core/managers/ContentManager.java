package com.despani.core.managers;

import com.despani.core.beans.ContentOut;
import com.despani.core.beans.mongo.DespComment;
import com.despani.core.beans.domains.DespContent;
import com.despani.core.beans.mongo.DespComplaint;
import com.despani.core.beans.mongo.DespVote;
import com.despani.core.beans.mongo.DespaniMongoUser;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.events.*;
import com.despani.core.interfaces.IManager;
import com.despani.core.platform.*;
import com.despani.core.platform.interfaces.IContentServices;
import com.despani.core.services.CommentsService;
import com.despani.core.services.NotificationsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ContentManager implements IDespTagGenerator, IManager {

    @Autowired
    public IContentServices contentService;



    @Autowired
    public CommentsService commentsService;



    @Autowired
    public NotificationsService notifService;


    @Autowired
    private ApplicationEventPublisher publisher;





    public ContentManager() {

    }


    public void createContent(DespaniPrincipal dsp ,DespContent dc, Map<String, String> allProps) {

        contentService.createContent(dsp,dc,allProps);
    }


    public void updateContent(DespContent dc, Map<String, String> allProps) {
        contentService.updateContent(dc,allProps);
    }


    public List<DespContent> getAllContentLimited(int limite, int offsetMultiplier) {
        return getAllContentLimited(limite,offsetMultiplier, true);
    }

    public List<DespContent> getAllContentLimited(int limitPerPage, int offsetMultiplier, boolean render) {
        List<DespContent> contetnToShow = contentService.getContentLimited(limitPerPage, offsetMultiplier, false);
//        if(render) contetnToShow.stream().forEach(content -> renderContent(content));
        if(render) contetnToShow.stream().forEach(this::renderContent);
        return contetnToShow;
    }


    public DespContent getContentByOid(Integer oid) throws Exception {
        return getContentByOid(oid,true);
    }


    public DespContent getContentByOid(Integer oid, boolean render) throws Exception {
        DespContent content = contentService.getContentByOid(oid);
        if(render) renderContent(content);
        return content;
    }



    public List<DespContent> getPublishedContentList(int limitPerPage, int offsetMultiplier, boolean render) {
        List<DespContent> contetnToShow = contentService.getContentLimited(limitPerPage, offsetMultiplier, true);
        if(render) contetnToShow.stream().forEach(content -> renderContent(content));
        return contetnToShow;
    }


    public List<DespContent> getPublishedContentList(int limitPerPage, int offsetMultiplier) {
       return getPublishedContentList(limitPerPage,offsetMultiplier,true);
    }


    public List<DespContent> getFeaturedContent(int limitPerPage, int offsetMultiplier) {
        return getFeaturedContent(limitPerPage,offsetMultiplier,true);
    }

    public List<DespContent> getFeaturedContent(int limitPerPage, int offsetMultiplier,boolean render) {
        List<DespContent> contetnToShow = contentService.getfeaturedContent(limitPerPage, offsetMultiplier, true);
        if(render) contetnToShow.stream().forEach(this::renderContent);
        return contetnToShow;
    }

    public int countContent() {
        return contentService.countContent();
    }

    @Autowired
    Configuration freemarkerCOnfig;

    public String processor (final Map variableMap, String templateName ) throws Exception {

        final Template template = freemarkerCOnfig.getTemplate(templateName);
        final Writer writer = new StringWriter();
        template.process(variableMap, writer);
        String ret = writer.toString();
        System.out.println(ret);
        return ret;
    }



    public void publishContent(Integer userOid, Boolean publish) {
        contentService.publishContent(userOid,publish);
    }

    public void featuredContent(Integer userOid, Boolean featured) {
        contentService.featuredContent(userOid,featured);
    }

    public void updateContentAccessLevel(Integer userOid, Integer access) {
        contentService.updateContentAccessLevel(userOid,access);
    }



//    public DespContent getContentByOidACL(Integer oid) throws Exception {
//        return contentService.getContentByOidACL(oid);
//    }

    /** private */

    private void renderContent(DespContent content)  {
        System.out.println("------ START-------------");
        String render1 = render(content.getIntrotext());
        String render2 = render(content.getFulltext());
        content.setOut(new ContentOut(render1,render2));
        System.out.println("------ END -------------");
    }

    private String render(String content) {
        System.out.println("------ {tabs} START-------------");

        // async
        publisher.publishEvent(new DespOnBeforeContentEvent(content, this));
        Document doc = Jsoup.parse(content);


        // sync
        publisher.publishEvent(new DespOnContentEvent(doc,this));

        String  body = doc.body().html();
        if(log.isDebugEnabled()){
            log.debug(body);
        }
//        System.out.println(body);
        // async
        publisher.publishEvent(new DespOnAfterContentEvent(content, this));

        return body;
    }


    public void addCommentToContentAndNotify(DespComment despComment, String fetchMode) {
        commentsService.createComment(despComment);
//         notifService.addComment(despComment);



        if(fetchMode!=null && fetchMode.trim().equalsIgnoreCase("stomp")) {
            notifService.dispatchAddComment(despComment);
        } else  if(fetchMode!=null && fetchMode.trim().equalsIgnoreCase("sse")) {

        // TODO fix this better
            publisher.publishEvent(new DespOnNewCommentEvent(despComment,this));
        } else {
            notifService.dispatchAddComment(despComment);
        }
    }


    public void editCommentToContentAndNotify(DespComment despComment, String fetchMode) {
        despComment = commentsService.editComment(despComment);
//         notifService.addComment(despComment);



        if(fetchMode!=null && fetchMode.trim().equalsIgnoreCase("stomp")) {
            notifService.dispatchUpdateComment(despComment);
        } else  if(fetchMode!=null && fetchMode.trim().equalsIgnoreCase("sse")) {

            // TODO fix this better
            publisher.publishEvent(new DespOnEditCommentEvent(despComment,this));
        } else {
            notifService.dispatchUpdateComment(despComment);
        }
    }

    public List<DespComment> loadCommentsforContent(Integer contentoid) {
        Optional<List<DespComment>> allCommentsForContent = commentsService.getAllCommentsForContent(contentoid);
        if(allCommentsForContent.isPresent()){
            return allCommentsForContent.get();
        }else {
          return   new ArrayList<>();
        }


    }

    public void createComment(DespComment despComment) {
        commentsService.createComment(despComment);
    }

    public void upVoteComment(String commentoid, DespaniMongoUser despaniMongoUser) {
        _VoteComment(commentoid,1,despaniMongoUser);
    }


    public void downVoteComment(String commentoid, DespaniMongoUser despaniMongoUser) {
        _VoteComment(commentoid, -1, despaniMongoUser);
    }

    private void _VoteComment(String commentoid, int value, DespaniMongoUser despaniMongoUser) {

        Optional<DespComment> commentById = commentsService.getCommentById(commentoid);
        if(commentById.isPresent()) {
            DespComment despComment = commentById.get();
            List<DespVote> votes = despComment.getVotes().stream().
                    filter(a -> a.getUser() != null && a.getUser().get_id().equalsIgnoreCase(despaniMongoUser.get_id())).collect(Collectors.toList());

            if (votes != null && votes.size() > 0) {
                votes.stream().forEach(e-> {
                   if(e.getValue()==value) {
                       throw new RuntimeException("you already voted down for this comment");
                   }else {
                       e.setValue(value);
                   }
               });

//                votes.stream().forEach(a -> a.setValue(value));
            } else {
                DespVote v = new DespVote();
                v.setValue(value);
                v.setUser(despaniMongoUser);
                despComment.getVotes().add(v);
            }

            List<DespVote> v= despComment.getVotes();
            int totalvotes = v.size();
            despComment.setTotalvotes(totalvotes);
            List<Integer> collect = v.stream().map(a -> a.getValue()).collect(Collectors.toList());
            Integer reduce =collect.stream().reduce(0, (a, b) -> a + b);
            despComment.setTotalvotessum(reduce);
            commentsService.saveComment(despComment);

            notifService.dispatchUpdateComment(despComment);

        } else {
                throw new RuntimeException("something is wrong comment with id ["+commentoid+"] does not exist ");
        }

    }


    public void reportComment(String commentoid, DespaniMongoUser despaniMongoUser) {

        Optional<DespComment> commentById = commentsService.getCommentById(commentoid);
        if(commentById.isPresent()) {
            DespComment despComment = commentById.get();
            List<DespComplaint> reports = despComment.getReports().stream().
            filter(a -> a.getUser() != null && a.getUser().get_id().equalsIgnoreCase(despaniMongoUser.get_id())).collect(Collectors.toList());

            if (reports != null && reports.size() > 0) {
                throw new RuntimeException("you already reported   for this comment");
            } else {
                DespComplaint c = new DespComplaint();
                c.setUser(despaniMongoUser);
                despComment.getReports().add(c);
                commentsService.saveComment(despComment);
            }


        } else {
            throw new RuntimeException("something is wrong comment with id ["+commentoid+"] does not exist ");
        }

    }

    public void deleteComment(String commentoid) {
        commentsService.deleteCOmment(commentoid);
    }
}
