package com.despani.x2.contents.beans.mongo;

import com.despani.x2.contents.mongo.annotation.CascadeSave;
import com.despani.x2.core.xusers.beans.mongo.DespaniMongoUser;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DespComplaint {

    @DBRef
    @CascadeSave
    @Field("user")
    protected DespaniMongoUser user;

    @DateTimeFormat(style = "M-")
    protected Date createdOn;


}
