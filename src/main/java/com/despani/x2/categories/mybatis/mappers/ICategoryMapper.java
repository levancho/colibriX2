package com.despani.x2.categories.mybatis.mappers;

import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.core.beans.DespNodeType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface ICategoryMapper {

    @Select(" SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title," +
            " desp_category_type.type " +
            " FROM" +
            " desp_categories_details" +
            " JOIN desp_categories" +
            " ON desp_categories_details.oid = desp_categories.oid" +
            " JOIN  desp_category_type " +
            " ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            " WHERE" +
            " desp_categories.parent_oid>#{parentoid}")
    List<DespCategoryItem> getCatNodes(@Param("parentoid") Integer parentoid);


    @Select(" SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title," +
            " desp_category_type.type " +
            " FROM" +
            " desp_categories_details" +
            " JOIN desp_categories" +
            " ON desp_categories_details.oid = desp_categories.oid" +
            " JOIN  desp_category_type " +
            " ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            " WHERE" +
            " desp_categories.parent_oid=#{parentoid}")
    List<DespCategoryItem> getCatNodesDirectChildren(@Param("parentoid") Integer parentoid);

    @Select(" SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title," +
            " desp_category_type.type " +
            " FROM" +
            " desp_categories_details" +
            " JOIN desp_categories" +
            " ON desp_categories_details.oid = desp_categories.oid" +
            " JOIN  desp_category_type " +
            " ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            " WHERE" +
            " desp_categories.lft>lft and desp_categories.rgt<rgt")
    List<DespCategoryItem> getCatNodesAllChildren(@Param("lft") Integer lft,@Param("rgt") Integer rgt);



    @Select(" SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title," +
            " desp_category_type.type " +
            " FROM" +
            " desp_categories_details" +
            " JOIN desp_categories" +
            " ON desp_categories_details.oid = desp_categories.oid" +
            " JOIN  desp_category_type " +
            " ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            " WHERE" +
            "  desp_categories.lft>(select lft from  desp_categories where oid=#{parentoid}) and desp_categories.rgt<(select rgt from  desp_categories where oid=#{parentoid}) ")
    List<DespCategoryItem> getCatNodesAllChildrenByOid(@Param("parentoid") Integer parentoid);

    @Select(" SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title," +
            " desp_category_type.type " +
            " FROM" +
            " desp_categories_details" +
            " JOIN desp_categories" +
            " ON desp_categories_details.oid = desp_categories.oid" +
            " JOIN  desp_category_type " +
            " ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            " WHERE" +
            " desp_category_type.oid = #{typeoid}")
    List<DespCategoryItem> getCatNodesByTypes(@Param("typeoid") Integer typeoid);


    @Select("    SELECT n.oid, " +
            "      CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),  " +
            "      (SELECT title FROM desp_categories_details WHERE oid = n.oid AND xlang = #{plang})) AS title " +
            "    FROM desp_categories AS n, desp_categories AS p " +
            "    WHERE (n.lft BETWEEN p.lft AND p.rgt) " +
            "    GROUP BY oid " +
            "    ORDER BY n.lft;  ")
    List<DespCategoryItem> getTree(@Param("plang") String plang);


    @Select("    SELECT n.oid, " +
            "      CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),  " +
            "      (SELECT title FROM desp_categories_details WHERE oid = n.oid AND xlang = #{plang})) AS title " +
            "    FROM desp_categories AS n, desp_categories AS p " +
            "    WHERE (n.lft BETWEEN p.lft AND p.rgt)  AND n.oid != #{pedited}" +
            "    GROUP BY oid " +
            "    ORDER BY n.lft;  ")
    List<DespCategoryItem> getTree2(@Param("plang") Integer pedited, @Param("plang") String plang);

    @Select(value = "{ CALL  createCategory (  " +
            "#{pparent_id, mode=IN, jdbcType=INTEGER}," +
            "#{title_in, mode=IN, jdbcType=VARCHAR}," +
            "#{cattype_in,mode=IN,jdbcType=INTEGER}," +
            "#{alias_in, mode=IN, jdbcType=VARCHAR}," +
            "#{desc_in, mode=IN, jdbcType=LONGVARCHAR}," +
            "#{created_by_in, mode=IN, jdbcType=INTEGER}," +
            "#{newID,mode=OUT, jdbcType=INTEGER}" +
            ")}")
    @Options(statementType = StatementType.CALLABLE)
    public void createCategoryFull(Map<String, Object> m);


    @Select(value = "{ CALL  deleteCategory (#{pid, mode=IN, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    public void deleteCategory(Map<String, Object> m);


    @Select(value = "{ CALL  sp_traverse_categories (#{typex, mode=IN, jdbcType=VARCHAR},#{pid, mode=IN, jdbcType=INTEGER},#{parentId, mode=IN, jdbcType=INTEGER})}")
    String traverseCategory(Map m);


    @Select("SELECT " +
            " desp_categories_details.cat_type_oid AS typeOid, " +
            " desp_categories.oid," +
            " desp_category_type.type " +
            "FROM " +
            " desp_categories_details " +
            "JOIN  desp_categories " +
            "ON  desp_categories_details.oid =  desp_categories.oid " +
            "JOIN  desp_category_type " +
            "ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            "WHERE " +
            " desp_categories.oid=#{parentOid}  ")
    @Results(value = {
            @Result(property = "type.oid", column = "typeOid"),
            @Result(property = "type.type", column = "type")

    })
    DespCategoryItem getNodeByItemOid(int parentOid);


    @Select("SELECT " +
            " desp_categories_details.cat_type_oid AS typeOid, " +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_category_type.type " +
            "FROM " +
            " desp_categories_details " +
            "JOIN  desp_categories " +
            "ON  desp_categories_details.oid =  desp_categories.oid " +
            "JOIN  desp_category_type " +
            "ON  desp_categories_details.cat_type_oid =  desp_category_type.oid " +
            "WHERE " +
            " desp_categories.oid=#{parentOid}  ")
    @Results(value = {
            @Result(property = "type.oid", column = "typeOid"),
            @Result(property = "type.type", column = "type")

    })
    DespCategoryItem getNodeByItemOidFull(int parentOid);


    @Select("SELECT " +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid" +
            " FROM " +
            " desp_categories " +
            " WHERE " +
            " desp_categories.oid=#{parentOid}  ")
    @Results(value = {
            @Result(property = "type.oid", column = "typeOid"),
            @Result(property = "type.type", column = "type")

    })
    DespCategoryItem getNodeByItemOidLight(int parentOid);


    @Select("SELECT DISTINCT cattype from desp_categories_details WHERE oid>1;")
    List<String> _getAllCategoryTypes();


    @Select("SELECT oid, description as typeDescription,   type from desp_category_type")
    List<DespNodeType> getAllCategoryTypes();


    @Select("SELECT" +
            " desp_categories.oid," +
            " desp_categories.lft," +
            " desp_categories.rgt," +
            " desp_categories.parent_oid," +
            " desp_categories_details.title " +
            " FROM " +
            " desp_categories_details " +
            " JOIN desp_categories " +
            "  on desp_categories_details.oid = desp_categories.oid where parent_oid>#{parentoid}  AND desp_categories_details.cattype=#{catType}")
    List<DespCategoryItem> getTypeCategoryNodes(@Param("parentoid") Integer parentoid, @Param("catType") String catType);



    @Select("SELECT n.oid,   " +
            "            CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),    " +
            "           (SELECT title FROM desp_categories_details WHERE oid = n.oid AND xlang = #{plang} and cat_type_oid=#{catType})) AS title   " +
            "            FROM desp_categories AS n, desp_categories AS p   " +
            "            WHERE (n.lft BETWEEN p.lft AND p.rgt)   " +
            "            GROUP BY oid   " +
            "            ORDER BY n.lft; ")
    List<DespCategoryItem> getTreeByCatype(@Param("plang") String plang, @Param("catType") Integer catType);


    @Select("SELECT " +
            " desp_category_type.oid as typeOid, " +
            " desp_category_type.type as menuType, " +
            " desp_categories_details.oid,    " +
            " desp_categories_details.title,    " +
            " desp_categories.parent_oid,    " +
            " desp_categories_details.alias    " +
            " FROM    " +
            " desp_categories_details    " +
            " JOIN desp_categories    " +
            " ON desp_categories_details.oid = desp_categories.oid " +
            " JOIN desp_category_type " +
            " ON desp_category_type.oid = desp_categories_details.cat_type_oid " +
            " WHERE desp_categories_details.oid = #{itemOid}")
    @Results(value = {
            @Result(property = "type.oid", column = "typeOid"),
            @Result(property = "type.type", column = "menuType")
    })
    DespCategoryItem getCatItemById(@Param("itemOid") int itemOid);


    @Update(" UPDATE desp_categories_details   SET " +
            " title = #{newName}, " +
            " alias = #{alias}, " +
            " modified_user_id = #{updatedByOid} " +
            " WHERE desp_categories_details.oid=#{categoryItemOid};")
    int updateCategoryNodeName(@Param("categoryItemOid") int menuItemOid, @Param("updatedByOid") int updateByOid, @Param("newName") String newName, @Param("alias") String alias);


    @Select("SELECT title from desp_categories_details WHERE oid = #{oid};")
    String getParentName(@Param("oid") int parentOid);
}