package com.xgimi.videodemo.model;

import com.xgimi.videodemo.bean.Video;
import com.xgimi.videodemo.bean.VideoDurationInfo;

/**
 * Created by XGIMI on 2017/8/9.
 */

public interface IVideoPlayView {

    void setVideo(Video video);

    void openPlayer();

    void setVideoListener(VideoPlayView.OnVideoPlayerListener listener);

    void play();

    void pause();

    void stop();

    int playOrPause();

    void seekTo(int position);

    //获取当前进度和总时长
    VideoDurationInfo getDurationInfo();

    Video getVideo();

    int getPlayerState();
}
