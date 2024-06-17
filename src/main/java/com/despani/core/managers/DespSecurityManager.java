package com.despani.core.managers;

import com.despani.core.beans.base.ADespaniObject;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.config.beans.DespaniConfigProperties;
import com.despani.core.exception.DespSecurityException;
import com.despani.core.managers.base.IDespSecurityManager;
import com.despani.core.utils.CookieUtils;
import com.despani.core.utils.DespGlobals;
import com.despani.core.utils.Encryptor;
import com.despani.core.utils.IRoles;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(
        value="despani.app.ssoconf.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class DespSecurityManager extends SimpleUrlLogoutSuccessHandler implements IDespSecurityManager, ApplicationListener<AuthenticationSuccessEvent>{


    @EventListener
    public void doSomething(InteractiveAuthenticationSuccessEvent event) { // any spring event
      log.info(event.toString());
    }


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        log.info(event.toString());
    }


    @Autowired
    DespaniConfigProperties despaniConfigProperties;

    @SneakyThrows
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        super.handle(request, response, authentication);
//        "http://54.146.190.96/auth/exit"
//        http://localhost:8092/auth/exit
        if(authentication.isAuthenticated()) {
            this.logout(request, response);
        }

         response.sendRedirect(despaniConfigProperties.getSsoconf().getLogoutAuthUrl());


    }



    @Override
    public DespaniPrincipal getCurrentUser() {
        Authentication a = getAuth();


        if(a==null) {
            return null;
        }

        if(a.getPrincipal() instanceof String){
            return null;
        }

        return(DespaniPrincipal)a.getPrincipal();
    }

    public boolean isOwner(ADespaniObject obj){
        DespaniPrincipal currentUser = getCurrentUser();
        return (obj.getCreatedByOid()==currentUser.getUser().getOid());
    }

    public  boolean hasRoleWeight(int contentWeight) {
        DespaniPrincipal du = getCurrentUser();
        if(du==null) return (contentWeight==0);

        int retvalue = 0;
        Collection<? extends GrantedAuthority> authorities = du.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case IRoles.ROLE_USER:
                    retvalue=retvalue<1?1:retvalue;
                    break;

                case IRoles.ROLE_EDITOR:
                    retvalue=retvalue<2?2:retvalue;
                    break;

                case IRoles.ROLE_MANAGER:
                    retvalue=retvalue<3?3:retvalue;
                    break;

                case IRoles.ROLE_ADMIN:
                    retvalue=retvalue<4?4:retvalue;
                    break;

                case IRoles.ROLE_SUPER_ADMIN:
                    retvalue=retvalue<5?5:retvalue;
                    break;

                default:
                    break;
            }

            if(retvalue>=contentWeight)return true;

        }
        return false;

    }

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getIp() {
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        return remoteAddress;

    }

    public void logout(HttpServletRequest req, HttpServletResponse res) {
        removeSecTokens(req,res);
        Authentication auth = getAuth();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(req, res, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

    }

    @Override
    public boolean isUserAuthenticated() {
        Authentication a = getAuth();
        return a!=null && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void  addSecTokens(HttpServletRequest req, HttpServletResponse res, DespaniPrincipal principal){

        Encryptor encryptor = DespGlobals.getEncryptor("", "Despani-wfish-CBC-Despani");
        String token = principal.getAccessToken();
        logger.info("token = "+token);
        String enced = encryptor.encrypt(token );
        logger.info("enced = "+enced);

        String  username = principal.getName();

        String encusername = encryptor.encrypt(username);
        logger.info("encusername = "+encusername);
        CookieUtils.setCookie(req,res,"despUsername", encusername,"/");
//        CookieUtils.setCookie(req,res,"despUsername", principal.getName());
        CookieUtils.setCookie(req,res,"despHash",enced,"/");
    }


    // TODO externalize props
    public void  removeSecTokens(HttpServletRequest req, HttpServletResponse res){
        CookieUtils.deleteCookieByName(req,res,"despUsername","/");
        CookieUtils.deleteCookieByName(req,res,"despHash","/");
    }




    @Override
    public List<DespaniPrincipal> getLoggedInUsers() {
        return null;
    }



    public DespaniPrincipal getDespPrincipal(){
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)getAuth();
        return (DespaniPrincipal)oAuth2AuthenticationToken.getPrincipal();
    }

    public String getSecurityToken(){
        return getDespPrincipal().getAccessToken();
    }

    @Override
    public void login(DespaniUser user, HttpServletRequest req, HttpServletResponse res) throws DespSecurityException {
        // TODO prograqmtic authentication
    }

    public Map<String,Object> getDespOauth2Attributes(){
        return getDespPrincipal().getAttributes();
    }


}
