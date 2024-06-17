package com.despani.core.mybatis.mappers;

import com.despani.core.utils.DespPropertyX2;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface IPropertiesMapper {

      static final String LOAD_PROPERTIES = "SELECT name, propValue, encrypted, iv, xtype,xsection FROM desp_properties";
      static final String INSERT_PROPERTY = "INSERT INTO desp_properties(`name`, propValue, encrypted, iv, xtype,xsection) VALUES(#{name},#{propValue},#{encrypted},#{iv}, #{xtype, typeHandler = com.despani.core.mybatis.typehandlers.DespPropertyTypeHandler},#{xsection})";
      static final String UPDATE_PROPERTY = "UPDATE desp_properties SET propValue=#{propValue}, encrypted=#{encrypted}, iv=#{iv} WHERE `name`=#{name}";
      static final String DELETE_PROPERTY = "DELETE FROM desp_properties WHERE `name` LIKE #{name}";

      @Select(LOAD_PROPERTIES)
      @Results(value = {
              @Result(property = "name", column = "name"),
              @Result(property = "propValue", column = "propValue"),
              @Result(property = "encrypted", column = "encrypted"),
              @Result(property = "iv", column = "iv"),
              @Result(property = "xtype", column = "xtype", typeHandler = com.despani.core.mybatis.typehandlers.DespPropertyTypeHandler.class),
              @Result(property = "xsection", column = "xsection")
      })
      public List<DespPropertyX2> loadProperties() ;
      
      @Insert(INSERT_PROPERTY)
      public void insertProperty(DespPropertyX2 prop);

      @Update(UPDATE_PROPERTY)
      public void updateProperty(DespPropertyX2 prop);

      @Delete(DELETE_PROPERTY)
      public void deleteProperty(@Param("name") String name);

}
