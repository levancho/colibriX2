package com.despani.core.beans.mongo;

import com.despani.core.beans.base.ADespaniDisplayObject;
import com.despani.core.beans.enums.DespContentTypes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.Principal;
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
