package com.despani.core.beans.form;

import com.despani.core.beans.domains.DespAddress;
import com.despani.core.beans.domains.DespMedia;
import com.despani.core.beans.domains.DespaniUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespCompanyForm {
    private String name;
    private String type;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private boolean published;
    private String companyDescription;
}
