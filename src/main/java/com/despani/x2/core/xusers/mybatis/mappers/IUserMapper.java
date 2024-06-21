package com.despani.x2.core.xusers.mybatis.mappers;


import com.despani.x2.core.xusers.beans.PasswordResetToken;
import com.despani.x2.core.xusers.beans.domains.DespFriend;
import  com.despani.x2.core.xusers.beans.domains.DespRole;
import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xusers.beans.enums.DespFriendStatus;
import com.despani.x2.core.xusers.mybatis.typehandlers.DespFriendStatusHandler;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public interface IUserMapper {


    final String GET_USER_LIGHT = "SELECT oid,firstname,username,lastname,username,password,created_on,updated_on, locked, enabled FROM desp_users";

    //delete
    final String SELECT_ALL_NAMES = "SELECT firstname,lastname,created_on,updated_on FROM desp_users WHERE oid>0 limit #{limit} offset #{offset}";

    @Select(SELECT_ALL_NAMES)
    public List<DespaniUser> getAllNames(@Param("limit") int limit, @Param("offset") int offset);

//    @Select(GET_USER)

    final String GET_NUM_OF_ALL_USERS = "SELECT COUNT(firstname) AS NumberOfUsers FROM desp_users";

    @Select(GET_NUM_OF_ALL_USERS)
    public int getUserNumber();
    //delete

    //test
    final String GET_FIRST_NAME_AND_LAST = "SELECT firstname,lastname FROM desp_users WHERE username=#{username}";

    @Select(GET_FIRST_NAME_AND_LAST)
    public HashMap<String, String> getFirstLastName(@Param("username") String username);
    //end of test


    final String CREATE_USER_ROLES = "INSERT INTO desp_user_role (userid,roleid) SELECT desp_users.oid, desp_roles.oid FROM desp_users,desp_roles WHERE desp_users.username=#{username} AND desp_roles.role=#{userRole};";

    @Select("SELECT COUNT(oid) AS NumberOfUsers FROM desp_users")
    public int countUsers();


    @Select("SELECT desp_users.username, desp_users.oid FROM desp_users WHERE username=#{username}")
    @Results(value = {
            @Result(property = "username", column = "username"),
            @Result(property = "oid", column = "oid")

    })
    public DespaniUser checkForUserNameExistance(String enteredUsername);

    @Insert("INSERT INTO desp_account_Password_ResetTokens(user_oid,username,token,expirationdate) VALUES(#{userOid},#{username},#{token},#{expirationDate})")
    public int createPassRecoveryTokenRecord(PasswordResetToken passwordResetToken);

    @Select("Select username, token, expirationdate FROM desp_account_Password_ResetTokens WHERE username=#{email}")
    @Results(value = {
            @Result(property = "username", column = "username"),
            @Result(property = "token", column = "token"),
            @Result(property = "expirationDate", column = "expirationdate")
    })
    public PasswordResetToken getPasswordResetTokeObj(String email);


    @Update("UPDATE desp_users SET password=#{newPassword} WHERE username=#{username}")
    public int updateUserPassword(@Param("username") String username, @Param("newPassword") String newPassword);


    @Select("SELECT username FROM desp_account_Password_ResetTokens WHERE token=#{hiddenToken}")
    public String getUserNameByToken(String hiddenToken);


    @Delete("DELETE FROM desp_account_Password_ResetTokens WHERE username=#{username}")
    public int deleteTknAfterPswdUpdate(@Param("username") String username);


    @Select("SELECT COUNT(*) FROM despani_account_Password_ResetTokens WHERE username=#{username}")
    public int countIfAnyRecordForUser(@Param("username") String username);

    @Select(GET_USER_LIGHT + " WHERE desp_users.oid>0 limit #{limit} offset #{offset}")
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "username", column = "username"),
            @Result(property = "lastName", column = "lastName"),
            @Result(property = "createdOn", column = "created_on"),
            @Result(property = "updatedOn", column = "updated_on"),
            @Result(property = "roles", column = "username", javaType = List.class, many = @Many(select = "getUserRoles"))
    })
    public List<DespaniUser> getAllUsersInfo(@Param("limit") int limit, @Param("offset") int offset);


    @Select("SELECT role FROM desp_users JOIN desp_user_role ON desp_users.oid=desp_user_role.userid  JOIN desp_roles ON desp_user_role.roleid=desp_roles.oid WHERE desp_users.username=#{username} ")
    @Results(value = {
            @Result(property = "role", column = "role")
    })
    public List<DespRole> getUserRolesByUsername(@Param("username") String username);


    @Select("SELECT " +
            " desp_roles.role as role, " +
            " desp_roles.weight as weight, " +
            " desp_roles.roleDescription as roleDescription, " +
            " desp_roles.oid " +
            " FROM " +
            " desp_roles ")
    public List<DespRole> getAllRoles();


    @Select("SELECT " +
            " desp_roles.role as role  " +
            " FROM " +
            " desp_roles ")
    public List<String> getAllRolesAsStrings();


    //end of test

    @Insert(" INSERT INTO desp_users(firstname,lastname,email,username,password,created_on,updated_on,locked,enabled)  " +
            " VALUES(#{firstName},#{lastName},#{email},#{username},#{password},#{createdOn},#{updatedOn},#{locked},#{enabled})")
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    public int createUser(DespaniUser despaniuser);


    @Insert(CREATE_USER_ROLES)
    public void createUserRole(@Param("username") String username, @Param("userRole") String userRole);


    @Select(GET_USER_LIGHT + " WHERE username=#{username}")
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "username", column = "username"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "password", column = "password"),
            @Result(property = "createdOn", column = "created_on"),
            @Result(property = "updatedOn", column = "updated_on"),
            @Result(property = "roles", column = "username", javaType = List.class, many = @Many(select = "getUserRoles"))
    })
    public DespaniUser getUserByUsername(@Param("username") String username);

    @Select({"<script>",
            "SELECT oid,firstname,username,email,lastname,username,password,created_on,updated_on,enabled,locked FROM desp_users ",


            " <choose>",

            " <when test = 'isEmail == null' >",
            " WHERE username=#{username}",
            " </when>",
            " <when test = 'isEmail != null'>",
            " WHERE email=#{username}",
            " </when>",
            " </choose>",

            "</script>"

    })
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "password", column = "password"),
            @Result(property = "createdOn", column = "created_on"),
            @Result(property = "updatedOn", column = "updated_on"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "locked", column = "locked"),
            @Result(property = "roles", column = "email", javaType = List.class, many = @Many(select = "getUserRoles"))
    })
    public DespaniUser getUserByUsernameEmail(@Param("username") String email, @Param("isEmail") String emailx);


    @Select("SELECT desp_roles.oid as roleOid, role, weight, roleDescription FROM desp_users " +
            "JOIN desp_user_role " +
            "ON desp_users.oid=desp_user_role.userid  " +
            "JOIN desp_roles " +
            "ON desp_user_role.roleid=desp_roles.oid " +
            "WHERE desp_users.email=#{userName};")
    @Results(value = {
            @Result(property = "oid", column = "roleOid"),
            @Result(property = "role", column = "role"),
            @Result(property = "weight", column = "weight"),
            @Result(property = "roleDescription", column = "roleDescription")
    })
    public List<DespRole> getUserRoles(String userName);


    @Select(GET_USER_LIGHT)
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "username", column = "username"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "password", column = "password"),
            @Result(property = "createdOn", column = "created_on"),
            @Result(property = "updatedOn", column = "updated_on"),
            @Result(property = "roles", column = "username", javaType = List.class, many = @Many(select = "getUserRoles"))
    })
    public List<DespaniUser> getAllUsers();


    final String CREATE_USER_PROFILE = "INSERT INTO desp_user_profile (oid,dob,gender) VALUES(#{oid},#{dob},#{gender})";
    final String CREATE_USER_PROFILE2 = "INSERT INTO desp_user_profile (oid,dob,gender,cell) VALUES(#{oid},#{dob},#{gender},#{cell})";

    @Insert(CREATE_USER_PROFILE)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    int createProfile(@Param("oid") int oid, @Param("dob") Date dob, @Param("gender") String gender);


    @Insert(CREATE_USER_PROFILE2)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    int createProfile2(@Param("oid") int oid, @Param("dob") Date dob, @Param("gender") String gender, @Param("cell") String cell);


    @Select(GET_USER_LIGHT + " WHERE oid=#{friendOid}")
    DespaniUser getUserLight(@Param("friendOid") Integer addOid);


