package com.despani.x2.core.xusers.beans.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigInteger;
import java.util.*;

@Data
public class DespaniMongoUser  {

    
    private String username;

    protected BigInteger oid;

    @Id
    protected String _id;


    @DateTimeFormat(style = "M-")
    protected Date createdOn;
    @DateTimeFormat(style = "M-")
    protected Date updatedOn;


}
