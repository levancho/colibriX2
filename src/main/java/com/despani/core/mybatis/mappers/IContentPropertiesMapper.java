package com.despani.core.mybatis.mappers;

import com.despani.core.utils.DespPropertyX2;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface IContentPropertiesMapper {


      static final String LOAD_CONTENT_PROPERTIES = "SELECT " +
              " desp_content_properties.`name`, " +
              " desp_content_properties.propValue, " +
              " desp_content_properties.content_oid " +
              " FROM " +
              " desp_content_properties " +
              " WHERE " +
              " desp_content_properties.content_oid=#{contentOid}";


      static final String INSERT_CONTENT_PROPERTY = "INSERT INTO `desp_content_properties` VALUES(#{name},#{propValue},#{contentOid} )";
      static final String UPDATE_CONTENT_PROPERTY = "UPDATE desp_content_properties SET  propValue=#{propValue}  WHERE content_oid=#{contentOid} AND `name`=#{name}";
      static final String DELETE_CONTENT_PROPERTY = "DELETE FROM desp_content_properties WHERE `name` = #{name} and  content_oid=#{contentOid}";
      static final String DELETE_ALL_CONTENT_PROPERTIES = "DELETE FROM desp_content_properties WHERE content_oid=#{contentOid}";

      static final String LOAD_CAT_TYPE_PROPERTIES_BY_CAT_TYPE = "SELECT oid, `prop_name` as name, `prop_value` as propValue, `prop_type` as type, `ref_type_oid` , cat_type_oid FROM desp_category_type_properties where cat_type_oid=${catTypeOid} " ;
      static final String INSERT_CAT_TYPE_PROPERTY = "INSERT INTO `desp_category_type_properties` ( `prop_name`, `prop_value`, `prop_type`, `ref_type_oid`,`cat_type_oid`) " +
              "VALUES (#{name}, #{propValue},#{type}, #{refTypeOid},#{catTypeOid})";
      static final String UPDATE_CAT_TYPE_PROPERTY_BY_PROP_NAME = "UPDATE desp_category_type_properties SET prop_value=#{propValue}, prop_type=#{type}, ref_type_oid=#{refTypeOid}, cat_type_oid=#{catTypeOid}  where prop_name=#{name}";
      static final String DELETE_CAT_TYPE_PROPERTY_BY_PROP_NAME = "DELETE FROM desp_category_type_properties WHERE where prop_name=#{propName}";



      @Select(LOAD_CONTENT_PROPERTIES)
      public List<DespPropertyX2> loadContentProperties(@Param("contentOid")Integer contentOid) ;

      @Select(LOAD_CONTENT_PROPERTIES)
      @MapKey("name")
      public Map<String,DespPropertyX2> loadContentPropertiesAsMap(@Param("contentOid")Integer contentOid) ;




      @Insert(INSERT_CONTENT_PROPERTY)
      public void insertContentProperty(@Param("name")String name,@Param("propValue")String propValue,@Param("contentOid")Integer contentOid);

      @Update(UPDATE_CONTENT_PROPERTY)
      public void updateContentProperty(@Param("name")String name,@Param("propValue")String propValue,@Param("contentOid")Integer contentOid);

      @Delete(DELETE_CONTENT_PROPERTY)
      public void deleteContentProperty(@Param("name") String name,@Param("contentOid")Integer contentOid);

      @Delete(DELETE_ALL_CONTENT_PROPERTIES)
      public void deleteAllContentProperties(@Param("contentOid")Integer contentOid);

      @Select(LOAD_CAT_TYPE_PROPERTIES_BY_CAT_TYPE)
      public List<DespPropertyX2> loadCatTypeProperties(@Param("catTypeOid") Integer catTypeOid) ;





}
