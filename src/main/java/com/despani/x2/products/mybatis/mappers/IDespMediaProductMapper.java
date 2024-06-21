package com.despani.x2.products.mybatis.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IDespMediaProductMapper {

    @Insert("INSERT INTO `despani3`.`desp_media_product`(`media_oid`, `product_oid`) VALUES (#{mediaOid}, #{productOid});" )
    public void insertDespMediaProduct(Integer mediaOid, Integer productOid);

    @Update("UPDATE `despani3`.`desp_media_product` SET `media_oid` = #{mediaOid},`product_oid` = #{productOid} WHERE `oid` = #{oid};")
    public void updateDespMediaProduct(Integer oid, Integer mediaOid, Integer productOid);



    @Delete("DELETE FROM desp_media_product WHERE oid = #{oid};")
    public void deleteDespMediaProductByOid(Integer oid);



}
