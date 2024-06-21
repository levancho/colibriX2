package com.despani.x2.core.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class DespResponse<T> {


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public T data;

    public boolean success;

    public String message;
    public String errorCode;

    public DespResponse(T data) {
        this.data = data;
    }

    public DespResponse() {
    }
}
