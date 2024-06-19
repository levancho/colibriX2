package com.despani.core.services;

import com.despani.core.beans.DespReferenceData;
import com.despani.core.beans.DespReferenceValue;
import com.despani.core.exceptions.DespRuntimeException;
import com.despani.core.mybatis.mappers.IRefDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RefDataServices {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IRefDataMapper refMapper;

    private Map<String, DespReferenceData> refDat = null;

    public void loadReferenceData() {
        if (refDat == null) {
            refDat = refMapper.getAllReferenceData();
        } else {
            log.info("Data is already loaded");
        }
    }

    public DespReferenceData getReferenceData(String key) {
        if (refDat == null) {
            throw new DespRuntimeException("reference data should be loaded at startup , it cant be null");
        }
        return refDat.get(key);
    }


    public DespReferenceData getReferenceDataByOid(final Integer oid) {
       return refMapper.getAllReferenceData().values().stream()
                .filter(v -> v.getOid() == oid).findAny().orElse(null);
    }



    public void deleteReferenceData(int id) {
        refMapper.deleteReferenceData(id);
    }

    public void deleteAllReferenceDataValues(int id) {
        refMapper.deleteAllReferenceDataValues(id);
    }

    public boolean hasRefData(String key) {
        return refDat.containsKey(key);
    }

    public void insertReferenceData(DespReferenceData data) {
        refMapper.insertReferenceData(data);
    }

    public void insertRefValue(DespReferenceValue value, int id) {
        refMapper.insertProperty(value, id);
    }



    public void updateReferenceData (DespReferenceData despReferenceData){
        refMapper.updateReferenceData(despReferenceData);
    }



    public void reset() {
        this.refDat = null;
        loadReferenceData();
    }
}

