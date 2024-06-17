package com.despani.core.config.beans;

import com.despani.core.beans.example.MessageDTO;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class SampleAutowiredAmqRoute /** extends RouteBuilder **/ {


    private static Map<String, Object> defaultHeaders;

    static {
        defaultHeaders = new HashMap<String, Object>();
        // add the Content-Type: application/json header by default
        defaultHeaders.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
    }

//    @Override
//    public void configure() throws Exception {
//
////
//
////        from("timer:bar")
////                .setBody(constant("Hello from Camel"))
////                .to("activemq:foo");
//
////        from("timer:bar")
////                .setBody(constant("Hello from Muteli"))
////                .to("activemq:rt_messages");
//
//        from("activemq:/topic/comments").log(body().toString());
////        from("activemq:rt_messages").bean(SampleAutowiredAmqRoute.class, "muteli");
//    }

//
//    public void muteli(Exchange exchange) {
//        Message in = exchange.getIn();
//    }

}