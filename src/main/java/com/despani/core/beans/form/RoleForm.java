package com.despani.core.beans.form;

import lombok.Data;

@Data
public class RoleForm  extends ABaseForm{
    protected String role;
    protected int weight;
    protected String roleDescription;
}
