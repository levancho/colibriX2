package com.despani.x2.core.controllers.mvc;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.despani.x2.core.services.NotificationsService;

@RestController
public class SsePushNotificationRestController {

    @Autowired
    NotificationsService service;


    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @GetMapping("/notification")
    public ResponseEntity<SseEmitter> doNotify() throws InterruptedException, IOException {
        final SseEmitter emitter = new SseEmitter();
        service.addEmitter(emitter);
        service.doNotify();
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }



}