package com.despani.x2.core.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DespaniAccessDeniedHandler implements AccessDeniedHandler {

    Logger logger = LoggerFactory.getLogger(DespaniAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(accessDeniedException != null){
            logger.warn("User: "+ authentication.getName()+" tried to access protected url: "+request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath()+"/accessDenied");
    }
}
