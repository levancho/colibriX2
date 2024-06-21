package com.despani.x2.core.exceptions;

import com.despani.x2.core.exceptions.base.DespRuntimeException;

public class DespaniParameterNullRuntimeException extends DespRuntimeException {
    public DespaniParameterNullRuntimeException(String parameter) {
        super($.DESP_PARAMETER_NULL_EXC,parameter);
    }
}
