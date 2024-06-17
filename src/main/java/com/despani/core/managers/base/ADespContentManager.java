package com.despani.core.managers.base;

import com.despani.core.beans.Pagination;
import com.despani.core.beans.domains.DespModule;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.exceptions.DespRuntimeException;
import com.despani.core.interfaces.ILinkable;
import com.despani.core.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;

public  abstract class ADespContentManager {


    @Autowired
    protected ModuleService modService;

    public abstract String  getManagerbaseURI();


    public String prev (Pagination p) {
        ILinkable l  = p.getPage();

        String retString = getBaseURI(l)+"?page={0}&limit={1}";
        return  MessageFormat.format(
                retString,
                p.getRequestedPageNumber()-1,p.getLimite());

    }

    public String next (Pagination p) {
        ILinkable l  = p.getPage();

        String retString = getBaseURI(l)+"?page={0}&limit={1}";
        return  MessageFormat.format(
                retString,
                p.getRequestedPageNumber()+1,p.getLimite());

    }


    public String pageNumber (Pagination p,int i) {
        ILinkable l  = p.getPage();
        String retString = getBaseURI(l)+"?page={0}&limit={1}";
        return  MessageFormat.format(
                retString,
                i,p.getLimite());

    }

    public String getLink(ILinkable linkable){

        String link = linkable.getObjectLink();
        String base = getManagerbaseURI();
        if(base==null) {
            throw new DespRuntimeException("can Not generate link when base returns null check manager");
        }

        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }


        if(!base.startsWith("/")){
            base ="/"+base;
        }

        return base+link;
    }

    public String getEditLink(ILinkable linkable){
        String link = linkable.getObjectEditLink();
        String base = getManagerbaseURI();
        if(base==null) {
            throw new DespRuntimeException("can Not generate link when base returns null check manager");
        }

        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }


        if(!base.startsWith("/")){
            base ="/"+base;
        }

        return base+link;
    }

    public String getViewLink(ILinkable linkable){
        String link = linkable.getObjectViewLink();
        String base = getManagerbaseURI();
        if(base==null) {
            throw new DespRuntimeException("can Not generate link when base returns null check manager");
        }

        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }


        if(!base.startsWith("/")){
            base ="/"+base;
        }

        return base+link;
    }

    public String getSaveLink(ILinkable linkable){
        String link = linkable.getObjectSaveLink();
        String base = getManagerbaseURI();
        if(base==null) {
            throw new DespRuntimeException("can Not generate link when base returns null check manager");
        }

        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }


        if(!base.startsWith("/")){
            base ="/"+base;
        }

        return base+""+link;
    }


    public String getBaseURI(ILinkable linkable){
        String base = getManagerbaseURI();
        String link = linkable.getObjectBaseURI();
        if(base==null) {
            throw new DespRuntimeException("can Not generate link when base returns null check manager");
        }

        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }


        if(!base.startsWith("/")){
            base ="/"+base;
        }

        return base+link;
    }


    public String getAvatar(DespaniUser user) {


        String av = user.getProfile().getAvatar();
        String g = user.getProfile().getGender();
        if(av!=null && !av.trim().equalsIgnoreCase("") ){
            return av;
        }else {
            av = (g==null || g.trim().equalsIgnoreCase("male")?"/images/users/avatar_m.png":"/images/users/avatar_f.png");
        }
        return av;
    }


    // sugar
    public String $$(ILinkable linkable){
        return getLink(linkable);
    }

    public List<DespModule> getModulesByPosition(String position) {
        return   modService.getModulesMap().get(position);
    }
}
