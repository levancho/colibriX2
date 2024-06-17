package com.despani.core.exceptions;

import com.despani.core.interfaces.IdespEnumTypes;
import com.despani.core.exceptions.base.ADespBaseRuntimeException;

import java.text.MessageFormat;

public class DespRuntimeException extends ADespBaseRuntimeException {

    public DespRuntimeException(IdespEnumTypes extTypeParam, Object... arguments) {
        super(extTypeParam, arguments);
    }

    public DespRuntimeException(String s) {
        super(_R.DESP_USER_GENRIC_EXC,s);
    }

    public enum _R implements IdespEnumTypes {

        DESP_USER_GENRIC_EXC(8000,"desp.user.generic.msg","Generic error occured {1}"),
        DESP_PARAMETER_NULL_EXC(8000, "desp.parameter.null.msg", "Parameter {1} is null"),
        DESP_PASSWORD_NOT_VALID_EXC(8000, "desp.password.notvalid.msg", "Password not valid")
        ;

        private short code;
        private String key;
        private String value;

        public short getCode() {
            return code;
        }

        public String getKey() {
            return key;
        }

        public String getValue(Object ... arguments){
            if(arguments!=null) {
                return  MessageFormat.format(
                        value,
                        arguments);
            }else {
                return value;
            }
        }

        private _R(int codeParam, String keyParam, String valueParam) {
            this.code = (short) codeParam;
            this.key = keyParam;
            this.value = valueParam;
        }

        @Override
        public String toString() {

            return this.code+":"+this.key+":"+this.value;

        }

    }
}
