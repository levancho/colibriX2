package com.despani.core.beans.domains;

import com.despani.core.beans.DespNodeType;
import com.despani.core.beans.base.ADespNodeItem;
import com.despani.core.beans.enums.DespContentTypes;

public class DespMenuItem extends ADespNodeItem  {

    private DespNodeType menuType;

    private String link;
    private String img;
    private String path;
    private Short level;

    private String label="Menu";

    public DespMenuItem() {
        super();
    }

    public static DespMenuItem getRoot() {
        DespMenuItem ni = new DespMenuItem();
        ni.setOid(1);
        ni.setParent_oid(0);
        ni.setTitle("root");
        return ni;
    }


    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.MENU;
    }


    public DespNodeType getMenuType() {
        return menuType;
    }

    public void setMenuType(DespNodeType menuType) {
        this.menuType = menuType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }



    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }




}