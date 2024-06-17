package com.despani.core.platform.interfaces;

import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.exception.DespUserAlreadyExistsException;
import com.despani.core.exception.DespUserInvalidPasswordException;
import com.despani.core.exception.DespUserNotFoundException;

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
