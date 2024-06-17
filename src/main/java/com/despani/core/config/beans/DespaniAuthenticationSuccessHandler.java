package com.despani.core.config.beans;

import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.managers.base.IDespSecurityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class DespaniAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    IDespSecurityManager secMan;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        log.info("authenticated");


        // TODO  SEecurity make cookies hhtps
        if(authentication.getPrincipal() instanceof  DespaniPrincipal) {
            secMan.addSecTokens(req, res, (DespaniPrincipal) authentication.getPrincipal());
        }else {
            log.error("something went wrong its not DespaniPrincipal");
            throw new RuntimeException("something went wrong its not DespaniPrincipal");
        }
        super.onAuthenticationSuccess(req,res,authentication);

    }
}
