package com.despani.x2.core.events;

import org.springframework.context.ApplicationEvent;

public class AuthenticationEvent extends ApplicationEvent {



    protected boolean success;

    public AuthenticationEvent(Object source  , boolean success) {
        this(source);
        this.success = success;

    }


    public AuthenticationEvent(Object source) {
        super(source);
    }
}
