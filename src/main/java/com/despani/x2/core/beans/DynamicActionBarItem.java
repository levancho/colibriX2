package com.despani.x2.core.beans;

import com.despani.x2.core.beans.enums.DespContentTypes;
import com.despani.x2.core.interfaces.IActionBarItem;
import com.despani.x2.core.interfaces.ILinkable;
import lombok.Data;

@Data
public class DynamicActionBarItem implements IActionBarItem {

    private String action;
    private String styleName;
    private String label;
    private String name;
    private String icon;

    private ILinkable context;

    public DynamicActionBarItem(ILinkable context, String action, String name, String label, String styleName, String icon ) {
        this.action = action;
        this.styleName = styleName;
        this.label = label;
        this.name = name;
        this.context = context;
        this.icon = icon;
    }


    @Override
    public ILinkable getXParent() {
        return context.getXParent();
    }

    @Override
    public boolean hasXParent() {
        return context.hasXParent();
    }

    @Override
    public ILinkable getXChild() {
        return context.getXChild();
    }

    @Override
    public boolean hasXChild() {
        return context.hasXChild();
    }

    @Override
    public DespContentTypes getContentType() {
        return context.getContentType();
    }

    @Override
    public Integer getOid() {
        return context.getOid();
    }

    @Override
    public String getObjectName() {
        return context.getObjectName();
    }

    @Override
    public String getObjectBaseURI() {
        return context.getObjectBaseURI();
    }

    @Override
    public String getObjectLink() {
            String contextLink = context.getObjectBaseURI();
            if(!contextLink.endsWith("/")){
                contextLink =contextLink+"/";
            }

            String link = this.action;

            String retString =contextLink +link;
            return retString;
    }
}
