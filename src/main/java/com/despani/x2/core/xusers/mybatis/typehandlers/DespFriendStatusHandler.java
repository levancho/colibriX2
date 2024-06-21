package com.despani.x2.core.xusers.mybatis.typehandlers;


import com.despani.x2.core.interfaces.IDespCodeEnum;
import com.despani.x2.core.mybatis.typehandlers.ADespTypeHandler;
import com.despani.x2.core.xusers.beans.enums.DespFriendStatus;
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
