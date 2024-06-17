package com.despani.core.beans.form;


import com.despani.core.beans.domains.DespCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {
    int oid = -1;
    String title;
    String description;
    String sku;
    int price;
    Boolean published;
    int companyOid;
    int categoryOid;
    int parentOid = -1;
}
