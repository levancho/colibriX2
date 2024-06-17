package com.despani.core.events;

import org.springframework.context.ApplicationEvent;

public class AuthenticationEventX2<T>  {

    private T what;
    protected boolean success;

    public AuthenticationEventX2(T  what, boolean success) {

        this.what = what;
        this.success = success;
    }

}
