package com.despani.core.managers.base;

import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.events.AuthenticationEventX2;
import com.despani.core.exception.DespSecurityException;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IDespSecurityManager {


    boolean isUserAuthenticated();

    DespaniPrincipal getCurrentUser();

    public Authentication getAuth();

    String getIp();

    public String getSecurityToken();

    public void login(DespaniUser user,HttpServletRequest req, HttpServletResponse res) throws DespSecurityException;
    public void logout(HttpServletRequest req, HttpServletResponse res) throws DespSecurityException;
    public List<DespaniPrincipal> getLoggedInUsers();

    void addSecTokens(HttpServletRequest req, HttpServletResponse res, DespaniPrincipal principal);



//    public void onAuth(ApplicationListener<AuthenticationEventX2> listener);



}
