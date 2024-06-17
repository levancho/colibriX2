package com.despani.core.exception;

public class DespUserInvalidPasswordException extends DespUserException {
    public DespUserInvalidPasswordException() {
        super($.DESP_USER_INVALID_PASS_OR_ID);
    }

}
