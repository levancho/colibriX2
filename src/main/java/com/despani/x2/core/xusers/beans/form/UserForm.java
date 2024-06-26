package com.despani.x2.core.xusers.beans.form;

import com.despani.x2.core.beans.form.ABaseForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserForm extends ABaseForm {



    protected List<Integer> roles = new ArrayList<>();
    protected List<Integer> localRoles = new ArrayList<>();
    protected Map<String,String> props = new HashMap<>();
    protected String firstName;
    protected String email;
    protected String lastName;
    protected boolean enabled;
    protected boolean locked;

}
