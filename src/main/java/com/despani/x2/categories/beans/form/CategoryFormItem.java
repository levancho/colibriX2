package com.despani.x2.categories.beans.form;

import com.despani.x2.core.beans.form.ABaseForm;
import lombok.Data;

@Data
public class CategoryFormItem extends ABaseForm {
    private Integer categoryTypeOid;
    private Integer parentOid;
    private String name;
    private String alias;
    private String description;

}
