package com.despani.x2.core.controllers.mvc;

import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auto")
public class CredController {

    @RequestMapping("/creds")
    public @ResponseBody
    Map<String,String> getCreds(@AuthenticationPrincipal DespaniPrincipal principal){
//        OAuth2User defaultOAuth2User = (OAuth2User) ((Authentication) principal).getPrincipal();
        Map<String,Object> dt = (Map<String, Object>) principal.getAttributes();

        Map<String,Object> details = (Map<String, Object>) dt.get("details");
        String token  = (String) details.get("tokenValue");
        String username = (String)dt.get("name");

        Map<String,String> creds = new HashMap<>();
        creds.put("jid",username);

        creds.put("password",token);
        return creds;
    }
}
