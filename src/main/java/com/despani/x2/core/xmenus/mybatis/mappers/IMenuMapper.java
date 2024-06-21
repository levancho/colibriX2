package com.despani.x2.core.xmenus.mybatis.mappers;

import com.despani.x2.core.beans.DespNodeType;
import com.despani.x2.core.xmenus.beans.domains.DespMenuItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMenuMapper {


//good original
//    @Select("SELECT" +
//            " desp_menu.oid," +
//            " desp_menu.lft," +
//            " desp_menu.rgt," +
//            " desp_menu.parent_oid," +
//            " desp_menu_details.xlevel as level," +
//            " desp_menu_details.title," +
//            "x desp_menu_details.link " +
//            " FROM " +
//            " desp_menu_details " +
//            " JOIN desp_menu " +
//            "  on desp_menu_details.oid = desp_menu.oid " +
//
//            " where parent_oid>#{parentoid} ")
//    List<NodeItem> getMenuNodes(@Param("parentoid") Integer parentoid) ;

    @Select("SELECT" +
            " desp_menu.oid," +
            " desp_menu.lft," +
            " desp_menu.rgt," +
            " desp_menu.parent_oid," +
            " desp_menu_details.xlevel as level," +
            " desp_menu_details.title," +
            " desp_menu_details.link " +
            " FROM " +
            " desp_menu_details " +
            " JOIN desp_menu " +
            "  on desp_menu_details.oid = desp_menu.oid" +

            " where parent_oid>#{parentoid} ")
    List<DespMenuItem> getMenuNodes(@Param("parentoid") Integer parentoid) ;

    @Select("SELECT" +
            " desp_menu.oid," +
            " desp_menu.lft," +
            " desp_menu.rgt," +
            " desp_menu.parent_oid," +
            " desp_menu_details.xlevel as level," +
            " desp_menu_details.title," +
            " desp_menu_details.link " +
            " FROM " +
            " desp_menu_details " +
            " JOIN desp_menu " +
            "  on desp_menu_details.oid = desp_menu.oid where parent_oid>#{parentoid}  AND desp_menu_details.menu_type_oid=#{menuTypeX}" )

    List<DespMenuItem> getTypeMenuNodes(@Param("parentoid") Integer parentoid, @Param("menuTypeX") Integer menuTypeX) ;



    @Select("SELECT oid,  type from desp_menu_type")
    List<DespNodeType> getAllMenuTypes();


    @Select("SELECT " +
            " desp_menu.oid," +
            " desp_menu.lft," +
            " desp_menu.rgt," +
            " desp_menu.parent_oid" +
            " FROM " +
            " desp_menu " +
            " WHERE " +
            " desp_menu.oid=#{parentOid}  ")
    @Results(value={
            @Result(property = "type.oid",column="typeOid"),
            @Result(property = "type.type",column="type")

    })
    DespMenuItem getNodeByItemOidLight(int parentOid);


//original
    @Select("    SELECT n.oid, " +
            "      CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),  " +
            "      (SELECT title FROM desp_menu_details WHERE oid = n.oid AND xlang = #{plang})) AS title " +
            "    FROM desp_menu AS n, desp_menu AS p " +
            "    WHERE (n.lft BETWEEN p.lft AND p.rgt) " +
            "    GROUP BY oid " +
            "    ORDER BY n.lft;  ")
    List<DespMenuItem> getTree(@Param("plang") String plang);


//original
    @Select("    SELECT n.oid, " +
            "      CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),  " +
            "      (SELECT title FROM desp_menu_details WHERE oid = n.oid AND xlang = #{plang} and menu_type_oid=#{menuType})) AS title " +
            "    FROM desp_menu AS n, desp_menu AS p " +
            "    WHERE (n.lft BETWEEN p.lft AND p.rgt) " +
            "    GROUP BY oid " +
            "    ORDER BY n.lft;  ")
    List<DespMenuItem> getTreeByMenuType(@Param("plang") String plang, @Param("menuType") Integer menuType);


    @Select("    SELECT n.oid, " +
            "      CONCAT( REPEAT(' . . ', COUNT(CAST(p.oid AS CHAR)) - 1),  " +
            "      (SELECT title FROM desp_menu_details WHERE oid = n.oid AND xlang = #{plang})) AS title " +
            "    FROM desp_menu AS n, desp_menu AS p " +
            "    WHERE (n.lft BETWEEN p.lft AND p.rgt)  AND n.oid != #{pedited}" +
            "    GROUP BY oid " +
            "    ORDER BY n.lft;  ")
    List<DespMenuItem> getTree2(@Param("plang") Integer pedited, @Param("plang") String plang) ;

    //        $sql = "CALL r_tree_traversal('insert', NULL, {$_POST['parent_id']});";
//        $prep = $pdo->prepare($sql);
//        $prep->execute();
//        $newNodeId = (int) $prep->fetchColumn();


            @Select(value= "{ CALL  createMenu (  " +
            "#{pparent_id, mode=IN, jdbcType=INTEGER}," +
            "#{menutype_in, mode=IN, jdbcType=INTEGER}," +
            "#{title_in, mode=IN, jdbcType=VARCHAR}," +
            "#{alias_in, mode=IN, jdbcType=VARCHAR}," +
            "#{path_in, mode=IN, jdbcType=VARCHAR}," +
            "#{link_in, mode=IN, jdbcType=VARCHAR}," +
            "#{type_in, mode=IN, jdbcType=VARCHAR}," +
            "#{img_in, mode=IN, jdbcType=VARCHAR}," +
            "#{params_in, mode=IN, jdbcType=VARCHAR}," +
            "#{newid,mode=OUT, jdbcType=INTEGER}" +
            ")}")
    @Options(statementType = StatementType.CALLABLE)
    public void createMenuFull(Map<String, Object> m);

    @Select(value= "{ CALL  deleteMenu (#{pid, mode=IN, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    public void deleteMenuItem(Map<String, Object> m);

    @Select(value= "{ CALL  sp_traverse_menu (#{typex, mode=IN, jdbcType=VARCHAR},#{pid, mode=IN, jdbcType=INTEGER},#{parentId, mode=IN, jdbcType=INTEGER})}")
    String traverseMenu(Map m);

    @Select("SELECT " +
            " desp_menu_details.menu_type_oid AS typeOid, " +
            " desp_menu.oid," +
            " desp_menu_type.type as menuType " +
            "FROM " +
            " desp_menu_details " +
            "JOIN  desp_menu " +
            "ON  desp_menu_details.oid =  desp_menu.oid " +
            "JOIN  desp_menu_type "+
            "ON  desp_menu_details.menu_type_oid =  desp_menu_type.oid "+
            "WHERE " +
            " desp_menu.oid=#{parentOid}  ")
    @Results(value={
            @Result(property = "menuType.oid",column="typeOid"),
            @Result(property = "menuType.type",column="menuType")

    })
    DespMenuItem getParentNode(@Param("parentOid") int parentOid);


    @Select("SELECT  " +
            "desp_menu_type.oid as typeOid, " +
            "desp_menu_type.type as menuType, " +
            "desp_menu_details.oid,  " +
            "desp_menu_details.title,  " +
            "desp_menu_details.link,  " +
            "desp_menu_details.img,  " +
            "desp_menu.parent_oid  " +
            "FROM desp_menu_details " +
            "JOIN desp_menu  " +
            "ON desp_menu_details.oid = desp_menu.oid " +
            "JOIN desp_menu_type " +
            "ON desp_menu_type.oid = desp_menu_details.menu_type_oid " +
            "WHERE desp_menu_details.oid = #{menuItemOid}")
    @Results(value={
            @Result(property = "menuType.oid",column="typeOid"),
            @Result(property = "menuType.type",column="menuType")
    })
    DespMenuItem getMenuItemById(@Param("menuItemOid") int menuItemOid);



    @Update("UPDATE desp_menu_details    " +
            "SET title = #{newName},   " +
            "alias = #{newName}, "+
            "path = #{url}, "+
            "link = #{url}, "+
            "img = #{img} "+
//            "modified_user_id = #{updaterOid} "+
            "WHERE desp_menu_details.oid=#{menuItemOid}; ")
    int updateMenuNodeName(@Param("menuItemOid") int menuItemOid,@Param("updaterOid") int updaterOid,@Param("newName") String newName,@Param("url")String url,@Param("img")String img);

    @Select("SELECT title from desp_menu_details WHERE oid = #{oid};")
    String getNodeName(@Param("oid") Integer parent_oid);
}