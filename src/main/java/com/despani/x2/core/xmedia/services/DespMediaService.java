package com.despani.x2.core.xmedia.services;


import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import com.despani.x2.core.xmedia.mybatis.mappers.IDespMediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespMediaService {

    @Autowired
    IDespMediaMapper iDespMediaMapper;



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


    public void resetDespMediaPrimary(Integer productOid){
        iDespMediaMapper.resetDespMediaPrimary(productOid);
    }


   public void makeDespMediaPrimary(int mediaOid, boolean primary){
        iDespMediaMapper.makeDespMediaPrimary(mediaOid, primary);
   }

}
