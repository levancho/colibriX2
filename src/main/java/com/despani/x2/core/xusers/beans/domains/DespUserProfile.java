package com.despani.x2.core.xusers.beans.domains;

import com.despani.x2.core.beans.base.ADespaniObject;
import lombok.Data;

import java.util.Date;

@Data
public class DespUserProfile extends ADespaniObject {
    private String gender;
    private Date dob;
    private String avatar;
    private String cell;


}
