package com.despani.core.exception;

public class DespUserNotFoundException extends DespUserException {
    public DespUserNotFoundException(String user) {
        super($.DESP_USER_NOT_FOUND, user);
    }

}
