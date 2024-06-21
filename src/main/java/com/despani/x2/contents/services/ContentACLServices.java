package com.despani.x2.contents.services;
import com.despani.x2.contents.beans.domains.DespContent;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
@ConditionalOnProperty(
        value="despani.app.acl.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class ContentACLServices extends ContentServices {

    @PostAuthorize("@secManager.hasRoleWeight(returnObject.access)")
    @Override
    public DespContent getContentByOid(Integer oid) throws Exception {
        return  super.getContentByOid(oid);
    }

    @PreAuthorize("@secManager.isOwner(#dc) or (hasPermission(#dc, 'CREATE') ) ")
    @Override
    public void createContent(DespaniPrincipal dsp , DespContent dc, Map<String, String> allProps){
        super.createContent(dsp,dc,allProps);
    }
    @PostFilter("hasPermission(filterObject, 'READ') or  @secManager.hasRoleWeight(filterObject.access)")
    @Override
    public List<DespContent> getAllContentLimited(int limitPerPage, int offsetMultiplier){
        return super.getAllContentLimited(limitPerPage, offsetMultiplier);
    }
    @PreAuthorize("@secManager.isOwner(#dc) or (hasPermission(#dc, 'WRITE') ) ")
    @Override
    public void updateContent(DespContent dc, Map<String, String> allProps) {
        super.updateContent(dc, allProps);
    }
    @PostFilter("hasPermission(filterObject, 'READ') or  @secManager.hasRoleWeight(filterObject.access)")
    public List<DespContent> getContentLimited(int limitPerPage, int offsetMultiplier, boolean publishedOnly) {
        return   super.getContentLimited(limitPerPage,offsetMultiplier,publishedOnly);
    }
}