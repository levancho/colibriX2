package com.despani.x2.core.services;

import com.despani.x2.core.mybatis.mappers.IPropertiesMapper;
import com.despani.x2.core.utils.DespPropertyX2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertiesServices {



    @Autowired
    private IPropertiesMapper propMapper;

//    public List<DespPropertyX2> loadContentProperties(Integer contentOid) {
//        return propMapper.loadContentProperties(contentOid);
//    }
//
//    public Map<String, DespPropertyX2> loadContentPropertiesAsMap(Integer contentOid) {
//        return propMapper.loadContentPropertiesAsMap(contentOid);
//    }
//
//
//    public void insertContentProperty(String name, String propValue, Integer contentOid) {
//        propMapper.insertContentProperty(name, propValue, contentOid);
//    }
//
//
//    public void updateContentProperty(String name, String propValue, Integer contentOid) {
//        propMapper.updateContentProperty(name, propValue, contentOid);
//    }
//
//
//    public void deleteContentProperty(String name, Integer contentOid) {
//        propMapper.deleteContentProperty(name, contentOid);
//    }


//    public List<DespPropertyX2> loadCatTypeProperties(Integer catTypeOid) {
//        return propMapper.loadCatTypeProperties(catTypeOid);
//    }
    public void insertProperty(DespPropertyX2 prop){
        propMapper.insertProperty(prop);
    }

//    public void insertProperty(DespPropertyX2 prop){
//        propMapper.insertProperty2(prop);
//    }
//public Map<String, String> getPropertiesAsMap
//
//
}
