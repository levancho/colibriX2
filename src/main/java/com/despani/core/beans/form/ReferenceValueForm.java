package com.despani.core.beans.form;

import lombok.Data;

@Data
public class ReferenceValueForm {
    protected int refOid;
    protected String name;
    protected String description;
    protected boolean defaultVal;
    protected boolean published;
    protected int alt_seq;


    public String getValue (){
        return name;
    };

}
