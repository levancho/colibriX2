package com.despani.core.mybatis.mappers;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IDespMediaCompanyMapper {


    @Insert("INSERT INTO `despani3`.`desp_media_company`(`media_oid`, `product_oid`) VALUES (#{mediaOid}, #{productOid});" )
    void insertDespMediaProduct(Integer mediaOid, Integer companyOid);


    @Update("UPDATE `despani3`.`desp_media_company` SET `media_oid` = #{mediaOid},`product_oid` = #{productOid} WHERE `oid` = #{oid};")
    void updateDespMediaProduct(Integer mediaOid, Integer companyOid);

    @Delete("DELETE FROM desp_media_company WHERE oid = #{oid};")
    void deleteDespMediaProductByOid(Integer oid);





}
