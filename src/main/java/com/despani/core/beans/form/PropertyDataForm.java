package com.despani.core.beans.form;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDataForm   extends ABaseForm {

    protected  Integer oid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String name;


    protected String propValue;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public List<ReferenceValueForm> getRefValues() {
        return refValues;
    }

    public void setRefValues(List<ReferenceValueForm> refValues) {
        this.refValues = refValues;
    }

    @Getter @Setter
    protected String description;
    protected String tab;
    protected int type;
    protected boolean published;
    protected boolean encrypted;


    protected List<ReferenceValueForm> refValues = new ArrayList<>();

    public String toString(){
        return tab+"."+type+"."+name.trim();
    }

    public String getPropValue(){
        if(propValue==null){
            propValue="off";
        }
        return propValue;
    }

    public String getErrorMessage(){
        return "Property Already Exists With this "+this.name+" name!";
    }



}
