package com.despani.x2.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WSSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {


    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/app/**","/stomp/app/**").authenticated();
    }

}
