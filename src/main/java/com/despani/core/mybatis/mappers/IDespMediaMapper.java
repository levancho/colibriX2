package com.despani.core.mybatis.mappers;


import com.despani.core.beans.domains.DespMedia;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IDespMediaMapper {

    final String SELECT_DESPMEDIA_BY_PRODUCT_OID = "SELECT " +
            " desp_media.oid, " +
            " desp_media.description, " +
            " desp_media.filename,  " +
            " desp_media.`primary`, " +
            " desp_media.url, " +
            " desp_media.format, " +
            " desp_media.type " +
//            " desp_media_product.product_oid AS product_oid " +
            " FROM desp_media " +
            " LEFT JOIN desp_media_product  ON desp_media.oid = desp_media_product.media_oid " +
            " LEFT JOIN desp_product ON desp_media_product.product_oid = desp_product.oid " +
            " WHERE desp_media_product.product_oid = #{oid}";


    @Insert("INSERT INTO `despani3`.`desp_media` ( `filename`, `description`, `type`, `primary`, `url`, `format` )\n" +
            "VALUES\n" +
            "\t( #{filename}, #{description}, #{type}, #{primary}, #{url}, #{format} );")
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    void createDespMedia(DespMedia despMedia);

    @Select("SELECT * FROM desp_media where oid = #{oid}")
    DespMedia getDespMediaByOid(@Param("oid") Integer oid);

    @Select("SELECT * FROM desp_media WHERE desp_media.filename = #{filename} ")
    DespMedia getMediaByFilename(@Param("filename") String filename);

    @Select("SELECT * FROM desp_media WHERE desp_media.type = #{type}")
    List<DespMedia> getMediaByType(@Param("type") Integer type);

    @Select("SELECT *  FROM desp_media WHERE desp_media.primary = #{primary}")
    List<DespMedia> getDespMediaByPrimary(@Param("primary") Integer primary);

    @Select("SELECT * FROM desp_media WHERE desp_media.url = #{url}")
    DespMedia getMediaByUrl(@Param("url") String url);

    @Select("SELECT * FROM desp_media  WHERE desp_media.format = #{format}")
    List<DespMedia> getMediaByFormat(@Param("format") String format);


    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\tdesp_media\n" +
            "\tJOIN desp_media_company ON desp_media.oid = desp_media_company.media_oid \n" +
            "WHERE\n" +
            "\tcompany_oid = #{oid}")
    List<DespMedia> getDespMediaByCompanyOid(@Param("oid") Integer oid);


    @Select(SELECT_DESPMEDIA_BY_PRODUCT_OID)
    List<DespMedia> getDespMediaByProductOid(Integer oid);


    @Select("SELECT * FROM desp_media; ")
    List<DespMedia> getAllDespMedia();

    @Update("UPDATE `despani3`.`desp_media` \n" +
            "SET `filename` = #{filename},\n" +
            "`description` = #{description},\n" +
            "`type` = #{type},\n" +
            "`primary` = #{primary},\n" +
            "`url` = #{url},\n" +
            "`format` = #{format} \n" +
            "WHERE\n" +
            "\t`oid` = #{oid};")
    void updateDespMedia(DespMedia despMedia);


    @Update("UPDATE `despani3`.`desp_media` \n" +
            "INNER JOIN desp_media_product ON desp_media.oid = desp_media_product.media_oid \n"+
            "SET desp_media.primary = 0 \n"+
            "WHERE desp_media_product.product_oid =#{productOid};"
    )
    void resetDespMediaPrimary(@Param("productOid") Integer productOid);

    @Update("UPDATE `despani3`.`desp_media` \n" +
            "SET `primary` = #{primary}\n" +
            "WHERE\n" +
            "\t oid = #{mediaOid};")
    void makeDespMediaPrimary(@Param("mediaOid") Integer mediaOid, @Param("primary") Boolean primary);

    @Delete("delete from desp_media WHERE desp_media.oid=#{oid}")
    void deleteDespMediaByOid(@Param("oid") Integer oid);


}







