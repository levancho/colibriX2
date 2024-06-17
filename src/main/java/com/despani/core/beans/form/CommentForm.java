package com.despani.core.beans.form;


import lombok.Data;

import java.util.Date;

@Data
public class CommentForm  {

    protected String oid;
    protected int content_id;// content id from DB
    protected String comment;
    protected String mode;

//    int reuseroid;
    protected Date createdOn;

    public CommentForm() {
        this.createdOn = new Date();
    }
}
