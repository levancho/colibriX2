package com.despani.x2.core.xusers.beans.domains;


import com.despani.x2.core.beans.base.ADespaniObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespAddress extends ADespaniObject {

    private String name;
    private String address;
    private String city;
    private String state;
    private int zipCode;

}
