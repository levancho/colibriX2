package com.despani.core.config.beans;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "despani.app")
public class DespaniConfigProperties {

    @PostConstruct
    public void init() {
        System.out.println(" DespaniProperties was created");
    }


    @Getter @Setter
    private String home;

    @Getter @Setter
    private String encryptionType;

    @Getter @Setter
    private ConfObj conf;


    @Getter @Setter
    private _SSo ssoconf;

    @Getter @Setter
    private  OpenFireProps openfire;

    @Getter @Setter
    private  Props props;


    @Getter @Setter
    private  ACL acl;


    @Getter @Setter
    private  Role role;



    public static class _SSo {
        @Setter @Getter
        private boolean enabled;
        @Setter @Getter
        private String clientId;
        @Getter @Setter
        private String clientSecret;
        @Getter @Setter
        private String authorizationGrantType;
        @Getter @Setter
        private String redirectUriTemplate;
        @Getter @Setter
        private String authorizationUri;
        @Getter @Setter
        private String tokenUri;
        @Getter @Setter
        private String userInfoUri;
        @Getter @Setter
        private String userNameAttribute;
        @Getter @Setter
        private String scope;
        @Getter @Setter
        private String clientName;
        @Getter @Setter
        private String logoutAuthUrl;
        @Getter @Setter
        private String serverUrl;
    }


    public static class Props{
        @Getter @Setter
        String sendgridkey;

        @Getter @Setter
        String fromEmail;


        @Getter @Setter
        String toEmail;


        @Getter @Setter
        List<String> systemProps;

    }

    public static class ACL{
        @Getter @Setter
        boolean enabled;

    }
    public static class OpenFireProps{
        @Getter @Setter
        private String url;
        @Getter @Setter
        private String credUrl;
    }

    public static class Role{
        @Getter @Setter
        private String prefix;
    }



    private static class ConfObj {
        @Setter @Getter
        private String name;

        @Setter @Getter
        private String lastnake;

    }



}
