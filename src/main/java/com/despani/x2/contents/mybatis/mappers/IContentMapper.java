package com.despani.x2.contents.mybatis.mappers;

import com.despani.x2.contents.beans.domains.DespContent;
import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IContentMapper {

    String INSERT_CONTENT = " INSERT INTO `desp_content` (" +
            "`catoid`  , " +
            "`introtext` , " +
            "`fulltext`, " +
            "`featured`,   " +
            "`title`,  " +
            "`alias`,   " +
            "`created`,    " +
            "`created_by`,   " +
            "`modified`,    " +
            "`modified_by`, " +
            "`published`,  " +
            "`access`, " +
            "`hits`,  " +
            "`xlang`, " +
            "`xlegacy`) " +
            "VALUES (" +
            "#{catoid} , " +
            "#{introtext} , " +
            "#{fulltext}, " +
            "#{featured}, " +
            "#{title}, " +
            "#{alias}, " +
            "#{createdOn}, " +
            "#{createdByOid}, " +
            "#{updatedOn}, " +
            "#{updatedByOid}, " +
            "#{published}, " +
            "#{access}, " +
            "#{hits}, " +
            "#{xlang}, " +
            "#{legacy}) ";

    String LIMIT = " LIMIT 0, 1000";

    String SELECT_CONTENT = "SELECT " +
            " desp_content.oid, " +
            " desp_content.title, " +
            " desp_content.alias, " +
            " desp_content.introtext, " +
            " desp_content.`fulltext`, " +
            " desp_content.catoid, " +
            " desp_content.created, " +
            " desp_content.created_by, " +
            " desp_content.modified, " +
            " desp_content.modified_by, " +
            " desp_content.hits, " +
            " desp_content.access, " +
            " desp_content.featured, " +
            " desp_content.xlang " +
            " FROM " +
            " desp_content ";


    String SELECT_CONTENT_WITH_CATEGORY = "SELECT  " +
            " desp_content.oid, " +
            " desp_content.title, " +
            " desp_content.alias, " +
            " desp_content.introtext, " +
            " desp_content.`fulltext`, " +
            " desp_content.catoid, " +
            " desp_content.created, " +
            " desp_content.created_by, " +
            " desp_content.modified, " +
            " desp_content.modified_by, " +
            " desp_content.hits, " +
            " desp_content.access, " +
            " desp_content.published, " +
            " desp_content.featured, " +
            " desp_content.xlang, " +
            " desp_categories_details.oid AS catoid,  " +
            " desp_categories_details.cat_type_oid AS categoryTypeOid,  " +
            " desp_categories_details.oid AS cata_oid,  " +
            " desp_categories_details.title AS cat_title " +
            " FROM " +
            " desp_content " +
            "   left JOIN  desp_categories_details " +
            " ON desp_content.catoid  = desp_categories_details.oid  ";


    @Select(SELECT_CONTENT_WITH_CATEGORY + LIMIT)
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "updatedByOid", column = "modified_by")

    })
    List<DespContent> getAllContent();


    String SELECT_CONTENT_FULL_COMPLETE = "SELECT  " +
            " desp_content.oid,  " +
            " desp_content.title,  " +
            " desp_content.alias,  " +
            " desp_content.introtext,  " +
            " desp_content.`fulltext`,  " +
            " desp_content.catoid,  " +
            " desp_content.created,  " +
            " desp_content.created_by,  " +
            " desp_content.modified,  " +
            " desp_content.modified_by,  " +
            " desp_content.access,  " +
            " desp_content.hits,  " +
            " desp_content.featured,  " +
            " desp_content.xlang ,  " +
            " desp_content.published ,  " +
            " desp_categories_details.oid AS cata_oid,  " +
            " desp_categories_details.oid AS catoid,  " +
            " desp_categories_details.cat_type_oid AS categoryTypeOid,  " +
            " desp_categories_details.title AS cat_title,  " +
            " desp_categories_details.alias AS cat_alias,  " +
            " desp_categories_details.description as cat_desc,  " +
            " desp_categories_details.published as cat_published,  " +
            " desp_categories_details.access AS cat_access,  " +
            " desp_categories_details.created_user_id as cat_created_by,  " +
            " desp_categories_details.created_time  as cat_created_on,  " +
            " desp_categories_details.modified_user_id as cat_modified_by,  " +
            " desp_categories_details.modified_time as cat_modifed_on ,  " +
            " desp_categories_details.hits AS cat_hits,  " +
            " desp_categories_details.xlang as cat_xlang,  " +
            " desp_category_type.oid as cat_type_oid,  " +
            " desp_category_type.type cat_type_type,  " +
            " desp_category_type.description AS cat_type_desc  " +
            " FROM " +
            " desp_content " +
            " left JOIN  desp_categories_details " +
            " ON desp_content.catoid  = desp_categories_details.oid  " +
            " left JOIN desp_category_type " +
            " ON desp_categories_details.cat_type_oid = desp_category_type.oid ";


    @Select(SELECT_CONTENT_FULL_COMPLETE + " where desp_content.oid=#{oid} ")
    @Results(value = {
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "updatedByOid", column = "modified_by"),
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "category.alias", column = "cat_alias"),
            @Result(property = "category.desc", column = "cat_desc"),
            @Result(property = "category.published", column = "cat_published"),
            @Result(property = "category.access", column = "cat_access"),
            @Result(property = "category.createdByOid", column = "cat_created_by"),
            @Result(property = "category.createdOn", column = "cat_created_on"),
            @Result(property = "category.updatedByOid", column = "cat_modified_by"),
            @Result(property = "category.updatedOn", column = "cat_modifed_on"),
            @Result(property = "category.hits", column = "cat_hits"),
            @Result(property = "category.xlang", column = "cat_xlang"),
            @Result(property = "category.type.oid", column = "cat_type_oid"),
            @Result(property = "category.type.type", column = "cat_type_type"),
            @Result(property = "category.type.typeDescription", column = "cat_type_desc"),
            @Result(property = "properties", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties")),
            @Result(property = "owner", column = "created_by", javaType = DespaniUser.class, one = @One(select = "com.despani.core.mybatis.mappers.IUserMapper.getUserByOid"))
    })
    DespContent getContentByOid(Integer oid);

    @Update("UPDATE desp_content " +
            " SET " +
            " title = #{title}, " +
            " alias = #{alias}, " +
            " catoid = #{catoid}, " +
            " introtext = #{introtext}, " +
            " modified = #{updatedOn}, " +
            " modified_by = #{updatedByOid}, " +
            " featured = #{featured}, " +
            " `fulltext` = #{fulltext}, " +
            " published = #{published}" +
            " WHERE oid=#{oid} ")
    void saveContent(DespContent dc);

    @Select("SELECT COUNT(oid) AS NumberOfContentRows FROM desp_content")
    public int countContent();

    @Select(SELECT_CONTENT_WITH_CATEGORY + " WHERE desp_content.oid>0 limit #{limit} offset #{offset}")
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "updatedByOid", column = "modified_by")

    })
    List<DespContent> getAllContentLimited(@Param("limit") int limit, @Param("offset") int offset);

    @Select(SELECT_CONTENT_WITH_CATEGORY + " WHERE desp_content.oid>0  and desp_content.published=#{published} and desp_categories_details.published=#{published} limit #{limit} offset #{offset}")
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "updatedByOid", column = "modified_by"),
            @Result(property = "properties", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties"))

    })
    List<DespContent> getContentLimitedPublished(@Param("limit") int limit, @Param("offset") int offset, @Param("published") boolean published);

    @Select(SELECT_CONTENT_WITH_CATEGORY + " WHERE desp_content.oid>0   limit #{limit} offset #{offset}")
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "updatedByOid", column = "modified_by"),
            @Result(property = "properties", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties"))
//            @Result(property = "propertiesMap", column = "oid", javaType = Map.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentPropertiesAsMap"))

    })
    List<DespContent> getContentLimited(@Param("limit") int limit, @Param("offset") int offset);

    @Insert(INSERT_CONTENT)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    void createContent(DespContent dc);


    @Update("UPDATE desp_content SET published=#{publish} WHERE oid=#{userOid}")
    void publishContent(Integer userOid, Boolean publish);

    @Update("UPDATE desp_content SET featured=#{featured} WHERE oid=#{userOid}")
    void fetaureContent(Integer userOid, Boolean featured);

    @Select(SELECT_CONTENT_WITH_CATEGORY + " WHERE desp_content.oid>0   and desp_content.featured=1  limit #{limit} offset #{offset}")
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
            @Result(property = "updatedByOid", column = "modified_by"),
            @Result(property = "properties", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties"))

//            @Result(property = "propertiesMap", column = "oid", javaType = Map.class, one = @One(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentPropertiesAsMap"))
    })
    List<DespContent> getfeaturedContent(int limit, int offset);


    @Select(SELECT_CONTENT_WITH_CATEGORY + " WHERE desp_content.oid>0  and desp_content.published=#{published} and desp_categories_details.published=#{published} and desp_content.featured=1  limit #{limit} offset #{offset}")
    @Results(value = {
//            @Result(property = "category", column = "catoid"  ),
            @Result(property = "createdOn", column = "created"),
            @Result(property = "oid", column = "oid"),
            @Result(property = "createdByOid", column = "created_by"),
            @Result(property = "updatedOn", column = "modified"),
            @Result(property = "category.oid", column = "cata_oid"),
            @Result(property = "category.title", column = "cat_title"),
//            @Result(property = "updatedByOid", column = "modified_by"),
            @Result(property = "properties", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties"))

//            @Result(property = "propertiesMap", column = "oid", javaType = Map.class, one = @One(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentPropertiesAsMap"))
    })
    List<DespContent> getfeaturedContentPublished(int limit, int offset, @Param("published") boolean published);

    @Update("UPDATE desp_content SET access=#{access} WHERE oid=#{userOid}")
    void updateContentAccessLevel(Integer userOid, Integer access);


}