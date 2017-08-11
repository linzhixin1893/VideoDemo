package com.xgimi.videodemo.presenter;

import android.media.MediaPlayer;
import android.media.TimedText;

import com.xgimi.videodemo.model.IVideoPlayView;
import com.xgimi.videodemo.bean.Video;
import com.xgimi.videodemo.bean.VideoDurationInfo;
import com.xgimi.videodemo.model.VideoPlayView;
import com.xgimi.videodemo.view.IMainView;

/**
 * Created by XGIMI on 2017/8/9.
 */

public class VideoPresenterImp implements IVideoPresenter {

    private IMainView mMainView;
    private IVideoPlayView mVideoView;

    private VideoPlayView.OnVideoPlayerListener mOnVideoPlayerListener = new VideoPlayView.OnVideoPlayerListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public void onComplete(MediaPlayer mp) {

        }

        @Override
        public void onError(MediaPlayer mp, int what, int extra) {

        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }

        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {

        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public void onInfo(MediaPlayer mp, int what, int extra) {

        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    };


    public VideoPresenterImp(IMainView activity, IVideoPlayView videoPlayView, Video video) {
        this.mMainView = activity;
        this.mVideoView = videoPlayView;
        mVideoView.setVideoListener(mOnVideoPlayerListener);
        mVideoView.setVideo(video);
    }

    @Override
    public void onKeyLeft() {
        VideoDurationInfo durationInfo = mVideoView.getDurationInfo();
        int wantPosition = durationInfo.getCurrentPostion() - 10000;
        if (wantPosition > 0) {
            mVideoView.seekTo(wantPosition);
        }
    }

    @Override
    public void onKeyRight() {
        VideoDurationInfo durationInfo = mVideoView.getDurationInfo();
        int wantPosition = durationInfo.getCurrentPostion() + 10000;
        if (wantPosition < durationInfo.getTotalDuration()) {
            mVideoView.seekTo(wantPosition);
        }
    }

    @Override
    public void onKeyMenu() {

    }

    @Override
    public void onKeyEnter() {
        mVideoView.playOrPause();
    }

    @Override
    public void onKeyHome() {

    }

    @Override
    public void quit() {
        mVideoView.stop();
    }
}
