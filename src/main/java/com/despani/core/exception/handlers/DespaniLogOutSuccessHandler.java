package com.despani.core.exception.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DespaniLogOutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
//    @Value("${security.oauth2.logout.success.url}")
    @Value("test")
    private String logoutAuthUrl;

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(logoutAuthUrl);
    }
}
