package com.despani.x2.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface DespAsyncEvent {
    public boolean async() default  true;
}
