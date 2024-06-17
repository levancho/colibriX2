package com.despani.core.beans;

import com.despani.core.beans.base.ADespaniObject;
import lombok.Data;

@Data
public class DespReferenceValue extends ADespaniObject {

    private int alt_seq;
    private String value;
    private String description;
    private boolean published;

}
