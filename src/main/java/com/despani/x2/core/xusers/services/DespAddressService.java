package com.despani.x2.core.xusers.services;


import com.despani.x2.core.xusers.beans.domains.DespAddress;
import com.despani.x2.core.xusers.mybatis.mappers.IDespAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespAddressService {

    @Autowired
    IDespAddressMapper iDespAddressMapper;


    public void createDespAddress(DespAddress despAddress) {
        iDespAddressMapper.insertDespAddress(despAddress);
    }

    public DespAddress getDespAddressByOid(Integer oid) {
        return iDespAddressMapper.getDespAddressByOid(oid);
    }

    public List<DespAddress> getAllDespAddresses() {
        return iDespAddressMapper.getAllDespAddresses();
    }

    public void updateDespAddress(DespAddress despAddress) {
        iDespAddressMapper.updateDespAddress(despAddress);
    }

    public void deleteDespAddressByOid(Integer oid) {
        iDespAddressMapper.deleteDespAddressByOid(oid);
    }


}
