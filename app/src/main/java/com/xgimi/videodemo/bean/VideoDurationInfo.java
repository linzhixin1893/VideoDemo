package com.xgimi.videodemo.bean;

/**
 * Created by linzhixin on 2017/8/9.
 */

public class VideoDurationInfo {
    private int currentPostion;
    private int totalDuration;

    public VideoDurationInfo() {
    }

    public VideoDurationInfo(int currentPostion, int totalDuration) {
        this.currentPostion = currentPostion;
        this.totalDuration = totalDuration;
    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    public void setCurrentPostion(int currentPostion) {
        this.currentPostion = currentPostion;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
