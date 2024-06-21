package com.despani.x2.core.xusers.beans.domains;

import com.despani.x2.core.beans.base.ADespaniDisplayObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Data
public class DespRole extends ADespaniDisplayObject implements GrantedAuthority{

    private String role;
    private String roleDescription;
    private int weight;
    private boolean external;

    public DespRole(){};
    public DespRole(String roleName){
       this.role = roleName;
    };


    @Override
    public String getAuthority() {
        return role;
    }


    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.ROLE;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role, roleDescription, weight, external);
    }
}
