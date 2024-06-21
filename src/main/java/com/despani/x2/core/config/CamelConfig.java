package com.despani.x2.core.config;


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