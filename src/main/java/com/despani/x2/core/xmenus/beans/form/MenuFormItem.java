package com.despani.x2.core.xmenus.beans.form;

import com.despani.x2.core.beans.form.ABaseForm;
import lombok.Data;

@Data
public class MenuFormItem extends ABaseForm {
    private Integer menuTypeOid;
    private Integer parentOid;
    private String name;
    private String url;
    private String img;

}
