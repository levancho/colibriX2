package com.despani.x2.core.exceptions.base;

import com.despani.x2.core.interfaces.IdespEnumTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DespException extends ADespBaseException {


    public DespException(IdespEnumTypes extTypeParam, Object... arguments) {
        super(extTypeParam, arguments);
    }

    @AllArgsConstructor
    @Getter
    public enum $ implements IdespEnumTypes {


        DESP_USER_GENERIC_ERROR(1000, "User Exception","desp.user.generic.error", "Generic error occured");

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
