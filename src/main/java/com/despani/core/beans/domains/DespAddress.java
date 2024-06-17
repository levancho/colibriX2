package com.despani.core.beans.domains;


import com.despani.core.beans.base.ADespaniObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
