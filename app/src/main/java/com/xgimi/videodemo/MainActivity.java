package com.xgimi.videodemo;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xgimi.videodemo.utils.XGIMILOG;
import com.xgimi.videodemo.model.IVideoPlayView;
import com.xgimi.videodemo.bean.Video;
import com.xgimi.videodemo.model.VideoPlayView;
import com.xgimi.videodemo.presenter.IVideoPresenter;
import com.xgimi.videodemo.presenter.VideoPresenterImp;
import com.xgimi.videodemo.view.IMainView;

import java.io.File;

public class MainActivity extends Activity implements IMainView{

    private IVideoPlayView mVideoPlayView;
    private IVideoPresenter mVideoPresenter;
    private Video mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGIMILOG.initTag("NewVideoPlayer", true);
        setContentView(R.layout.activity_main);
        initVideo();
        initView();
        initPresenter();
    }

    private void initVideo() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/XgimiVideo/video_2D.mp4";
        XGIMILOG.E(path);
        File file = new File(path);
        XGIMILOG.E(file.exists() + "");
        mVideo = new Video();
        mVideo.setAbsPath(path);
        mVideo.setName("测试");
        mVideo.setFormat("mp4");
        mVideo.setSeekAble(true);
    }

    private void initPresenter() {
        mVideoPresenter = new VideoPresenterImp(this, mVideoPlayView, mVideo);
    }

    private void initView() {
        mVideoPlayView = (VideoPlayView) findViewById(R.id.view_video_play_view_main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mVideoPresenter.onKeyLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mVideoPresenter.onKeyRight();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                mVideoPresenter.onKeyEnter();
                break;
            case KeyEvent.KEYCODE_MENU:
                mVideoPresenter.onKeyMenu();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
