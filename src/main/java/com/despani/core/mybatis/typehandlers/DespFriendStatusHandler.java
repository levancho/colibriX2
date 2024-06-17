package com.despani.core.mybatis.typehandlers;


import com.despani.core.beans.enums.DespFriendStatus;
import com.despani.core.interfaces.IDespCodeEnum;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(DespFriendStatus.class)
public class DespFriendStatusHandler extends ADespTypeHandler {

    public DespFriendStatusHandler() {
    }


    @Override
    public IDespCodeEnum[] getEnums() {
        return DespFriendStatus.values();//To change body of implemented methods use File | Settings | File Templates.
    }

}
