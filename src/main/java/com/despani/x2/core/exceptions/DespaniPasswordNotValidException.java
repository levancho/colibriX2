package com.despani.x2.core.exceptions;

import com.despani.x2.core.exceptions.base.DespRuntimeException;

public class DespaniPasswordNotValidException extends DespRuntimeException {
    public DespaniPasswordNotValidException() {
        super($.DESP_PASSWORD_NOT_VALID_EXC);
    }
}
