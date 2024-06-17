package com.despani.core.exception;

import com.despani.core.exceptions.DespRuntimeException;

public class DespaniParameterNullRuntimeException extends DespRuntimeException {
    public DespaniParameterNullRuntimeException(String parameter) {
        super(_R.DESP_PARAMETER_NULL_EXC,parameter);
    }
}
