package com.xgimi.videodemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XGIMI on 2017/8/9.
 */

public class Video {

    private int totleDuration;
    private String absPath;
    private String name;
    private boolean isSeekAble;
    private String format;
    private List<Video> videoInParentFileList;

    public Video() {
    }

    public int getTotleDuration() {
        return totleDuration;
    }

    public void setTotleDuration(int totleDuration) {
        this.totleDuration = totleDuration;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSeekAble() {
        return isSeekAble;
    }

    public void setSeekAble(boolean seekAble) {
        isSeekAble = seekAble;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Video> getVideoInParentFileList() {
        if (videoInParentFileList == null) {
            videoInParentFileList = new ArrayList<>();
        }
        return videoInParentFileList;
    }

    public void setVideoInParentFileList(List<Video> videoInParentFileList) {
        this.videoInParentFileList = videoInParentFileList;
    }
}
