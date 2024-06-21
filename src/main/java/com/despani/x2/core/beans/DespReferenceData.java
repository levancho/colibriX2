package com.despani.x2.core.beans;

import com.despani.x2.core.beans.base.ADespaniObject;
import lombok.Data;

import java.util.List;

@Data
public class DespReferenceData  extends ADespaniObject {


    private String typeKey;
    private String name;

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<DespReferenceValue> getValues() {
        return values;
    }

    public void setValues(List<DespReferenceValue> values) {
        this.values = values;
    }

    private String description;
    private boolean published;
    private List<DespReferenceValue> values;


}
