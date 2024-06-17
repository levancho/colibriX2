package com.despani.core.services;


import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.despani.core.beans.mongo.DespComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@EnableScheduling
public class NotificationsService {


    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(final SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }


    private final SimpMessagingTemplate simpMessagingTemplate;


//    @Autowired
//    private CamelContext camelContext;


    public NotificationsService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }



//    @Scheduled(fixedRate = 5000)
    public void doNotify() throws IOException {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .data(DATE_FORMATTER.format(new Date()) + " : " + UUID.randomUUID().toString()));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            try {
                emitter.send("/" + " @ " + new Date());
                // we could send more events
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }


    @SendTo("_/topic/comments")
    public DespComment _addComment(DespComment comment) {
        log.info("sending comment {} to /topic/comments",comment);
      return comment;
    }

//    @SendTo("/topic/comments")
    public void __addComment(DespComment comment) {
        log.info("sending comment {} to /topic/comments",comment);

        Map map = new HashMap();
        map.put("operation", "add");
        this.simpMessagingTemplate.convertAndSend("/topic/comments", comment);
    }

    public void dispatchUpdateComment(DespComment comment) {
        log.info("sending comment {} to /topic/comments",comment);

        Map map = new HashMap();
        map.put("operation", "update");
        this.simpMessagingTemplate.convertAndSend("/topic/comments", comment,map);
    }

    public void dispatchAddComment(DespComment comment) {
        log.info("sending comment {} to /topic/comments",comment);

//        camelContext.createProducerTemplate().sendBody("activemq:topic:topic/comments", comment);
//        camelContext.createProducerTemplate().sendBody("activemq:topic/comments", comment);
//        camelContext.createProducerTemplate().sendBody("activemq:topic:comments", comment);
//        camelContext.createProducerTemplate().sendBody("activemq:comments", comment)


        Map map = new HashMap();
//        map.put("operation", "add");
        this.simpMessagingTemplate.convertAndSend("/topic/comments", comment,map);
//        this.simpMessagingTemplate.convertAndSend("/topic/comments", comment);
    }

    @MessageMapping("/messaging")
    public void messaging(Message<Object> message) {
        // get the user associated with the message
        Principal user = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        // notify all users that a user has joined the topic
        simpMessagingTemplate.convertAndSend("/topic/users", user.getName());
    }


//    // example
//    @Scheduled(cron = "*/5 * * * * *")
//    public void performTask() {
//        Instant now = Instant.now();
//        log.info("Scheduled task performed at {} (ISO 8601 date and time format)", now);
//        this.simpMessagingTemplate.convertAndSend("/queue/now", now);
//    }
}
