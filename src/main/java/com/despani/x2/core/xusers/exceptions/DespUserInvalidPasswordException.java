package com.despani.x2.core.xusers.exceptions;

public class DespUserInvalidPasswordException extends DespUserException {
    public DespUserInvalidPasswordException() {
        super($.DESP_USER_INVALID_PASS_OR_ID);
    }

}
