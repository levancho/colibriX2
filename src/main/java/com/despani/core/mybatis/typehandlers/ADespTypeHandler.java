package com.despani.core.mybatis.typehandlers;

import com.despani.core.interfaces.IDespCodeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class  ADespTypeHandler extends BaseTypeHandler implements TypeHandler {

  public abstract   IDespCodeEnum[] getEnums();


  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    ps.setInt(i, (( IDespCodeEnum) parameter).getCode());
  }

  public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
    int s = rs.getInt(columnName);
    for (  IDespCodeEnum status : getEnums()) {
      if (status.getCode() == s) {
        return status;
      }
    }
    return null;
  }


    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int s = rs.getInt(columnIndex);
        for (  IDespCodeEnum status : getEnums()) {
            if (status.getCode() == s) {
                return status;
            }
        }
        return null;
    }



  public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    int s = cs.getInt(columnIndex);
    for (  IDespCodeEnum status : getEnums()) {
      if (status.getCode() == s) {
        return status;
      }
    }
    return null;
  }

}