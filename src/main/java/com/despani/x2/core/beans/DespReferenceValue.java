package com.despani.x2.core.beans;

import com.despani.x2.core.beans.base.ADespaniObject;
import lombok.Data;

@Data
public class DespReferenceValue extends ADespaniObject {

    private int alt_seq;
    private String value;
    private String description;
    private boolean published;

}
