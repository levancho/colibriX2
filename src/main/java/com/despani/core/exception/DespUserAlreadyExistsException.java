package com.despani.core.exception;

public class DespUserAlreadyExistsException extends DespUserException{
    public DespUserAlreadyExistsException(String username) {
        super($.DESP_USER_EMAIL_ALREADY_EXISTS, username);
    }
}
