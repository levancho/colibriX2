package com.despani.x2.companies.beans.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
