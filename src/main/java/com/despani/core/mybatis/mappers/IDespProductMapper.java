package com.despani.core.mybatis.mappers;


import com.despani.core.beans.domains.*;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface IDespProductMapper {

    final String SELECT_PRODUCTS = "SELECT * FROM desp_product ";

    final String DELETE_PRODUCT = "DELETE FROM desp_product WHERE oid = #{oid}; ";

    final String DELETE_CHILD_PRODUCTS = "DELETE FROM desp_product WHERE parent_oid = #{oid}; ";

    final String INSERT_PRODUCT = " INSERT INTO desp_product (title, description, sku, price, published, category_oid,  company_oid, parent_oid) " +
                                        "VALUES ( #{title}, #{description}, #{sku}, #{price}, #{published}, #{category.oid},  #{company.oid}, #{parent.oid} ); ";

    final String UPDATE_PRODUCT = "UPDATE despani3.desp_product SET title = #{title}, description = #{description}, sku = #{sku}, price = #{price}, published = #{published},"+
                                    " category_oid = #{category.oid}, company_oid = #{company.oid} WHERE oid = #{oid}; ";

    final String PUBLISH_PRODUCT = "UPDATE despani3.desp_product SET published = #{published} WHERE oid = #{oid}; ";

    final String SELECT_PRODUCTS_PARENT = "select * from desp_product as lp left join desp_product as rp on lp.parent_oid = rp.oid ";


    @Insert(INSERT_PRODUCT)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    public void insertDespProduct(DespProduct despProduct);


    @Delete(DELETE_PRODUCT)
    public void deleteProduct(@Param("oid") Integer oid);


    @Delete(DELETE_CHILD_PRODUCTS)
    public void deleteChildProducts(@Param("oid") Integer oid);

    @Update(PUBLISH_PRODUCT)
    public void publishProduct(@Param("oid") Integer oid, @Param("published") boolean published);

    @Update(UPDATE_PRODUCT)
    public void updateProduct(DespProduct product);



    @Select(SELECT_PRODUCTS + " where oid = #{oid} ")
    @Results(value = {
            @Result(property = "oid", column= "oid"),
            @Result(property = "parent", column= "parent_oid", javaType = DespProduct.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespProductMapper.selectProductByOid")),
            @Result(property = "category", column = "category_oid", javaType = DespCategoryItem.class, one = @One(select = "com.despani.core.mybatis.mappers.ICategoryMapper.getCatItemById")),
            @Result(property = "company", column = "company_oid", javaType = DespCompany.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespCompanyMapper.getDespCompanyByOid")),
            @Result(property = "media", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByProductOid"))

    })
    public DespProduct selectProductWithParentByOid(@Param("oid") Integer oid);


    @Select(SELECT_PRODUCTS + " where oid = #{oid} ")
    @Results(value = {
            @Result(property = "oid", column= "oid"),
            @Result(property = "category", column = "category_oid", javaType = DespCategoryItem.class, one = @One(select = "com.despani.core.mybatis.mappers.ICategoryMapper.getCatItemById")),
            @Result(property = "company", column = "company_oid", javaType = DespCompany.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespCompanyMapper.getDespCompanyByOid")),
            @Result(property = "media", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByProductOid"))

    })
    public DespProduct selectProductByOid(@Param("oid") Integer oid);




    @Select(SELECT_PRODUCTS + " where parent_oid = #{oid}")
    @Results(value = {
            @Result(property = "oid", column= "oid"),
            @Result(property = "category", column = "category_oid", javaType = DespCategoryItem.class, one = @One(select = "com.despani.core.mybatis.mappers.ICategoryMapper.getCatItemById")),
//            @Result(property = "properties", column = "oid", javaType = Map.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties")),
            @Result(property = "company", column = "company_oid", javaType = DespCompany.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespCompanyMapper.getDespCompanyByOid")),
            @Result(property = "media", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByProductOid"))

    })
    public List<DespProduct> selectProductsByParentOid(@Param("oid") Integer oid);



    @Select(SELECT_PRODUCTS + " where parent_oid is null ")
    @Results(value = {
            @Result(property = "oid", column= "oid"),
            @Result(property = "category", column = "category_oid", javaType = DespCategoryItem.class, one = @One(select = "com.despani.core.mybatis.mappers.ICategoryMapper.getCatItemById")),
//            @Result(property = "properties", column = "oid", javaType = Map.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties")),
            @Result(property = "company", column = "company_oid", javaType = DespCompany.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespCompanyMapper.getDespCompanyByOid")),
            @Result(property = "media", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByProductOid"))

    })
    public List<DespProduct> selectParentProducts();

    @Select(SELECT_PRODUCTS + " where parent_oid is null and company_oid = #{oid} ")
    @Results(value = {
            @Result(property = "oid", column= "oid"),
            @Result(property = "category", column = "category_oid", javaType = DespCategoryItem.class, one = @One(select = "com.despani.core.mybatis.mappers.ICategoryMapper.getCatItemById")),
//            @Result(property = "properties", column = "oid", javaType = Map.class, many = @Many(select = "com.despani.core.mybatis.mappers.IPropertiesMapper2.loadContentProperties")),
            @Result(property = "company", column = "company_oid", javaType = DespCompany.class, one = @One(select = "com.despani.core.mybatis.mappers.IDespCompanyMapper.getDespCompanyByOid")),
            @Result(property = "media", column = "oid", javaType = List.class, many = @Many(select = "com.despani.core.mybatis.mappers.IDespMediaMapper.getDespMediaByProductOid"))

    })
    public List<DespProduct> selectParentProductsByCompanyOid(@Param("oid") Integer oid);











}
