package com.despani.x2.core.config;

import com.despani.x2.core.annotations.DespAsyncEvent;
import com.despani.x2.contents.processor.TabsTagProcessor;
import com.despani.x2.contents.processor.VideoTagProcessor;
import com.despani.x2.core.utils.DespProperties;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.lang.annotation.Annotation;

@ComponentScan({"com.despani.x1.utils",
        "com.despani.x1.events",
        "com.despani.x1.beans",
        "com.despani.x1.beans.*",
        "com.despani.x1.repository",
        "com.despani.x1.config.beans",
        "com.despani.x2.core.exceptions.handlers",
        "com.despani.x1.despaniPostOffice",
        "com.despani.x1.controllers",
        "com.despani.x1.controllers.*",
        "com.despani.x1.controllers.mvc",
        "com.despani.x1.services",
        "com.despani.x1.filters",
        "com.despani.x1.events.listeners",
        "com.despani.x1.managers",
        "com.despani.x1.security",
})
@Slf4j
@EnableTransactionManagement
@Configuration
@EnableAsync
@MapperScan(basePackages = {"com.despani.x1.mybatis.mappers", "com.despani.x1.mybatis.mappers.client.gen",
        "com.despani.x1.mybatis.mappers", "com.despani.xelosani.mappers"})
public class RootConfig {

    @Autowired
    private Environment env;

    @Qualifier("applicationTaskExecutor")
    @Autowired
    AsyncTaskExecutor executor;

    @PostConstruct
    public void init() {
        log.info("initialization happened");
    }


    @Bean
    public DespProperties props () {
        return DespProperties.getInstance();
    }


    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        log.debug("creating multicaster");
        return new SimpleApplicationEventMulticaster() {
            @Override
            public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
                ResolvableType type = eventType != null ? eventType : ResolvableType.forInstance(event);
                System.out.println("");
                Annotation[] declaredAnnotations = event.getClass().getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (declaredAnnotation.annotationType().equals(DespAsyncEvent.class)) {
                        DespAsyncEvent despann = (DespAsyncEvent) declaredAnnotation;
                        if (!despann.async()) {
                            log.debug("Dispatching Event {} in  Sync ", event);
                            getApplicationListeners(event, type).forEach(l -> invokeListener(l, event));
                            return;
                        }
                    }
                }
                log.debug("Dispatching Event {} in  Async ", event);
                getApplicationListeners(event, type).forEach(l -> executor.execute(() -> invokeListener(l, event)));
            }
        };
    }

    @Bean
    public TabsTagProcessor tabtagsProcessor() {
        return new TabsTagProcessor();
    }

    @Bean
    public VideoTagProcessor videotagsProcessor() {
        return new VideoTagProcessor();
    }


    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();

//        multipartResolver(100000000);
        return multipartResolver;
    }

}
