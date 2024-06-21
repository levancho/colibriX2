package com.despani.x2.core.mybatis.typehandlers;

import com.despani.x2.core.beans.enums.DespPropertyType;
import com.despani.x2.core.interfaces.IDespCodeEnum;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(DespPropertyType.class)
public class DespPropertyTypeHandler extends ADespTypeHandler {

    public DespPropertyTypeHandler() {
    }


    @Override
    public IDespCodeEnum[] getEnums() {
        return DespPropertyType.values();//To change body of implemented methods use File | Settings | File Templates.
    }

}
