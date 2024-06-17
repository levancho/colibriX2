package com.despani.core.exception;

import com.despani.core.exceptions.DespRuntimeException;

public class DespaniPasswordNotValidException extends DespRuntimeException {
    public DespaniPasswordNotValidException() {
        super(_R.DESP_PASSWORD_NOT_VALID_EXC);
    }
}
