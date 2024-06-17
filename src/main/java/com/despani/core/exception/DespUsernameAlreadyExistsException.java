package com.despani.core.exception;

public class DespUsernameAlreadyExistsException extends DespUserException{
    public DespUsernameAlreadyExistsException(String username) {
        super($.DESP_USER_NAME_ALREADY_EXISTS, username);
    }
}
