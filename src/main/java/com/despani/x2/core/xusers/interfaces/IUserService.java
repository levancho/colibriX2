package com.despani.x2.core.xusers.interfaces;

import com.despani.x2.core.xusers.beans.domains.DespRole;
import com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xusers.exceptions.DespUserAlreadyExistsException;
import com.despani.x2.core.xusers.exceptions.DespUserInvalidPasswordException;
import com.despani.x2.core.xusers.exceptions.DespUserNotFoundException;

import java.util.List;

public interface IUserService {

    Integer editUser(DespaniUser user) ;

    List<DespaniUser> getPagedUsers(int limit, int offset);

    DespaniUser getUserByOid(int oid);

    DespaniUser getUserByEmail(String email);

    List<DespRole> getAllRoles();

    boolean updateRolesForUser(int oid, List<Integer> roleIds) ;

    DespaniUser createUser(DespaniUser despaniUser) throws DespUserNotFoundException, DespUserInvalidPasswordException, DespUserAlreadyExistsException;

    int countUsers();

    public String updateRole(DespRole despRole);

    public String createRole(DespRole despRole);

    DespaniUser getDespaniUser(String email);

    void lockUser(Integer userOid, Boolean lock);

    void enableUser(Integer userOid, Boolean enable);
}
