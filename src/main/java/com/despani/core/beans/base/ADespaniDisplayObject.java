package com.despani.core.beans.base;

import com.despani.core.beans.enums.DespContentTypes;
import com.despani.core.interfaces.ILinkable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class ADespaniDisplayObject extends ADespaniObject implements ILinkable {

    protected int access; // int(10) UNSIGNED NOT NULL DEFAULT 0

    @Setter @Getter
    private ADespaniDisplayObject parent;
    @Setter @Getter
    private ADespaniDisplayObject child;

    protected ADespaniDisplayObject() {
    }

    public ADespaniDisplayObject( ADespaniDisplayObject parent,ADespaniDisplayObject child) {
        this.parent = parent;
        this.child = child;
    }

    public ADespaniDisplayObject( ADespaniDisplayObject parent) {

        this.parent = parent;
    }

    public void updateParent(ADespaniDisplayObject parent) {
        this.parent = parent;
    }

    public void updateChild(ADespaniDisplayObject child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) ;
    }

    @Override
    public ILinkable getXParent() {
        return parent;
    }

    @Override
    public boolean hasXParent() {
        return parent != null;
    }

    @Override
    public ILinkable getXChild() {
        return child;
    }

    @Override
    public boolean hasXChild() {
        return child != null;
    }

    @Override
    public DespContentTypes getContentType() {
        return null;
    }
}
