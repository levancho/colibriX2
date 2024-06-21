package com.despani.x2.core.managers.base;

import com.despani.x2.core.exceptions.base.DespSecurityException;
import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
