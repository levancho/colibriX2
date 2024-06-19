package com.despani.core.config;


import jakarta.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

//@Configuration
// disabling camel for now.
public class CamelConfig {

//
//
//    @Bean
//    CamelContextConfiguration contextConfiguration() {
//        return new CamelContextConfiguration() {
//
//
//            @Override
//            public void beforeApplicationStart(CamelContext camelContext) {
//                camelContext.setStreamCaching(true);
//            }
//
//            @Override
//            public void afterApplicationStart(CamelContext camelContext) {
//
//            }
//        };
//    }




//    @Bean
//    RouteBuilder myRouter() {
//        return new RouteBuilder() {
//
//            @Override
//            public void configure() throws Exception {
//                // listen the queue named rt_messages and upon receiving a new entry
//                // simply redirect it to a bean named queueHandler which will then send it to users over STOMP
//                from("activemq:rt_messages").to("bean:queueHandler");
//            }
//        };
//    }
}