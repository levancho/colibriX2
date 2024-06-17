package com.despani.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface Crumb {

    public String label();
    public String xdesc();
    public String family();
    public String parent();

}
