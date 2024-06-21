package com.despani.x2.core.config;

import com.despani.x2.core.interceptors.BreadCrumbInterceptor;
import com.despani.x2.core.interceptors.LogInterceptor;
import com.despani.x2.core.interceptors.MaintenanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Bean
    HandlerInterceptor adminbreadCrumb () {
        return new BreadCrumbInterceptor();
    }

    @Bean
    HandlerInterceptor loginInterceptor () {
        return new LogInterceptor();
    }

    @Bean
    MaintenanceInterceptor maintananceInterceptor () {
        return new MaintenanceInterceptor();
    }

    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en", "US"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {  // LogInterceptor apply to all URLs.
//        registry.addInterceptor(breadCrumb());

        // Old Login url, no longer use.
        // Use OldURLInterceptor to redirect to a new URL.
        registry.addInterceptor(loginInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(maintananceInterceptor()).addPathPatterns("/app/**");
        registry.addInterceptor(adminbreadCrumb())
                .addPathPatterns("/admin/**");

//        // This interceptor apply to URL like /admin/*
//        // Exclude /admin/oldLogin
//        registry.addInterceptor(new AdminInterceptor())//
//                .addPathPatterns("/admin/*")//
//                .excludePathPatterns("/admin/oldLogin");
    }
    /*
     * Configure ContentNegotiationManager
     */

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/resources/**")
//                .addResourceLocations("classpath:/static/css/");
////                .addResourceHandler("/static/**")
////                .addResourceLocations("classpath:/static/");
//    }

//    @Override
//    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }

//    @Override
//    public void addViewControllers(final ViewControllerRegistry registry) {
//        registry.addViewController("/")
//                .setViewName("forward:/index");
//        registry.addViewController("/index");
//        registry.addViewController("/securedPage");
//    }
//


//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/");
//    }


//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//
//            "classpath:/META-INF/resources/", "classpath:/resources/",
//            "classpath:/static/", "classpath:/public/" };
//
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if (!registry.hasMappingForPattern("/webjars/**")) {
//            registry.addResourceHandler("/webjars/**").addResourceLocations(
//                    "classpath:/META-INF/resources/webjars/");
//        }
//        if (!registry.hasMappingForPattern("/**")) {
//            registry.addResourceHandler("/**").addResourceLocations(
//                    CLASSPATH_RESOURCE_LOCATIONS);
//        }
//    }






}
