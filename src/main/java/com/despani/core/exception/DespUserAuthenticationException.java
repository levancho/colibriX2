package com.despani.core.exception;

import org.springframework.security.core.AuthenticationException;

public class DespUserAuthenticationException extends AuthenticationException {

    public DespUserAuthenticationException(String msg) {
        super(msg);
    }
}
