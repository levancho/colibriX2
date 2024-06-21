package com.despani.x2.contents.beans;

import com.despani.x2.contents.interfaces.ITag;
import lombok.Data;

@Data
public class VideoTag implements ITag {

    private String type;
    private String value;

    private int width=600;
    private int height=480;
    private int autoplay;

    private String playerControls;
    private String siteurl;
    private String folder;
    private String fileExt;

    public VideoTag(String content) {
        this.value=content;
    }



}
