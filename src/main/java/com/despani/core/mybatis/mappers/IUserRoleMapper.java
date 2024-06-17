package com.despani.core.mybatis.mappers;


import com.despani.core.beans.domains.DespRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IUserRoleMapper {

    final String GET_ALL_ROLES = "SELECT oid, role, weight,external,roleDescription from desp_roles";


    @Select(GET_ALL_ROLES)
    List<DespRole> getAllRoles();

    @Update("UPDATE desp_roles "+
            "SET role=#{role}, weight=#{weight}, external=#{external}, roleDescription=#{roleDesc} "+
            "WHERE oid=#{oid}")
    int updateRole(@Param("oid")int oid, @Param("role")String role, @Param("weight")int weight,@Param("external")boolean external,@Param("roleDesc") String roleDesc);


    @Insert("INSERT INTO desp_roles(role,weight,external,roleDescription) "+
            "VALUES (#{role},#{weight},#{external},#{roleDescription});")
    int createRole(@Param("role") String role,@Param("weight") int weight,@Param("roleDescription") String roleDescription);



    final String READ_USER_ROLE = "SELECT role,weight,roleDescription, external FROM desp_roles WHERE oid=#{userId}";


    @Select(READ_USER_ROLE)
    public DespRole readUserRole(@Param("userId") int oid);


    @Delete(" DELETE FROM  desp_user_role " +
            "    WHERE 1=1 " +
            "    AND userId=#{userId} " +
            "    AND roleId=#{roleId} ")
    int deleteRole(long userId, long roleId);

    @Delete(" DELETE FROM  desp_user_role " + " WHERE userid=#{userId} " )
    int deleteUserAllRoles(long userId);

    @Insert("INSERT INTO  desp_user_role (userId, roleId) " +
            "    VALUES (#{userId, jdbcType=BIGINT}, #{roleId, jdbcType=BIGINT});")
    int addRole(long userId, long roleId);

}
