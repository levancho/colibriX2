package com.despani.core.beans;


import lombok.Data;

@Data
public class DespGlobalLocalProp {

    private String name;
    private String shortName;
    private String propValue;
    private int type;
    private boolean global;


    public DespGlobalLocalProp(String name, String shortName, String propValue, int type, boolean global) {
        this.name = name;
        this.shortName = shortName;
        this.propValue = propValue;
        this.type = type;
        this.global = global;
    }

    public DespGlobalLocalProp() {

    }


}
