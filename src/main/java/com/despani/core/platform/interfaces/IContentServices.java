package com.despani.core.platform.interfaces;

import com.despani.core.beans.DespGlobalLocalProp;
import com.despani.core.beans.domains.DespContent;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.utils.DespPropertyX2;
import com.despani.core.utils.DespPropertyX2;

import java.util.List;
import java.util.Map;

public interface IContentServices {
    List<DespContent> getAllContent();
    List<DespContent> getAllContentFiltered();
    DespContent getContentByOidACL(Integer oid) throws Exception;
    DespContent getContentByOid(Integer oid) throws Exception;
    public List<DespGlobalLocalProp> getGlobalLocalProps(List<DespPropertyX2> despProps, int oid);
    public void setContentProperties(Map<String, String> allProps, int oid);
    public void updateContent(DespContent dc, Map<String, String> allProps);
    public void createContent(DespaniPrincipal dsp , DespContent dc, Map<String, String> allProps);
    int countContent();
    List<DespContent> getAllContentLimited(int limitPerPage, int offsetMultiplier);
    // ADMIN
    void publishContent(Integer userOid, Boolean publish);
    // ADMIN
    void featuredContent(Integer userOid, Boolean feature);
    List<DespContent> getContentLimited(int limitPerPage, int offsetMultiplier, boolean publishedOnly);
    List<DespContent> getfeaturedContent(int limitPerPage, int offsetMultiplier, boolean publishedOnly);
    void updateContentAccessLevel(Integer userOid, Integer access);
    List<DespPropertyX2> getContentLocalProps(int contentOid);
    Map<String, DespPropertyX2> getContentLocalPropsAsMap(int contentOid);
    void insertContentProperty(String name, String propValue, Integer contentOid);
    List<DespPropertyX2> loadContentProperties(Integer contentOid);
    Map<String, DespPropertyX2> loadContentPropertiesAsMap(Integer contentOid);
    void updateContentProperty(String name, String propValue, Integer contentOid);
    void deleteAllContentProperties(Integer contentOid);
    void deleteContentProperty(String name, Integer contentOid);
    List<DespPropertyX2> loadCatTypeProperties(Integer catTypeOid);
}