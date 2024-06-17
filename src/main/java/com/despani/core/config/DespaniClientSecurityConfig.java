package com.despani.core.config;

import com.despani.core.beans.oauth.DespaniOAuth2User;
import com.despani.core.config.beans.DespaniAuthenticationSuccessHandler;
import com.despani.core.config.beans.DespaniConfigProperties;
import com.despani.core.managers.DespSecurityManager;
import com.despani.core.services.DespaniOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;


@EnableOAuth2Client
@Configuration
@EnableWebSecurity(debug=true)
public class DespaniClientSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    DespaniOauth2UserService despaniOauth2UserService;

    @Autowired
    DespSecurityManager secMan;

    @Bean
    AuthenticationSuccessHandler  despaniAuthenticationSuccessHandler () {
        return new DespaniAuthenticationSuccessHandler();
    }


//
//    @Bean
//    public IDespSecurityManager secManager() {
//        return new DespSecurityManager();
//    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

//    @Autowired
//    RoleHierarchy roleHierarchy;
//
//    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
//        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
//        return defaultWebSecurityExpressionHandler;
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
//        .expressionHandler(myMethodSecurityExpressionHandler)
                .antMatchers("/",
                        "/xout",
                        "/app/**",
                        "/login**",
                        "/js/**",
                        "/static/**",
                        "/css/**",
                        "/fonts/**",
                        "/images/**",
                        "/resources/images/**",
                        "/chat/**",
                        "/ckeditor/**",
                        "/ckeditor5/**",
                        "/tinymce/**",
                        "/resources/**",
                        "/icons/**",
                        "/react/**",
                        "/webjars/**",
                        "/stomp",
                        "/rest/**",
                        "/stomp/**",
                        "/front/**",
                        "/company/**",
                        "/company/save/**",
                        "/company/deleteMedia/**",
                        "/product/**")
                .permitAll()

                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(secMan)

                .and()

                .oauth2Login()
                .successHandler(despaniAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(despaniOauth2UserService)

                .customUserType(DespaniOAuth2User.class, "despani");

        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());


    }

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/css/**");
//        web.ignoring().antMatchers("/js/**");
//        web.ignoring().antMatchers("/images/**");
//        web.ignoring().antMatchers("/fonts/**");
//    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(DespaniConfigProperties props) {
        DespaniConfigProperties._SSo ssoProps = props.getSsoconf();

        ClientRegistration registration = ClientRegistration.withRegistrationId("despani")
                .clientId(ssoProps.getClientId())
                .clientSecret(ssoProps.getClientSecret())
                .clientName(ssoProps.getClientName())
                .scope(props.getSsoconf().getScope())
                .authorizationUri(ssoProps.getAuthorizationUri())
                .tokenUri(ssoProps.getTokenUri())
                .userInfoUri(ssoProps.getUserInfoUri())
                .redirectUriTemplate(ssoProps.getRedirectUriTemplate())
                .authorizationGrantType(new AuthorizationGrantType(ssoProps.getAuthorizationGrantType()))
                .userNameAttributeName(ssoProps.getUserNameAttribute())
                .build();

        List<ClientRegistration> clientRegistrationList = new ArrayList<>();
        clientRegistrationList.add(registration);
        return new InMemoryClientRegistrationRepository(clientRegistrationList);
    }
}
