package com.despani.core.beans.form;

import lombok.Data;

@Data
public class MenuFormItem extends ABaseForm {
    private Integer menuTypeOid;
    private Integer parentOid;
    private String name;
    private String url;
    private String img;

}
