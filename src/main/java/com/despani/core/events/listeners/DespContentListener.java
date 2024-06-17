package com.despani.core.events.listeners;

import com.despani.core.events.DespOnBeforeContentEvent;
import com.despani.core.events.DespOnContentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DespContentListener {


    // @Async
    @EventListener
    public void handleContextStart(DespOnBeforeContentEvent cse) {
        log.info("Handling context started event.{}",cse);
    }


    @EventListener
    public void handleContextStart(DespOnContentEvent cse) {
        log.info("Handling context started event.{}",cse);
    }
}
