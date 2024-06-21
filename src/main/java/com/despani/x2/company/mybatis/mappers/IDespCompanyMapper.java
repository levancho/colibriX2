package com.despani.x2.company.mybatis.mappers;


import com.despani.x2.company.beans.domains.DespCompany;
import com.despani.x2.core.xusers.beans.domains.DespAddress;
import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IDespCompanyMapper {

    final String INSERT_DESPCOMPANY = "INSERT INTO desp_company (name, address_oid, user_oid, type, description, published)" +
                                " VALUES (#{name}, #{address.oid} , #{representative.oid}, #{type}, #{description}, #{published})";

    final String DELETE_DESPCOMPANY = "DELETE FROM desp_company WHERE oid= #{name}";

    final String UPDATE_DESPCOMPANY = "UPDATE desp_company " +
            " SET name = #{name}, " +
            " address_oid = #{address.oid}, " +
            " user_oid =#{representative.oid}, " +
            " type = #{type}, " +
            " published = #{published}, " +
            " WHERE oid = #{oid} ";

    final String GET_DESPCOMPANY_BY_OID = "SELECT * FROM desp_company where desp_company.oid = #{oid}";

    final String GET_DESPCOMPANY = "SELECT" +
            " desp_company.oid, " +
            " desp_company.name, " +
            " desp_company.type, " +
            " desp_company.address_oid, " +
            " desp_company.user_oid, " +
            " desp_address.name AS address_name, " +
            " desp_address.address AS address_address, " +
            " desp_address.city AS address_city, " +
            " desp_address.state AS address_state, " +
            " desp_address.zipCode AS address_zipCode, " +
            " desp_users.firstname AS user_firstname, " +
            " desp_users.email AS user_email, " +
            " desp_users.username AS user_username, " +
            " desp_users.lastname AS user_lastname, " +
            " desp_users.`password` AS user_password, " +
            " desp_users.created_on AS user_created_od, " +
            " desp_users.updated_on AS user_update_on, " +
            " desp_users.locked AS user_locked, " +
            " desp_users.enabled AS user_enabled" +
            " FROM" +
            " desp_company" +
            " LEFT JOIN" +
            " desp_users" +
            " ON " +
            " desp_company.user_oid = desp_users.oid" +
            " LEFT JOIN" +
            " desp_address" +
            " ON " +
            " desp_company.address_oid = desp_address.oid " ;


    @Insert(INSERT_DESPCOMPANY)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    void createDespCompany(DespCompany dc);

    @Delete(DELETE_DESPCOMPANY)
    public void deleteDespCompany(@Param("oid") int oid);

    @Select(GET_DESPCOMPANY_BY_OID)
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "title", column = "title"),
            @Result(property = "representative", column = "user_oid",
                    javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid")),
            @Result(property = "address", column = "address_oid",
                    javaType = DespAddress.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespAddressMapper.getDespAddressByOid")),
            @Result(property = "media", column = "oid",
                    javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByCompanyOid")),
    })
    DespCompany getDespCompanyByOid(@Param("oid") Integer oid);

    @Select(GET_DESPCOMPANY + "where" + " desp_company.name = #{name}" )
    @Results(value = {
            @Result(property = "representative", column = "user_oid",
                    javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid")),
            @Result(property = "address", column = "address_oid",
                    javaType = DespAddress.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespAddressMapper.getDespAddressByOid")),
            @Result(property = "media", column = "oid",
                    javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByCompanyOid")),
    })
    DespCompany getDespCompanyByName(String name);

    @Select(GET_DESPCOMPANY + "where" + " desp_company.type = #{type}" )
    @Results(value = {
            @Result(property = "oid", column="oid"),
            @Result(property = "representative", column = "user_oid",
                    javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid")),
            @Result(property = "address", column = "address_oid",
                    javaType = DespAddress.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespAddressMapper.getDespAddressByOid")),
            @Result(property = "media", column = "oid",
                    javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByCompanyOid")),
    })
    List<DespCompany> getDespCompanyByType(String type);

    @Select(GET_DESPCOMPANY)
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "representative", column = "user_oid",
                    javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid")),
            @Result(property = "address", column = "address_oid",
                    javaType = DespAddress.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespAddressMapper.getDespAddressByOid")),
            @Result(property = "media", column = "oid",
                    javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByCompanyOid")),
    })
    List<DespCompany> getALLDespCompanys();

    @Select(GET_DESPCOMPANY)
    @Results(value = {
            @Result(property = "oid", column="oid"),
            @Result(property = "representative", column = "user_oid",
                    javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid")),
            @Result(property = "address", column = "address_oid",
                    javaType = DespAddress.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespAddressMapper.getDespAddressByOid")),
            @Result(property = "media", column = "oid",
                    javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByCompanyOid")),
    })
    List<DespCompany> getAllDespCompanyAsList();

    @Update(UPDATE_DESPCOMPANY)
    void updateDespCompany(DespCompany despCompany);

}
