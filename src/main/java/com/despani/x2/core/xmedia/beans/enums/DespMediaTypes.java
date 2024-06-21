package com.despani.x2.core.xmedia.beans.enums;


import com.despani.x2.core.interfaces.IDespCodeEnum;

import java.util.*;


public enum DespMediaTypes implements IDespCodeEnum {

    WEBP(1, "image/webp"),
    JPEG(2, "image/jpeg"),
    PNG(3, "image/png"),
    JPG(4, "image/jpg"),
    MOV(5, "video/quicktime"),
    MP4(6, "video/mp4"),
    MP3(7,"audio/mp3"),
    OGG(8,"video/ogg");



    private static final Map<String, DespMediaTypes> lookup = new HashMap<String, DespMediaTypes>();
    static {
        for ( DespMediaTypes s : EnumSet.allOf(DespMediaTypes.class) ) {
            lookup.put(s.getValue(), s);
        }
    }
    public static DespMediaTypes get(String value) {
        return lookup.get(value);
    }

    private int code;
    public int getCode() {
        return code;
    }

    private String value;
    public String getValue() {
        return value;
    }

    private DespMediaTypes(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    public static Collection<String> getItems() {
        DespMediaTypes[] values = DespMediaTypes.values();
        ArrayList<String> items = new ArrayList<>(values.length);
        for ( DespMediaTypes type : values ) {
            items.add(new String(type.value));
        }
        return items;
    }
}
