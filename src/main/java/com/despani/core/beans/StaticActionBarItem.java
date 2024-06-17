package com.despani.core.beans;

import com.despani.core.beans.enums.DespContentTypes;
import com.despani.core.interfaces.IActionBarItem;
import com.despani.core.interfaces.ILinkable;
import lombok.Data;

@Data
public class StaticActionBarItem implements IActionBarItem {

    private String action;
    private String styleName;
    private String label;
    private String name;
    private String icon;
    private int xorder;


    public StaticActionBarItem(String action, String name, String label, String styleName, String icon) {
        this.action = action;
        this.styleName = styleName;
        this.label = label;
        this.name = name;
        this.xorder = xorder;
        this.icon = icon;
    }

    @Override
    public ILinkable getXParent() {
        return null;
    }

    @Override
    public boolean hasXParent() {
        return false;
    }

    @Override
    public ILinkable getXChild() {
        return null;
    }

    @Override
    public boolean hasXChild() {
        return false;
    }

    @Override
    public DespContentTypes getContentType() {
        return null;
    }

    @Override
    public Integer getOid() {
        return null;
    }

    @Override
    public String getObjectLink() {
            return this.action;
    }
}
