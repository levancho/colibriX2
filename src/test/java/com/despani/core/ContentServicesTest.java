//package com.despani.core;
//
//import com.despani.core.beans.domains.DespContent;
//import com.despani.core.mybatis.mappers.IContentMapper;
//import com.despani.core.mybatis.mappers.IPropertiesMapper2;
//import com.despani.core.utils.DespPropertyX2;
//import com.despani.core.utils.DespPropertyX2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ContentServicesTest {
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    public IContentMapper mapper;
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    public IPropertiesMapper2 propMapper;
//
//
//    public List<DespContent> getAllContent() {
//       return  mapper.getAllContent();
//    }
//
//
//    public List<DespContent> getAllContentFiltered() {
//        return  mapper.getAllContent();
//    }
//
//
//    public DespContent getContentByOidACL(Integer oid) throws Exception {
//        DespContent content = mapper.getContentByOid(oid);
//        return content;
//    }
//
//    public DespContent getContentByOid(Integer oid) throws Exception {
//        DespContent content = mapper.getContentByOid(oid);
//        return content;
//    }
//
//    public void createContent(DespContent dc) {
//        mapper.createContent(dc);
//    }
//
//    public void saveContent( DespContent dc) {
//        mapper.saveContent(dc);
//    }
//
//    public int countContent() {
//        return mapper.countContent();
//    }
//
//    public List<DespContent> getAllContentLimited(int limitPerPage, int offsetMultiplier){
//
//        List<DespContent> contetnToShow = mapper.getAllContentLimited(limitPerPage, offsetMultiplier);
//
//        return contetnToShow;
//    }
//
//
//
//    // ADMIN
//    public void publishContent(Integer userOid, Boolean publish) {
//        mapper.publishContent(userOid,publish);
//    }
//
//    // ADMIN
//    public void featuredContent(Integer userOid, Boolean feature) {
//        mapper.fetaureContent(userOid,feature);
//
//    }
//
//    public List<DespContent> getContentLimited(int limitPerPage, int offsetMultiplier, boolean publishedOnly) {
//        if(publishedOnly) {
//            return mapper.getContentLimitedPublished(limitPerPage,offsetMultiplier,publishedOnly);
//        }else {
//            return mapper.getContentLimited(limitPerPage,offsetMultiplier);
//        }
//
//    }
//
//    public List<DespContent> getfeaturedContent(int limitPerPage, int offsetMultiplier, boolean publishedOnly) {
//
//        if(publishedOnly) {
//            return mapper.getfeaturedContentPublished(limitPerPage,offsetMultiplier,publishedOnly);
//        }else {
//            return mapper.getfeaturedContent(limitPerPage,offsetMultiplier);
//        }
//    }
//
//    public void updateContentAccessLevel(Integer userOid, Integer access) {
//        mapper.updateContentAccessLevel(userOid,access);
//    }
//
//
//    public List<DespPropertyX2> getContentLocalProps(int contentOid){
//        return propMapper.loadContentProperties(contentOid);
//    }
//    public Map<String, DespPropertyX2> getContentLocalPropsAsMap(int contentOid){
//        return propMapper.loadContentPropertiesAsMap(contentOid);
//    }
//
//    public void insertContentProperty(String name, String propValue, Integer contentOid) {
//        propMapper.insertContentProperty(name, propValue, contentOid);
//    }
//
//    public List<DespPropertyX2> loadContentProperties(Integer contentOid) {
//        return propMapper.loadContentProperties(contentOid);
//    }
//
//    public Map<String, DespPropertyX2> loadContentPropertiesAsMap(Integer contentOid) {
//        return propMapper.loadContentPropertiesAsMap(contentOid);
//    }
//
//    public void updateContentProperty(String name, String propValue, Integer contentOid) {
//        propMapper.updateContentProperty(name, propValue, contentOid);
//    }
//
//
//    public void deleteAllContentProperties(Integer contentOid){
//        propMapper.deleteAllContentProperties(contentOid);
//    }
//
//    public void deleteContentProperty(String name, Integer contentOid) {
//        propMapper.deleteContentProperty(name, contentOid);
//    }
//
//
//    public List<DespPropertyX2> loadCatTypeProperties(Integer catTypeOid) {
//        return propMapper.loadCatTypeProperties(catTypeOid);
//    }
//}
