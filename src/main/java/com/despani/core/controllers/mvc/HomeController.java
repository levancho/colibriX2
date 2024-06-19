package com.despani.core.controllers.mvc;

import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.example.Greeting;
import com.despani.core.beans.example.HelloMessage;
import com.despani.core.exception.DespSecurityException;
import com.despani.core.exception.DespUserAlreadyExistsException;
import com.despani.core.exception.DespUserInvalidPasswordException;
import com.despani.core.exception.DespUserNotFoundException;
import com.despani.core.managers.base.IDespSecurityManager;
import com.despani.core.utils.DespGlobals;
import com.despani.core.utils.OXUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class HomeController extends ABaseController    {




    @Autowired
    public IDespSecurityManager secMan;
//    @Autowired
//    private CamelContext camelContext;
//
//
//    @RequestMapping(value = "/send")
//    public String sendMessage( ) {
//
//        MessageDTO m = new MessageDTO();
//        m.setContent("this is content");
//        m.setDate(new Date());
//        m.setFrom("bondo");
//        m.setTo("jondoia");
//        // send any message sent by clients to a queue called rt_messages
//
//        camelContext.createProducerTemplate().sendBody("activemq:rt_messages", m);
//        return siteRedirect("");
//    }


    @RequestMapping("/")
    public String home2(HttpServletRequest req, HttpServletResponse res,Model model){

//        return "home";
        return siteRedirect("");
    }

    @RequestMapping("/xout")
    public String xout(HttpServletRequest req, HttpServletResponse res,Model model) throws DespSecurityException {
        secMan.logout(req,res);
//        return "home";
        return "logout";
    }

    private final SimpMessagingTemplate simpMessagingTemplate;

    public HomeController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/str")
    public void greet(String greeting) {
        log.info("Greeting for {}", greeting);

        String text = "[" + Instant.now() + "]: " + greeting;
        this.simpMessagingTemplate.convertAndSend("/topic/greetings", text);
    }


    @MessageMapping("/obj")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }


    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String  getAuth(HttpServletRequest request, Model model){
        request.setAttribute("cx",request.getContextPath());
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.defaults());
        errorAttributes.forEach((attribute, value) -> {
            request.setAttribute(attribute,value);
        });
        return generic(model);
    }


    @RequestMapping("/offline")
    public String  offlineHandler(Model model){

        model.addAttribute("offlineMessage",DespGlobals.getProperty("site.1.offline-text"));
        return generic(model, "offline");
    }

//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }


    @GetMapping("/chat")
    public String chat(HttpServletRequest httpServletRequest, @AuthenticationPrincipal Authentication authentication, Model model){
        boolean isAuthed =(authentication!=null && authentication.isAuthenticated());
        model.addAttribute("authed",isAuthed);
       String credoUrl = OXUtils.getURL(httpServletRequest)+"/auto/creds/";


        model.addAttribute("credoUrl",credoUrl);
        model.addAttribute("openfireUrl", despaniConfigProperties.getOpenfire().getUrl());
        return  "chat";
    }


    @GetMapping("/principal")
    public @ResponseBody String  getAuth(@AuthenticationPrincipal Authentication authentication){
        Principal principal = (Principal) authentication.getPrincipal();
        String userName = principal.getName();
        return "OK";
    }


    @RequestMapping("/login")
    public String login(HttpServletRequest req, Model model){

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
//        return "home";
        return site(req,model,"login");
    }














    @RequestMapping("/signup")
    public String createUser(@ModelAttribute DespaniUser despaniUser, Model model){

        try {
            userService.createUser(new DespaniUser());
        } catch (DespUserNotFoundException e) {
            e.printStackTrace();


        } catch (DespUserInvalidPasswordException despUserInvalidPassword) {
            despUserInvalidPassword.printStackTrace();
        } catch (DespUserAlreadyExistsException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/securedPage")
    public String getSecPage(){
        return "securedPage";
    }

    @RequestMapping("/login/oauth2")
    public String loginPage(){
        String despaniAuthServer = "/oauth2/authorization/despani";
        //return "oauth2";
        return "redirect:"+despaniAuthServer;
    }


    @RequestMapping("/a")
    public String home(Model model){

//        model.addAttribute("na","Shalikia");

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
        return "home.ftl";
    }

    @RequestMapping("/b")
    public String b(Model model){

//        model.addAttribute("na","Shalikia");

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
        return "index-f";
    }


    @RequestMapping("/c")
    public String c(Model model){

//        model.addAttribute("na","Shalikia");

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
        return "indexf.ftl";
    }

    @RequestMapping("/e.htm")
    public String e(Model model){

//        model.addAttribute("na","Shalikia");

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
        return "home.htm";
    }



//



}
