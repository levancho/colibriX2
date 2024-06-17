package com.despani.core.beans.mongo;

import com.despani.core.mongo.annotation.CascadeSave;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
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
