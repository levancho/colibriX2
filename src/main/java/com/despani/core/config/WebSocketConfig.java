package com.despani.core.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.jms.ConnectionFactory;
import java.util.function.Predicate;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, EnvironmentAware {

//    @Override
//
//    public void configureMessageBroker(MessageBrokerRegistry config) {
////        config.enableSimpleBroker("/topic");
//        config.enableSimpleBroker("/topic", "/queue");
//
//    }
//


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        if(env.acceptsProfiles(Profiles.of("prod"))){
            config.enableStompBrokerRelay("/topic", "/queue");
        }else {
            config.enableSimpleBroker("/topic", "/queue");
        }
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*");
//        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }


    private Environment env;
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}