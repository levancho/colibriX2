package com.despani.x2.core.config;

import com.despani.x2.core.config.beans.DespaniAuthenticationSuccessHandler;
import com.despani.x2.core.config.beans.DespaniConfigProperties;
import com.despani.x2.core.managers.DespSecurityManager;
import com.despani.x2.core.xusers.interfaces.IUserService;
import com.despani.x2.core.xusers.services.AUserService;
import com.despani.x2.core.xusers.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug=true)
public class DespaniClientSecurityConfig    {

    @Autowired
    UserServices despaniOauth2UserService;

    @Autowired
    DespSecurityManager secMan;

    @Bean
    AuthenticationSuccessHandler  despaniAuthenticationSuccessHandler () {
        return new DespaniAuthenticationSuccessHandler();
    }


//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation("https://localhost:8080");
//    }

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


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.maximumSessions(1).sessionRegistry(sessionRegistry()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/",
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
                                "/product/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(secMan));
//                            .oauth2Login(oauth2 -> oauth2.successHandler(despaniAuthenticationSuccessHandler())
//
//                        .userInfoEndpoint(userInfo -> userInfo.userService(despaniOauth2UserService)));
//https://docs.spring.io/spring-security/site/docs/5.5.0/api/org/springframework/security/oauth2/client/userinfo/CustomUserTypesOAuth2UserService.html
//                       TODO  use  .customUserType(DespaniOAuth2User.class, "despani"));
        // http....;

        return http.build();
    }


//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .requestMatchers("/",
//                        "/xout",
//                        "/app/**",
//                        "/login**",
//                        "/js/**",
//                        "/static/**",
//                        "/css/**",
//                        "/fonts/**",
//                        "/images/**",
//                        "/resources/images/**",
//                        "/chat/**",
//                        "/ckeditor/**",
//                        "/ckeditor5/**",
//                        "/tinymce/**",
//                        "/resources/**",
//                        "/icons/**",
//                        "/react/**",
//                        "/webjars/**",
//                        "/stomp",
//                        "/rest/**",
//                        "/stomp/**",
//                        "/front/**",
//                        "/company/**",
//                        "/company/save/**",
//                        "/company/deleteMedia/**",
//                        "/product/**")
//                .permitAll()
//
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest()
//                .authenticated()
//
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessHandler(secMan)
//
//                .and()
//
//                .oauth2Login()
//                .successHandler(despaniAuthenticationSuccessHandler())
//                .userInfoEndpoint()
//                .userService(despaniOauth2UserService);
//
////  TODO              .customUserType(DespaniOAuth2User.class, "despani");
//
//        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
//
//
//    }

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/css/**");
//        web.ignoring().antMatchers("/js/**");
//        web.ignoring().antMatchers("/images/**");
//        web.ignoring().antMatchers("/fonts/**");
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }

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
                .redirectUri(ssoProps.getRedirectUriTemplate())
                .authorizationGrantType(new AuthorizationGrantType(ssoProps.getAuthorizationGrantType()))
                .userNameAttributeName(ssoProps.getUserNameAttribute())
                .build();

        List<ClientRegistration> clientRegistrationList = new ArrayList<>();
        clientRegistrationList.add(registration);
        return new InMemoryClientRegistrationRepository(clientRegistrationList);
    }
}
