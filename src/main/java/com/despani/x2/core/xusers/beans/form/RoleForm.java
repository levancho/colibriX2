package com.despani.x2.core.xusers.beans.form;

import com.despani.x2.core.beans.form.ABaseForm;
import lombok.Data;

@Data
public class RoleForm  extends ABaseForm {
    protected String role;
    protected int weight;
    protected String roleDescription;
}
