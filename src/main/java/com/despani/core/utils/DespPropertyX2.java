package com.despani.core.utils;

import com.despani.core.beans.DespReferenceData;
import com.despani.core.beans.enums.DespPropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespPropertyX2 {

    private String name;
    private String propValue;
    private DespPropertyType xtype;
    private String xsection;
    private boolean encrypted;
    private String iv;
    private Integer oid;
    private String shortName;
    private DespReferenceData refData;

    public DespPropertyX2(String defaultValue) {
        this.propValue=defaultValue;
        this.xsection="system";
        this.xtype=DespPropertyType.TEXT;
    }

    public DespPropertyX2(String name, String value, DespPropertyType type, String xsection, boolean isEncrypted, String ivString) {
        this.name=name;
        this.propValue=value;
        this.xtype=type;
        this.xsection=xsection;
        this.encrypted=isEncrypted;
        this.iv=ivString;
    }
}

