package com.despani.core.beans.form;

import lombok.Data;

@Data
public class CategoryFormItem extends ABaseForm {
    private Integer categoryTypeOid;
    private Integer parentOid;
    private String name;
    private String alias;
    private String description;

}
