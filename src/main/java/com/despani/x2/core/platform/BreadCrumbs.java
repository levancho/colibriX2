package com.despani.x2.core.platform;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class BreadCrumbs {

    private LinkedList<Crumb> list = new LinkedList<Crumb>();



    public void push(String text, String url) {
        this.list.add(new Crumb(text, url));
    }


    public Crumb pop() {
        if (this.list.size() == 0) {
            throw new NoSuchElementException();
        }
        return this.list.removeLast();
    }


    public List<Crumb> getList() {
        return Collections.unmodifiableList(this.list);
    }


    public int size() { return this.list.size();}




    public String toString(String crumb) {
        return toString(crumb, "|");
    }


    public String toString(String crumb, String sep) {
        StringBuilder buf = new StringBuilder();
        boolean addSep = false;
        for (Crumb c : this.list) {
            if (addSep) {
                buf.append(sep);
            }
            buf.append(c);
            addSep = true;
        }
        return buf.toString();
    }



    public static class Crumb {

        private String text;
        private String url;

        public Crumb(String text, String url) {
            this.text = text;
            this.url = url;
        }

        public String getText() { return this.text;}
        public String getUrl() { return this.url;}

    }

}