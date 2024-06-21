package com.despani.x2.core.xusers.exceptions;

public class DespUsernameAlreadyExistsException extends DespUserException {
    public DespUsernameAlreadyExistsException(String username) {
        super($.DESP_USER_NAME_ALREADY_EXISTS, username);
    }
}
