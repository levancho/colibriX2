package com.despani.core.beans.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract public class ADespaniObject implements Serializable  {

    protected Integer oid;
    protected Date createdOn;
    protected Date updatedOn;

    @Getter
    @Setter
    protected int createdByOid;

    @Getter
    @Setter
    protected int updatedByOid;


    public ADespaniObject() {
        this.oid = -1;
        this.setCreatedOn(new Date());
        this.setUpdatedOn(new Date());
    }

    public ADespaniObject(Date createdOn, Date updatedOn) {
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }


    public Integer getOid() {
        return oid;
    }

    public int getId() {
        return getOid();
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public Date getUpdatedOn() {
        if (updatedOn == null) updatedOn = this.createdOn;
        return updatedOn;
    }

    public String getPrettyCrtDateInString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sdt = sdf.format(this.createdOn);
        return sdt;
    }

    public String getPrettyUpdDateInString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String udt = sdf.format(this.updatedOn);
        return udt;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ADespaniObject)) {
            return false;
        }
        ADespaniObject thatObj = (ADespaniObject) obj;
        return (this.getOid() == thatObj.getOid());
    }

    @Override
    public int hashCode() {
        return this.oid * 7;
    }

}
