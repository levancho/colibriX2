package com.despani.x2.core.xusers.services;


import  com.despani.x2.core.xusers.beans.domains.DespRole;
import com.despani.x2.core.xusers.mybatis.mappers.IUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {


    @Autowired
    private IUserRoleMapper roleMapper;


    public List<DespRole> getAllRoles(){
        return roleMapper.getAllRoles();
    }

    public int updateRole(int oid,String role,int weight,boolean external, String roleDesc) {
        return roleMapper.updateRole(oid,role,weight,external,roleDesc);
    }

    public int createRole(String role, int weight, String roleDescription) {
        return roleMapper.createRole(role,weight,roleDescription);
    }


    @Transactional
    public boolean updateRolesForUser(int oid, List<Integer> roles) {
        // Delete all roles
        roleMapper.deleteUserAllRoles(oid);

        boolean retval = true;
        // Add new roles
        for (Integer role : roles) {
            try {
//               retval &= (1== roleMapper.addRole(oid,role));
                retval = retval && (1 == roleMapper.addRole(oid, role));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retval;
    }
}
