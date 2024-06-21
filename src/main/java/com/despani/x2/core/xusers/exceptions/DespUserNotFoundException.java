package com.despani.x2.core.xusers.exceptions;

public class DespUserNotFoundException extends DespUserException {
    public DespUserNotFoundException(String user) {
        super($.DESP_USER_NOT_FOUND, user);
    }

}
