package com.despani.x2.core.xmodules.mybatis.mappers;

import com.despani.x2.core.xmodules.beans.domains.DespModule;
import com.despani.x2.core.xmodules.beans.form.ModulePositionForm;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface IModulesMapper {


      static final String LOAD_MODULES = "SELECT `oid`, `title`, internalTitle, `content`, `ordering`, `position`, `published`, `module`, `showtitle`  FROM desp_modules";

      static final String INSERT_MODULE ="INSERT INTO desp_modules \n" +
              "(title,\n" +
              "internalTitle," +
              "`content`,\n" +
              "`ordering`,\n" +
              "`position`,\n" +
              "`published`,\n" +
              "`module`,\n" +
              "`showtitle`)\n" +
              "VALUES\n" +
              "\t( #{title}, #{internalTitle}, #{content}, #{ordering}, #{position}, #{published}, #{module}, #{showTitle});";

      static final String UPDATE_MODULE = "UPDATE desp_modules SET"+
              " `title`= #{title}, " +
              " internalTitle = #{internalTitle}," +
              " `content`= #{content}," +
              " `ordering` = #{ordering}, " +
              " `position` = #{position}, " +
              " `published` = #{published}," +
              " `module` = #{module}," +
              " `showtitle` = #{showTitle}" ;

      static final String POSITION_CLOUSE = " ordering=#{order}, position=#{position} ";

      static final String DELETE_MODULE = "DELETE FROM desp_modules ";

      final String UPDATE_SELECTED_MODULE = "UPDATE desp_modules ";

      final String OID_CLAUS=" oid=#{oid}";

      final String PUBLISHED_CLAUS=" published=#{published} ";

      final String SHOW_TITLE_CLAUS=" showtitle=#{showTitle} ";

      static final String MODULE_MAX_ORDERING_BY_POSITION = "SELECT MAX( ordering ) FROM despani3.desp_modules" +
              " WHERE position = #{position} ;";

      @Select(LOAD_MODULES)
      public List<DespModule> loadModules();

      @Select(LOAD_MODULES + " where " + OID_CLAUS)
      public DespModule getModuleByOid(Integer oid);

      @Select(LOAD_MODULES)
      @MapKey("position")
      public Map<String, List<DespModule>> loadModulesMap();

      @Select(MODULE_MAX_ORDERING_BY_POSITION)
      public Integer getModuleMaxOrderingByPosition(@Param("position") String position);


      
      @Insert(INSERT_MODULE)
      @Options(useGeneratedKeys = true, keyProperty = "oid")
      public void insertMODULE(DespModule prop);

      @Update(UPDATE_MODULE + " where " + OID_CLAUS)
      public void updateMODULE(DespModule prop);

      @Update(UPDATE_SELECTED_MODULE + " set " + PUBLISHED_CLAUS + " where " + OID_CLAUS )
      public void publishModule(@Param("oid") Integer oid, @Param("published") boolean published);

      @Update(UPDATE_SELECTED_MODULE + " set " + SHOW_TITLE_CLAUS + " where " + OID_CLAUS)
      public void showTitle(@Param("oid") Integer oid, @Param("showTitle") boolean showtitle);

      @Update(UPDATE_SELECTED_MODULE + " set " + POSITION_CLOUSE + " where " + OID_CLAUS)
      public void updateModulePosition(ModulePositionForm position);

      @Delete(DELETE_MODULE + " where " + OID_CLAUS)
      public void deleteModule(@Param("oid") Integer oid);


}
