package com.despani.core.mybatis.mappers;

import com.despani.core.beans.DespReferenceData;
import com.despani.core.beans.DespReferenceValue;
import com.despani.core.beans.form.ReferenceValueForm;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface IRefDataMapper {

//    static final String SELECT_REFERENCE_DATA= "SELECT\n" +
//            "\tdesp_ref_types.oid,\n" +
//            "\tdesp_ref_types.`name`,\n" +
//            "\tdesp_ref_types.type_key,\n" +
//            "\tdesp_ref_types.description,\n" +
//            "\tdesp_ref_types.published,\n" +
//            "\tdesp_ref_values.oid AS value_oid,\n" +
//            "\tdesp_ref_values.alt_seq,\n" +
//            "\tdesp_ref_values.`value` AS value_value,\n" +
//            "\tdesp_ref_values.description AS value_description,\n" +
//            "\tdesp_ref_values.published AS value_published \n" +
//            "FROM\n" +
//            "\tdesp_ref_values\n" +
//            "\tJOIN desp_ref_types ON desp_ref_values.ref_type_oid = desp_ref_types.oid \n" +
//            "WHERE\n" +
//            "\tdesp_ref_values.published = 1 \n" +
//            "\tAND desp_ref_types.published = 1 \n" +
//            "\tAND desp_ref_types.oid = 4;";

    static final String INSERT_REFERENCE_DATA = "INSERT INTO desp_ref_types(`type_key`, name, description, published) VALUES(#{typeKey},#{name},#{description},#{published})";
    static final String INSERT_REFERENCE_VALUE = "INSERT INTO desp_ref_values(alt_seq, ref_type_oid, value, description, published) VALUES(#{xobj.alt_seq}, #{xid},#{xobj.value},#{xobj.description},#{xobj.published})";



    static final String UPDATE_REFERENCE_DATA = "UPDATE despani3.desp_ref_types " +
                                                "SET description = #{description}, " +
                                                "published =  #{published} WHERE oid = #{oid};";
    static final String DELETE_REFERENCE_DATA = "DELETE FROM desp_ref_types WHERE `oid`= #{oid}";
    static final String DELETE_ALL_REFERENCE_DATA_VALUES = "DELETE FROM desp_ref_values WHERE ref_type_oid = #{ref_id}";


//    @Select(SELECT_REFERENCE_DATA)
//    @MapKey("oid")
//    public HashMap<Integer, DespReferenceData> getReferenceDataByOid (Integer oid );

    @Insert(INSERT_REFERENCE_VALUE)
    public void insertProperty(@Param("xobj") DespReferenceValue xobj, @Param("xid") int xid);



    @MapKey("typeKey")
    Map<String, DespReferenceData> getAllReferenceData();

    @Insert(INSERT_REFERENCE_DATA)
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    public void insertReferenceData(DespReferenceData refData);


    @Update (UPDATE_REFERENCE_DATA)
    public void updateReferenceData (DespReferenceData referenceData);

    @Delete(DELETE_REFERENCE_DATA)
    public void deleteReferenceData(@Param("oid") int oid);

    @Delete(DELETE_ALL_REFERENCE_DATA_VALUES)
    public void deleteAllReferenceDataValues(@Param("ref_id") int ref_id);
}