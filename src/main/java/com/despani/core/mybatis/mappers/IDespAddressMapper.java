package com.despani.core.mybatis.mappers;


import com.despani.core.beans.domains.DespAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface IDespAddressMapper {

    @Insert("INSERT INTO `despani3`.`desp_address` ( `name`, `address`, `city`, `state`, `zipCode`)\n" +
            "VALUES ( #{name}, #{address}, #{city}, #{state}, #{zipCode});")
    @Options(useGeneratedKeys = true, keyProperty = "oid")
    void insertDespAddress(DespAddress despAddress);


    @Select("SELECT * FROM desp_address where desp_address.oid = #{oid};")
    DespAddress getDespAddressByOid(@Param("oid") Integer oid);


    @Select("SELECT * FROM desp_address;")
    List<DespAddress> getAllDespAddresses();

    @Update("UPDATE `despani3`.`desp_address` \n" +
            "SET \n" +
            "\t`name` = #{name},\n" +
            "\t`address` = #{address},\n" +
            "\t`city` = #{city},\n" +
            "\t`state` = #{state},\n" +
            "\t`zipCode` = #{zipCode} \n" +
            "WHERE\n" +
            "\t`oid` = #{oid};")
    void updateDespAddress(DespAddress despAddress);

    @Delete("DELETE FROM desp_address where oid = #{oid};")
    void deleteDespAddressByOid(@Param("oid") Integer oid);

}
