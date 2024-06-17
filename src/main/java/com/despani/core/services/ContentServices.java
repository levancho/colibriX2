package com.despani.core.services;
import com.despani.core.beans.DespGlobalLocalProp;
import com.despani.core.beans.domains.DespContent;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.mybatis.mappers.IContentMapper;
import com.despani.core.mybatis.mappers.IContentPropertiesMapper;
import com.despani.core.platform.interfaces.IContentServices;
import com.despani.core.utils.DespPropertyX2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@ConditionalOnProperty(
        value="despani.app.acl.enabled",
        havingValue = "false",
        matchIfMissing = false )
public class ContentServices  implements IContentServices {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public IContentMapper mapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public IContentPropertiesMapper propMapper;


    @Override
    public List<DespContent> getAllContent() {
        return  mapper.getAllContent();
    }
    @Override
    public List<DespContent> getAllContentFiltered() {
        return  mapper.getAllContent();
    }
    @Override
    public DespContent getContentByOidACL(Integer oid) throws Exception {
        DespContent content = mapper.getContentByOid(oid);
        return content;
    }
    @Override
    public DespContent getContentByOid(Integer oid) throws Exception {
        DespContent content = mapper.getContentByOid(oid);
        return content;
    }
    @Transactional
    public void createContent(DespaniPrincipal dsp , DespContent dc, Map<String, String> allProps) {
        mapper.createContent(dc);
        setContentProperties( allProps, dc.getOid());
    }
    @Transactional
    public void updateContent(DespContent dc, Map<String, String> allProps) {
        mapper.saveContent(dc);
        setContentProperties(allProps, dc.getOid());
    }
    @Override
    public int countContent() {
        return mapper.countContent();
    }
    @Override
    public List<DespContent> getAllContentLimited(int limitPerPage, int offsetMultiplier){
        List<DespContent> contetnToShow = mapper.getAllContentLimited(limitPerPage, offsetMultiplier);
        return contetnToShow;
    }
    public void setContentProperties(Map<String, String> allProps, int oid){
        Map<String, String> localProps = allProps.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("content") && allProps.get("global." + entry.getKey()) == null)
                .map(entry -> {
                    if (entry.getValue() == null) entry.setValue("off");
                    return entry;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        deleteAllContentProperties(oid);
        localProps.forEach((k, v) -> insertContentProperty(k, v, oid));
    }
    // ADMIN
    @Override
    public void publishContent(Integer userOid, Boolean publish) {
        mapper.publishContent(userOid,publish);
    }
    // ADMIN
    @Override
    public void featuredContent(Integer userOid, Boolean feature) {
        mapper.fetaureContent(userOid,feature);
    }
    @Override
    public List<DespContent> getContentLimited(int limitPerPage, int offsetMultiplier, boolean publishedOnly) {
        if(publishedOnly) {
            return mapper.getContentLimitedPublished(limitPerPage,offsetMultiplier,publishedOnly);
        }else {
            return mapper.getContentLimited(limitPerPage,offsetMultiplier);
        }
    }
    @Override
    public List<DespContent> getfeaturedContent(int limitPerPage, int offsetMultiplier, boolean publishedOnly) {
        if(publishedOnly) {
            return mapper.getfeaturedContentPublished(limitPerPage,offsetMultiplier,publishedOnly);
        }else {
            return mapper.getfeaturedContent(limitPerPage,offsetMultiplier);
        }
    }
    @Override
    public void updateContentAccessLevel(Integer userOid, Integer access) {
        mapper.updateContentAccessLevel(userOid,access);
    }
    @Override
    public List<DespPropertyX2> getContentLocalProps(int contentOid){
        return propMapper.loadContentProperties(contentOid);
    }
    public Map<String, DespPropertyX2> getContentLocalPropsAsMap(int contentOid){
        return propMapper.loadContentPropertiesAsMap(contentOid);
    }
    public List<DespGlobalLocalProp> getGlobalLocalProps(List<DespPropertyX2> despProps, int oid){
        Map<String, DespPropertyX2> despContentLocalProps = propMapper.loadContentPropertiesAsMap(oid);
        List<DespGlobalLocalProp> despGlobalLocalProps = despProps.stream().map(prop -> {
            String name = prop.getName();
            String shortName = prop.getName().substring(10);
            String propValue = prop.getPropValue();
            int type = Integer.parseInt(prop.getName().substring(8, 9));
            boolean isGlobal = true;
            if (despContentLocalProps.containsKey(name)) {
                isGlobal = false;
                propValue = despContentLocalProps.get(name).getPropValue();
            }
            return new DespGlobalLocalProp(name, shortName, propValue, type, isGlobal);
        }).collect(Collectors.toList());
        return despGlobalLocalProps;
    }
    @Override
    public void insertContentProperty(String name, String propValue, Integer contentOid) {
        propMapper.insertContentProperty(name, propValue, contentOid);
    }
    @Override
    public List<DespPropertyX2> loadContentProperties(Integer contentOid) {
        return propMapper.loadContentProperties(contentOid);
    }
    @Override
    public Map<String, DespPropertyX2> loadContentPropertiesAsMap(Integer contentOid) {
        return propMapper.loadContentPropertiesAsMap(contentOid);
    }
    @Override
    public void updateContentProperty(String name, String propValue, Integer contentOid) {
        propMapper.updateContentProperty(name, propValue, contentOid);
    }
    @Override
    public void deleteAllContentProperties(Integer contentOid){
        propMapper.deleteAllContentProperties(contentOid);
    }
    @Override
    public void deleteContentProperty(String name, Integer contentOid) {
        propMapper.deleteContentProperty(name, contentOid);
    }
    @Override
    public List<DespPropertyX2> loadCatTypeProperties(Integer catTypeOid) {
        return propMapper.loadCatTypeProperties(catTypeOid);
    }
}