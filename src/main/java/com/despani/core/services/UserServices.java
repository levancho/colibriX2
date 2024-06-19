package com.despani.core.services;

import com.despani.core.beans.domains.DespFriend;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.enums.DespFriendStatus;
import com.despani.core.exception.*;
import com.despani.core.exceptions.DespRuntimeException;
import com.despani.core.mybatis.mappers.IUserMapper;
import com.despani.core.mybatis.mappers.IUserRoleMapper;
import com.despani.core.platform.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.*;


@Service
@ConditionalOnProperty(
        value = "despani.app.ssoconf.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class UserServices extends AUserService implements IUserService, UserDetailsService {



    @Autowired
    private RoleService roleService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IUserRoleMapper roleMapper;

    @Autowired
    IUserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    static int DEFAULT_ROLE_OID = 1;
    static int STATUS_PENDING = 0;
    static int STATUS_APPROVED = 1;


    private Validator getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }


    public void updateUserPasswordByUsername (String username, String newPassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        userMapper.updateUserPassword(username, encryptedPassword);
    }



    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public DespaniUser createUser(DespaniUser despaniuser) throws DespUserInvalidPasswordException, DespUserAlreadyExistsException {
        //check if username already exists in DB
        if (despaniuser == null) {


            throw new DespaniParameterNullRuntimeException("despaniuser");
        }

        // check user by username
        if (this.getDespaniUser(despaniuser.getUsername()) != null) {
            throw new DespUserAlreadyExistsException((despaniuser.getUsername()).toString());
        }
        // check user by email
        if (this.getDespaniUser(despaniuser.getEmail()) != null) {
            throw new DespUserAlreadyExistsException((despaniuser.getEmail()).toString());
        }




        if (despaniuser.getUsername() == null) {
            throw new DespaniParameterNullRuntimeException("despaniuser.getUsername()");
        }

        if (despaniuser.getPassword() == null) {
            throw new DespUserInvalidPasswordException();
        }


        despaniuser.setPassword(bCryptPasswordEncoder.encode(despaniuser.getPassword()));


        DespRole defaultRole = roleMapper.readUserRole(DEFAULT_ROLE_OID);
        despaniuser.setRoles(new HashSet<>(Arrays.asList(defaultRole)));
        String newUserName = despaniuser.getUsername();


        int recordStatus = userMapper.createUser(despaniuser);

        if (recordStatus != 1 || despaniuser.getOid() <= 0) {
            throw new DespRuntimeException(DespRuntimeException._R.DESP_USER_GENRIC_EXC);
        } else {

            despaniuser.getAuthorities().stream().forEach(userRole -> userMapper.createUserRole(newUserName, userRole.getAuthority()));

            int oid = despaniuser.getOid();
            Date dob = despaniuser.getProfile().getDob();
            String gender = despaniuser.getProfile().getGender();
            String cell = despaniuser.getProfile().getCell();
            int profileRecordStatus = userMapper.createProfile2(oid, dob, gender,cell);

            if (profileRecordStatus != 1) {
                throw new DespRuntimeException(DespRuntimeException._R.DESP_USER_GENRIC_EXC);
            }
        }

        despaniuser.setPassword("");

        return despaniuser;

    }


    @Transactional
    public void makeFriendRequest(Integer myOid, Integer friendOid) throws DespUserNotFoundException {

        if (myOid == null || friendOid == null) {
            throw new DespaniParameterNullRuntimeException("Friend oid or WhoOid");
        }

        // Retreive future friend user
        DespaniUser despFutureFriend = userMapper.getUserLight(friendOid);

        if (despFutureFriend == null) {
            throw new DespUserNotFoundException(friendOid + "");
        }

        // Retrieve this user
        DespaniUser thisFriend = userMapper.getUserLight(myOid);

        //Create friend request record in desp_friends table;
        userMapper.insertFriendRecord(myOid, friendOid, DespFriendStatus.PENDING);

        //TODO send email notofcation to new friend about request

    }


    @Transactional
    public DespaniUser acceptFriendRequest(Integer myOid, Integer requesterOid) throws
            DespFriendRequesterNotFound,
            DespFriendReqiesterStatusNotValid {
        if (myOid == null || requesterOid == null) {
            throw new DespaniParameterNullRuntimeException("Friend oid or WhoOid");
        }
        //Check if friend requester really exists
        DespFriend requester = userMapper.getFreindByOid(myOid, requesterOid);

        if (requester == null) {
            throw new DespFriendRequesterNotFound(requesterOid + "");
        }

        DespFriendStatus requesterStatus = requester.getStatus();

        if (requesterStatus != DespFriendStatus.PENDING) {
            throw new DespFriendReqiesterStatusNotValid(requesterStatus.getValue());
        }

        //if reached here then change status to approved;
        userMapper.insertFriendRecord(requesterOid, myOid, DespFriendStatus.APPROVED);
        //Update requester status
        userMapper.updateFriendStatus(myOid, requesterOid, DespFriendStatus.APPROVED);

        DespFriend despApprovedFriend = this.getFreind(myOid, requesterOid);
        //TODO sen email notificatioj to both users(friends)
        return despApprovedFriend;

    }


    @Transactional
    public DespaniUser declineFriendRequest(Integer myOid, Integer requesterOid) throws
            DespFriendRequesterNotFound,
            DespFriendReqiesterStatusNotValid {

        if (myOid == null || requesterOid == null) {
            throw new DespaniParameterNullRuntimeException("Friend oid or WhoOid");
        }
        //Check if friend requester really exists
        DespFriend requester = userMapper.getFreindByOid(myOid, requesterOid);

        if (requester == null) {
            throw new DespFriendRequesterNotFound(requesterOid + "");
        }

        DespFriendStatus requesterStatus = requester.getStatus();

        if (requesterStatus != DespFriendStatus.PENDING) {
            throw new DespFriendReqiesterStatusNotValid(requesterStatus.getValue());
        }

        userMapper.updateFriendStatus(myOid, requesterOid, DespFriendStatus.DECLIEND);

        DespaniUser despRejectedFriend = this.getFreind(myOid, requesterOid);
        //TODO sen email notificatioj to both users(friends)
        return despRejectedFriend;

    }


    @Transactional
    public void deleteFriend(Integer myOid, Integer deleteFriendOid) {

        if (myOid == null || deleteFriendOid == null) {
            throw new DespaniParameterNullRuntimeException("Friend oid or WhoOid");
        }
        userMapper.deleteFriend(myOid, deleteFriendOid);
        userMapper.deleteFriend(deleteFriendOid, myOid);
    }


    @Transactional
    public List<DespFriend> getMutualFriends(Integer myOid, Integer friendOid, Integer status) {

        if (myOid == null || friendOid == null || status == null) {
            throw new DespaniParameterNullRuntimeException("Friend oid or my oid or status");
        }

        List<DespFriend> mutualFriends = userMapper.mutualFriendsByStatus(myOid, friendOid, status);

        return mutualFriends;
    }


    public DespFriend getFreind(Integer myOid, Integer friendOid) {

        if (myOid == null || friendOid == null) {
            throw new DespaniParameterNullRuntimeException("oid vor friendOid ");
        }
        //get friends
        DespFriend myFriends = userMapper.getFreindByOid(myOid, friendOid);

        return myFriends;
    }


    @Transactional
    public DespFriend getDespFriendRecord(int oid, int oidf) {
        return userMapper.getFreindByOid(oid, oidf);

    }


    public List<DespaniUser> getUsers() {

        return userMapper.getAllUsers();
    }


    public void lockUser(Integer userOid, Boolean lock) {
        userMapper.lockUser(userOid, lock);
    }


    public void enableUser(Integer userOid, Boolean enable) {
        userMapper.enableUser(userOid, enable);
    }


    public Integer editUser(DespaniUser user) {
        userMapper.editUser(user.getOid(), user.getFirstName(), user.getLastName(), user.isLocked(), user.isEnabled());
        return 1;
    }


    @Override
    public DespaniUser getUserByOid(int oid) {
        return getUserByUserOid(oid);
    }


    @Override
    public DespaniUser getUserByEmail(String email) {
        return userMapper.getUserByUsernameEmail(email,"email");
    }


    public int countUsers() {
        return userMapper.countUsers();
    }


    @Override
    public String updateRole(DespRole despRole) {
        return "" + roleService.updateRole(despRole.getOid(), despRole.getRole(), despRole.getWeight(), despRole.isExternal(),despRole.getRoleDescription());
    }


    @Override
    public String createRole(DespRole despRole) {
        return "" + roleService.createRole(despRole.getRole(), despRole.getWeight(), despRole.getRoleDescription());
    }


    public List<DespaniUser> getAllUsers(int limitPerPage, int offsetMultiplier) {
        return userMapper.getAllUsersInfo(limitPerPage, offsetMultiplier);
    }


    public DespaniUser getUserByUserOid(Integer oid) {
        return userMapper.getUserByOid(oid);
    }


    public List<DespRole> getAllRoles() {
        return userMapper.getAllRoles();
    }


    public List<String> getAllRolesASStrings() {
        return userMapper.getAllRolesAsStrings();
    }


//
//    public DespaniUser getDespaniUser(String username){
//        DespaniUser despaniUser = userMapper.getUserByUsername(username);
//
//        return despaniUser;
//    }


    @Transactional
    public void saveUserRoles(Integer oid, List<Integer> roles) {
        for (Integer role : roles) {
            try {
                roleMapper.addRole(oid, role);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int deleteUserAllRoles(Integer oid) {
        return roleMapper.deleteUserAllRoles(oid);
    }


    @Override
    @Transactional
    public boolean updateRolesForUser(int oid, List<Integer> roles) {
        return roleService.updateRolesForUser(oid,roles);
    }


    public boolean nameIsAvalilable(String username) throws DespUsernameAlreadyExistsException {
        int result = userMapper.usernameExists(username);
        if (result > 0) {
            throw new DespUsernameAlreadyExistsException(username);
        }
        return true;
    }


    @Override
    public List<DespaniUser> getPagedUsers(int limit, int offset) {

        return getAllUsers(limit, offset);
    }





}