//    @Select("SELECT  " +
//            " desp_users.oid, " +
//            " desp_users.firstname, " +
//            " desp_friends.statusx, " +
//            " desp_users.lastname, " +
//            " desp_friends.user_2_oid " +
//            " FROM desp_friends  " +
//            " JOIN desp_users ON (desp_friends.user_2_oid=desp_users.oid and desp_friends.statusx!=#{status}) " +
//            " where  desp_friends.user_1_oid=#{fOid}  AND desp_friends.user_2_oid in (  " +
//            " SELECT desp_friends.user_2_oid FROM desp_friends WHERE  desp_friends.user_1_oid=#{oid} )")

    @Select("SELECT  " +
            " desp_users.oid, " +
            " desp_users.firstname, " +
            " desp_friends.statusx, " +
            " desp_users.lastname, " +
            " desp_friends.user_2_oid " +
            " FROM desp_friends  " +
            " JOIN desp_users ON (desp_friends.user_2_oid=desp_users.oid and desp_friends.statusx!=#{status}) " +
            " where  desp_friends.user_1_oid=#{fOid}  AND desp_friends.user_2_oid in (  " +
            " SELECT desp_friends.user_2_oid FROM desp_friends WHERE  desp_friends.user_1_oid=#{oid} )")

    public List<DespFriend> mutualFriendsByStatus(@Param("oid") Integer oid, @Param("fOid") Integer fOid, @Param("status") Integer status);


    final String GET_FRIEND_BY_OID = "SELECT  " +
            "  desp_users.oid,  " +
            "  desp_users.firstname,  " +
            "  desp_users.lastname,  " +
            "  desp_users.username,  " +
            "  desp_users.created_on,  " +
            "  desp_users.updated_on,  " +
            "  desp_friends.statusx  " +
            "FROM  " +
            "  desp_friends  " +
            " right JOIN   desp_users  " +
            "ON   desp_friends.user_2_oid = desp_users.oid  ";

    @Select(GET_FRIEND_BY_OID + " WHERE   desp_friends.user_1_oid=#{myOid} and   desp_friends.user_2_oid=#{requesterOid} ")
    @Results(value = {
            @Result(property = "status", column = "statusx", typeHandler = DespFriendStatusHandler.class),

    })
    DespFriend getFreindByOid(@Param("myOid") Integer myOid, @Param("requesterOid") Integer requesterOid);

    @Insert("INSERT INTO desp_friends (user_1_oid, user_2_oid,statusx) VALUES (#{myOid},#{friendOid},#{status,typeHandler = com.despani.core.mybatis.typehandlers.DespFriendStatusHandler})")
    void insertFriendRecord(@Param("myOid") Integer myOid, @Param("friendOid") Integer friendOid, @Param("status") DespFriendStatus status);

    @Update("UPDATE desp_friends SET statusx = #{status,typeHandler = com.despani.core.mybatis.typehandlers.DespFriendStatusHandler} WHERE desp_friends.user_1_oid=#{myOid} AND desp_friends.user_2_oid=#{requesterOid};")
    void updateFriendStatus(@Param("myOid") Integer myOid, @Param("requesterOid") Integer requesterOid, @Param("status") DespFriendStatus despFriendStatus);

    @Delete("DELETE FROM desp_friends WHERE user_1_oid=#{Oid1} AND user_2_oid=#{Oid2}")
    void deleteFriend(@Param("Oid1") Integer oid1, @Param("Oid2") Integer oid2);


    @Update("UPDATE desp_users SET firstname=#{firstName}, lastname=#{lastName}, locked=#{locked}, enabled=#{enabled} " +
            "WHERE oid=#{userOid}")
    void editUser(@Param("userOid") Integer userOid, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("locked") Boolean locked, @Param("enabled") Boolean enabled);


    @Update("UPDATE desp_users SET locked=#{lock} WHERE oid=#{userOid}")
    void lockUser(@Param("userOid") Integer userOid, @Param("lock") Boolean lock);


    @Update("UPDATE desp_users SET enabled=#{enable} WHERE oid=#{userOid}")
    void enableUser(@Param("userOid") Integer userOid, @Param("enable") Boolean enable);


    @Select(GET_USER_LIGHT + " WHERE oid=#{oid}")
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "username", column = "username"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "password", column = "password"),
            @Result(property = "createdOn", column = "created_on"),
            @Result(property = "updatedOn", column = "updated_on"),
            @Result(property = "roles", column = "username", javaType = List.class, many = @Many(select = "getUserRolesByUsername"))
    })
    DespaniUser getUserByOid(Integer oid);


    public final String CHECK_USERNAME_X = "SELECT EXISTS(SELECT * FROM desp_users WHERE ";

    @Select(CHECK_USERNAME_X + "username=#{username})")
    int usernameExists(@Param("username") String username);


}

