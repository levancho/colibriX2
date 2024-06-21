package com.despani.x2.core.beans.base;

import com.despani.x2.core.exceptions.base.DespRuntimeException;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.List;

public abstract class ADespNodeItem extends ADespaniContentObject {



    @JsonBackReference
    protected ADespNodeItem parent;

    protected List<ADespNodeItem> children;
    private Short lft;
    private Short rgt;
    private Integer parent_oid;

    public ADespNodeItem() {
        this.children= new ArrayList<>();
    }



    public ADespNodeItem getXParent() {
        return parent;
    }

    public void setParent(ADespNodeItem parent) {

        if( this == parent || (parent!=null && parent.getOid()==this.getOid()) ){
            throw new DespRuntimeException(DespRuntimeException.$.DESP_USER_GENRIC_EXC);
        }
        this.parent = parent;
    }

    public List<ADespNodeItem> getChildren() {
        return children;
    }

    public boolean hasChildren () {
        return (getChildren() != null && getChildren().size()>0);
    }

    public void setChildren(List<ADespNodeItem> children) {
        this.children = children;
    }

    public void addChild( ADespNodeItem childItem){
        childItem.setParent(this);
        this.children.add(childItem);
    }

    public Short getLft() {
        return lft;
    }

    public void setLft(Short lft) {
        this.lft = lft;
    }

    public Short getRgt() {
        return rgt;
    }

    public void setRgt(Short rgt) {
        this.rgt = rgt;
    }

    public Integer getParent_oid() {
        return parent_oid;
    }

    public void setParent_oid(Integer parent_oid) {
        this.parent_oid = parent_oid;
    }
}
