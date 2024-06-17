package com.despani.core.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public class SessionListener implements HttpSessionListener {


    private static final int ACTIVE_SESSION_TIME_IN_SECONDS = 150000;


    @Autowired
    SessionRegistry sessionRegistry;
    @Autowired
    Authentication authentication;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
       System.out.println("session is created");
        String sessionId = se.getSession().getId();
        se.getSession().setMaxInactiveInterval(ACTIVE_SESSION_TIME_IN_SECONDS);

//        SessionInformation info = sessionRegistry.getSessionInformation(sessionId);

        System.out.println(se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session is destroyed");
        String sessionId = se.getSession().getId();
//        SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
//        sessionRegistry.removeSessionInformation(sessionId);
        //sessionRegistry.removeSessionInformation(sessionId);
       // Set<String> s = sessionRegistry.
        //sessionRegistry.removeSessionInformation(sessionId);




    }
}
