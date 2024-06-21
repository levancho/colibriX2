package com.despani.x2.core.interfaces;


import com.despani.x2.core.beans.enums.DespContentTypes;
import com.despani.x2.core.exceptions.base.DespRuntimeException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * content type : content
 *  baseURI : /content
 *  link : /content/oid/1 but if this object has parent, then it will be /parentContentType/{parentOid}/thisCOntentType/{oid}
 *  if object has child: /contetType/{oid}/childcontentType/{childOid}
 *
 *  else it will be /contenttype/{oid}/1
 *
 *  get list link and page
 *  get view link and page
 *  get edit link and page
 *  get
 *
 */

public interface ILinkable {

    ILinkable getXParent();

    boolean hasXParent();

    ILinkable getXChild();

    boolean hasXChild();

    DespContentTypes getContentType();

    Integer getOid();


    @JsonIgnore
    default String getObjectName() {
        return getContentType().getValue();
    }

    @JsonIgnore
    default  String getObjectBaseURI() {

        String link = getObjectName();
        if(link==null) {
            throw new DespRuntimeException("can Not generate link when linkable returns null ");
        }

        if(!link.startsWith("/")){
            link ="/"+link;
        }
        return link;
    }

    @JsonIgnore
    default  String getObjectPage(boolean includeparent, boolean inclideChild) {
        // /content
        String link = getObjectBaseURI();

        if(includeparent && hasXParent()){
            String parentLink = getXParent().getObjectBaseURI();
            link = parentLink +link;
        }

        if(inclideChild && hasXChild()){
            String childLink = getXChild().getObjectBaseURI();
            link = link+childLink;
        }


        return link;
    }

    // backward compatibility
    default  String getObjectLink() {
        return getObjectLink(false,false);
    }

    @JsonIgnore
    default  String getObjectLink(boolean includeparent, boolean inclideChild) {
        String link = getObjectBaseURI();

        if(!link.endsWith("/")){
            link =link+"/";
        }
        link =  link+"oid/"+this.getOid();

        if(includeparent && hasXParent()){
            // we do not want child url since this is a child
            String parentLink = getXParent().getObjectLink(true,false);
            link = parentLink +link;
        }

        if(inclideChild && hasXChild()){
            String childLink = getXChild().getObjectLink(false,true);
            link = link+childLink;
        }


        return link;
    }

    @JsonIgnore
    default  String _getObjectLinkWithSuffix(boolean includeparent, boolean inclideChild, String suffix) {
        String link = getObjectLink(includeparent,inclideChild);

        if(!link.endsWith("/")){
            link =link+"/";
        }

        if(includeparent && hasXParent()){
            // we do not want child url since this is a child
            String parentLink = getXParent().getObjectLink(true,false);
            link = parentLink +link;
        }

        if(inclideChild && hasXChild()) {
            String childLink = getXChild().getObjectLink(false, true);
            link = link + childLink;
        }

        return link+suffix;
    }

    @JsonIgnore
    default  String _getObjectPageWithSuffix(boolean includeparent, boolean inclideChild, String suffix) {
        String link = getObjectPage(includeparent,inclideChild);

        if(!link.endsWith("/")){
            link =link+"/";
        }

        if(includeparent && hasXParent()){
            String parentLink = getXParent().getObjectPage(true, false);
            link = parentLink +link;
        }

        if(inclideChild && hasXChild()){
            String childLink = getXChild().getObjectPage(false,true);
            link = link+childLink;
        }

        return link+suffix;
    }



    @JsonIgnore
    default  String getObjectViewLink() {
        return getObjectViewLink(false,false);
    }

    @JsonIgnore
    default  String getObjectViewLink(boolean includeparent, boolean inclideChild) {
        return _getObjectLinkWithSuffix(includeparent,inclideChild,"view");
    }


    @JsonIgnore
    default  String getObjectEditLink() {
        return getObjectEditLink(false,false);
    }

    @JsonIgnore
    default  String getObjectViewPage() {
        return getObjectViewPage(false,false);
    }

    @JsonIgnore
    default  String getObjectViewPage(boolean includeparent, boolean inclideChild) {
        return _getObjectPageWithSuffix(includeparent,inclideChild,"view");
    }


    @JsonIgnore
    default  String getObjectEditLink(boolean includeparent, boolean inclideChild) {
        return _getObjectLinkWithSuffix(includeparent,inclideChild,"edit");
    }


    @JsonIgnore
    default  String getObjectEditPage() {
        return getObjectEditPage(false,false);
    }
    @JsonIgnore
    default  String getObjectEditPage(boolean includeparent, boolean inclideChild) {
        return _getObjectPageWithSuffix(includeparent,inclideChild,"edit");
    }

    @JsonIgnore
    default  String getListLink(){
        return getListLink(false);
    }

    @JsonIgnore
    default  String getListLink(boolean includeparent) {
        String link = getObjectBaseURI();

        if(!link.endsWith("/")){
            link =link+"/";
        }

        if(includeparent && hasXParent()){
            // we do not want child url since this is a child
            String parentLink = getXParent().getObjectLink(true,false);
            link = parentLink +link;
        }

        String retString =  link+"list";
        return retString;
    }


    @JsonIgnore
    default  String getNewLink(boolean includeparent, boolean inclideChild) {
        return _getObjectLinkWithSuffix(includeparent,inclideChild,"new");
    }


    @JsonIgnore
    default  String getListPage() {
        String link = getObjectBaseURI();

        if(!link.endsWith("/")){
            link =link+"/";
        }
        return link+"list";
    }


    @JsonIgnore
    default String getObjectSaveLink(){
        return getObjectSaveLink(false,false);
    }

    @JsonIgnore
    default String getObjectSaveLink(boolean includeparent, boolean inclideChild){
        return _getObjectLinkWithSuffix(includeparent,inclideChild,"save");
    }
}
