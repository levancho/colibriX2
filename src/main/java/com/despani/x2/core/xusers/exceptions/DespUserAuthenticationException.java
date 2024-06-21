package com.despani.x2.core.xusers.exceptions;

import org.springframework.security.core.AuthenticationException;

public class DespUserAuthenticationException extends AuthenticationException {

    public DespUserAuthenticationException(String msg) {
        super(msg);
    }
}
