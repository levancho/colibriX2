package com.despani.x2.company.mybatis.mappers;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IDespMediaCompanyMapper {



    @Insert("INSERT INTO `despani3`.`desp_media_company`(`media_oid`, `company_oid`) VALUES (#{mediaOid}, #{companyOid});" )
    void insertDespMediaCompany(Integer mediaOid, Integer companyOid);

    @Update("UPDATE `despani3`.`desp_media_company` SET `media_oid` = #{mediaOid},`company_oid` = #{companyOid} WHERE `oid` = #{oid};")
    void updateDespMediaCompany(Integer oid, Integer mediaOid, Integer productOid);

    @Delete("DELETE FROM desp_media_company WHERE oid = #{oid};")
    void deleteDespMediaCompanyByOid(Integer oid);





}
