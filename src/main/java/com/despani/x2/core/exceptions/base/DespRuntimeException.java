package com.despani.x2.core.exceptions.base;

import com.despani.x2.core.interfaces.IdespEnumTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DespRuntimeException extends ADespBaseRuntimeException {

    public DespRuntimeException(IdespEnumTypes extTypeParam, Object... arguments) {
        super(extTypeParam, arguments);
    }

    public DespRuntimeException(String s) {
        super($.DESP_USER_GENRIC_EXC,s);
    }

    @AllArgsConstructor
    @Getter
    public enum $ implements IdespEnumTypes {

        DESP_USER_GENRIC_EXC(8000,"","desp.user.generic.msg","Generic error occured {1}"),
        DESP_PARAMETER_NULL_EXC(8000,"", "desp.parameter.null.msg", "Parameter {1} is null"),
        DESP_PASSWORD_NOT_VALID_EXC(8000, "","desp.password.notvalid.msg", "Password not valid");

        final private int code;
        final private String title;
        final private String key;
        final private String defaultValue;

        @Override
        public String toString() {
            return this.code+":"+this.title+":"+this.key+":"+this.defaultValue;
        }


        }
}
