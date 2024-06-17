package com.despani.core.mybatis.mappers;

import com.despani.core.beans.domains.DespMedia;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IDespMediaProductMapper {

    @Insert("INSERT INTO `despani3`.`desp_media_product`(`media_oid`, `product_oid`) VALUES (#{mediaOid}, #{productOid});" )
    void insertDespMediaProduct(Integer mediaOid, Integer productOid);

    @Update("UPDATE `despani3`.`desp_media_product` SET `media_oid` = #{mediaOid},`product_oid` = #{productOid} WHERE `oid` = #{oid};")
    void updateDespMediaProduct(Integer oid, Integer mediaOid, Integer productOid);



    @Delete("DELETE FROM desp_media_product WHERE oid = #{oid};")
    void deleteDespMediaProductByOid(Integer oid);



}
