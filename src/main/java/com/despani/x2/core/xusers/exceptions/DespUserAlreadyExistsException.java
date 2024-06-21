package com.despani.x2.core.xusers.exceptions;

public class DespUserAlreadyExistsException extends DespUserException {
    public DespUserAlreadyExistsException(String username) {
        super($.DESP_USER_EMAIL_ALREADY_EXISTS, username);
    }
}
