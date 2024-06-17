package com.despani.core.managers;

import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.config.beans.DespaniConfigProperties;
import com.despani.core.interfaces.IManager;
import com.despani.core.mybatis.mappers.IUserMapper;
import com.despani.core.platform.interfaces.IUserService;
import com.despani.core.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserManager implements IManager {

    @Autowired
    public DespaniConfigProperties props;

    @Autowired
    IUserService userservice;

    @Autowired
    protected IUserMapper userMapper;


    @Autowired
    private RoleService roleService;


    public List<DespRole> getAllRoles(){
        List<DespRole>  roles = userservice.getAllRoles();
        if(props.getSsoconf().isEnabled()){
            List<DespRole> localRoles = roleService.getAllRoles();
            roles.addAll(localRoles);
        }
        return roles;
    }


    public DespaniUser getUserByOid(int oid)  {

        DespaniUser user = userservice.getUserByOid(oid);
        if(props.getSsoconf().isEnabled()){
        // TODO add local userinfo here
        }
        return user;

    }


    public Integer editUser(DespaniUser user) {

      int retvalue =   userservice.editUser(user);

        if(props.getSsoconf().isEnabled()){
            // TODO add local userinfo here
            // TODO userMapper.editUser(user.getOid(), user.getFirstName(), user.getLastName(), user.isLocked(), user.isEnabled());

        }
        return retvalue;
    }


    public void updateRolesForOid(int oid, List<Integer> roles, List<Integer> localRoles){
        userservice.updateRolesForUser(oid,roles);
        if(props.getSsoconf().isEnabled() && localRoles!=null & localRoles.size()>0) {
            roleService.updateRolesForUser(oid,localRoles);
        }
    }
}
