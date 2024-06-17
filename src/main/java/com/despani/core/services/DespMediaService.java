package com.despani.core.services;


import com.despani.core.beans.domains.DespMedia;
import com.despani.core.mybatis.mappers.IDespMediaComapnyMapper;
import com.despani.core.mybatis.mappers.IDespMediaMapper;
import com.despani.core.mybatis.mappers.IDespMediaProductMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespMediaService {

    @Autowired
    IDespMediaMapper iDespMediaMapper;

    @Autowired
    IDespMediaProductMapper despMediaProductMapper;

    @Autowired
    IDespMediaComapnyMapper despMediaComapnyMapper;

    public void createDespMedia(DespMedia despMedia) {
        iDespMediaMapper.createDespMedia(despMedia);
    }

    public DespMedia getDespMediaByOid(Integer oid){
        return iDespMediaMapper.getDespMediaByOid(oid);
    }

    public DespMedia getDespMediaByFilename(String filename){
        return iDespMediaMapper.getMediaByFilename(filename);
    };

    public List<DespMedia> getDespMediaByType(Integer type){
        return iDespMediaMapper.getMediaByType(type);
    };

    public List<DespMedia> getDespMediaByPrimary(Integer primary){
        return iDespMediaMapper.getDespMediaByPrimary(primary);
    };

    public DespMedia getDespMediaByUrl(String url){
        return iDespMediaMapper.getMediaByUrl(url);
    };

    public List<DespMedia> getDespMediaByFormat(String format){
        return iDespMediaMapper.getMediaByFormat(format);
    };

    public List<DespMedia> getDespMediaByProductOid(Integer oid) {
        return iDespMediaMapper.getDespMediaByProductOid(oid);
    }

    public List<DespMedia> getDespMediaByCompanyOid(Integer oid) {
        return iDespMediaMapper.getDespMediaByCompanyOid(oid);
    }


    public List<DespMedia> getAllDespMedia() {
        return iDespMediaMapper.getAllDespMedia();
    };

    public void updateDespMedia(DespMedia despMedia){
        iDespMediaMapper.updateDespMedia(despMedia);
    };

    public void deleteDespMediaByOid(Integer oid){
        iDespMediaMapper.deleteDespMediaByOid(oid);
    };



    /** DESP-MEDIA-PRODUCT **/
    public void createMediaForProduct(int mediaOid, int productOid){
        despMediaProductMapper.insertDespMediaProduct(mediaOid, productOid);
    }

    public void updateMediaForProduct(int oid, int mediaOid, int productOid){
        despMediaProductMapper.updateDespMediaProduct(oid, mediaOid, productOid);
    }

    public void deleteMediaPForProduct(int oid){
        despMediaProductMapper.deleteDespMediaProductByOid(oid);
    }


    /** DESP-MEDIA-COMPANY **/
    public void createMediaForCompany(int mediaOid, int companyOid) {
        despMediaComapnyMapper.insertDespMediaCompany(mediaOid,companyOid);
    }

    public void updateMediaForCompany(int oid, int mediaOid, int productOid){
        despMediaComapnyMapper.updateDespMediaCompany(oid, mediaOid, productOid);
    }

    public void deleteMediaPForCompany(int oid){
        despMediaComapnyMapper.deleteDespMediaCompanyByOid(oid);
    }

    public void resetDespMediaPrimary(Integer productOid){
        iDespMediaMapper.resetDespMediaPrimary(productOid);
    }


   public void makeDespMediaPrimary(int mediaOid, boolean primary){
        iDespMediaMapper.makeDespMediaPrimary(mediaOid, primary);
   }

}
