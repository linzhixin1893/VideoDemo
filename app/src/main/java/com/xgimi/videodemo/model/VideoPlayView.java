package com.xgimi.videodemo.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.gson.Gson;

import com.mstar.android.media.MMediaPlayer;
import com.xgimi.videodemo.utils.XGIMILOG;
import com.xgimi.videodemo.bean.Video;
import com.xgimi.videodemo.bean.VideoDurationInfo;
import com.xgimi.videodemo.utils.BroadCastUitls;
import com.xgimi.videodemo.utils.CommonUtil;

import java.io.IOException;


/**
 * Created by yub on 2017/6/9.
 */

public class VideoPlayView extends SurfaceView implements IVideoPlayView {
    private static final String TAG = "VideoPlayView";

    //播放器状态
    public static final int PLAYER_GOTO_PLAY = 0;
    public static final int PLAYER_GOTO_PAUSE = 1;
    public static final int PLAYER_NULL = 2;

    public static final int PLAYER_STATE_PREPARING = 3;//准备中
    public static final int PLAYER_STATE_PREPARED = 4;//准备完成
    public static final int PLAYER_STATE_COMPLETE = 5;//播放完成
    public static final int PLAYER_STATE_SEEKING = 6;//跳转进度中
    public static final int PLAYER_STATE_SEEK_COMPLETE = 7;//跳转完成
    public static final int PLAYER_STATE_PLAYING = 8;//播放中
    public static final int PLAYER_STATE_PAUSING = 9;//暂停中
    public static final int PLAYER_STATE_STOPED = 10;//播放停止
    public static final int PLAYER_STATE_ERROR = 11;//出错


    private Context mContext;
    private int mVideoWidth;
    private int mVideoHeight;
    private MMediaPlayer mMMediaPlayer;
    private Video mVideo;
    private OnVideoPlayerListener mVideoPlayerListener;
    private int mCurrentPlayerState;

    private SurfaceHolder mSurfaceHolder;
    private SurfaceHolder.Callback mSurfaceHolderCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            openPlayer();
            XGIMILOG.E("current state : " + getStateString() );
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceHolder = holder;
            XGIMILOG.E("current state : " + getStateString() );
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceHolder = null;
            stop();
            XGIMILOG.E("current state : " + getStateString() );
        }
    };

    public VideoPlayView(Context context) {
        super(context);
        initVideoView();
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    private void initVideoView() {
        mContext = getContext();
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSurfaceHolderCallBack);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    public void setVideo(Video video) {
        this.mVideo = video;
        mCurrentPlayerState = PLAYER_STATE_PREPARING;
        openPlayer();
        requestLayout();
        invalidate();
    }

    @Override
    public void openPlayer() {
        XGIMILOG.E("current state : " + getStateString() + ", video : " + new Gson().toJson(mVideo));
        if (mVideo == null || mVideo.getAbsPath() == null || "".equals(mVideo.getAbsPath()) || mSurfaceHolder == null) {
            return;
        }
        BroadCastUitls.sendPauseMusic(mContext);
        try {
            mMMediaPlayer = new MMediaPlayer();
            mMMediaPlayer.setDataSource(mContext, Uri.parse(mVideo.getAbsPath()));
            mMMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mMMediaPlayer.setOnTimedTextListener(mOnTimedTextListener);
            }
            mMMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            if (mSurfaceHolder != null) {
                mMMediaPlayer.setDisplay(mSurfaceHolder);
            }
            mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMMediaPlayer.setScreenOnWhilePlaying(true);
            mMMediaPlayer.prepareAsync();
        } catch (IOException e) {
//            log("openPlayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setVideoListener(OnVideoPlayerListener listener) {
        this.mVideoPlayerListener = listener;
    }

    @Override
    public void play() {
        if (mMMediaPlayer != null && !mMMediaPlayer.isPlaying()) {
            mMMediaPlayer.start();
            mCurrentPlayerState = PLAYER_STATE_PLAYING;
        }
        XGIMILOG.E("current state : " + getStateString());
    }

    @Override
    public void pause() {
        if (mMMediaPlayer != null && mMMediaPlayer.isPlaying()) {
            mMMediaPlayer.pause();
            mCurrentPlayerState = PLAYER_STATE_PAUSING;
        }
        XGIMILOG.E("current state : " + getStateString());
    }

    @Override
    public void stop() {
        mCurrentPlayerState = PLAYER_STATE_STOPED;
        synchronized (this) {
            if (mMMediaPlayer != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mMMediaPlayer.isPlaying()) {
                                XGIMILOG.E("current state : " + getStateString() + ", start to stop");
                                mMMediaPlayer.stop();
                                XGIMILOG.E("current state : " + getStateString() + ", end to stop");
                            }
                            XGIMILOG.E("current state : " + getStateString() + ", start to release");
                            mMMediaPlayer.release();
                            XGIMILOG.E("current state : " + getStateString() + ", end to release");
                            mMMediaPlayer = null;
                        } catch (Exception e) {
                            XGIMILOG.E("current state : " + getStateString() + ", " + e.getMessage());
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public int playOrPause() {
        int state = PLAYER_NULL;
        if (mMMediaPlayer != null) {
            if (mMMediaPlayer.isPlaying()) {
                mMMediaPlayer.pause();
                mCurrentPlayerState = PLAYER_STATE_PAUSING;
                state = PLAYER_GOTO_PAUSE;
            } else {
                mMMediaPlayer.start();
                mCurrentPlayerState = PLAYER_STATE_PLAYING;
                state = PLAYER_GOTO_PLAY;
            }
        }
        XGIMILOG.E("current state : " + getStateString() + ", playOrPause");
        return state;
    }

    @Override
    public void seekTo(int position) {
        if (playerCanOperate()) {
            if (mCurrentPlayerState == PLAYER_STATE_PAUSING) {
                play();
            }
            mCurrentPlayerState = PLAYER_STATE_SEEKING;
            mMMediaPlayer.seekTo(position);
            XGIMILOG.E("current state : " + getStateString() + "seeking to " + CommonUtil.formatDuration(position));
        }
    }

    @Override
    public VideoDurationInfo getDurationInfo() {
        if (playerCanOperate()) {
            return new VideoDurationInfo(mMMediaPlayer.getCurrentPosition(), mMMediaPlayer.getDuration());
        }
        return null;
    }

    @Override
    public Video getVideo() {
        return mVideo;
    }

    @Override
    public int getPlayerState() {
        return mCurrentPlayerState;
    }

    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            mCurrentPlayerState = PLAYER_STATE_SEEK_COMPLETE;
            XGIMILOG.E("current state : " + getStateString() + ", onSeekComplete");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onSeekComplete(mp);
            }
        }
    };
    private MediaPlayer.OnTimedTextListener mOnTimedTextListener = new MediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onTimedText(mp, text);
            }
        }
    };
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            XGIMILOG.E("current state : " + getStateString() + "percent = " + percent + "%");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onBufferingUpdate(mp, percent);
            }
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mCurrentPlayerState = PLAYER_STATE_PREPARED;
            XGIMILOG.E("current state : " + getStateString());
            play();
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onPrepared(mp);
            }
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mCurrentPlayerState = PLAYER_STATE_COMPLETE;
            XGIMILOG.E("current state : " + getStateString() + ", onCompletion");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onComplete(mp);
            }
        }
    };

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mCurrentPlayerState = PLAYER_STATE_ERROR;
            XGIMILOG.E("current state : " + getStateString() + "， what = " + what + ", extra = " + extra);
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onError(mp, what, extra);
            }
            return false;
        }
    };

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            XGIMILOG.E("current state : " + getStateString() + "， what = " + what + ", extra = " + extra);
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onInfo(mp, what, extra);
            }
            return false;
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mVideoWidth = width;
            mVideoHeight = height;
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoSizeChanged(mp, width, height);
            }
        }
    };


    public interface OnVideoPlayerListener {
        void onPrepared(MediaPlayer mp);

        void onComplete(MediaPlayer mp);

        void onError(MediaPlayer mp, int what, int extra);

        void onBufferingUpdate(MediaPlayer mp, int percent);

        void onTimedText(MediaPlayer mp, TimedText text);

        void onSeekComplete(MediaPlayer mp);

        void onInfo(MediaPlayer mp, int what, int extra);

        void onVideoSizeChanged(MediaPlayer mp, int width, int height);
    }

    private String getStateString() {
        String state = "";
        switch (mCurrentPlayerState) {
            case PLAYER_NULL:
                state = "null";
                break;
            case PLAYER_STATE_COMPLETE:
                state = "complete";
                break;
            case PLAYER_STATE_PAUSING:
                state = "pausing";
                break;
            case PLAYER_STATE_PLAYING:
                state = "playing";
                break;
            case PLAYER_STATE_PREPARING:
                state = "preparing";
                break;
            case PLAYER_STATE_PREPARED:
                state = "prepared";
                break;
            case PLAYER_STATE_SEEKING:
                state = "seeking";
                break;
            case PLAYER_STATE_SEEK_COMPLETE:
                state = "seek complete";
                break;
            case PLAYER_STATE_STOPED:
                state = "stoped";
                break;
            case PLAYER_STATE_ERROR:
                state = "error";
                break;
        }
        return state;
    }

    private boolean playerCanOperate() {
        if (mMMediaPlayer != null && mCurrentPlayerState != PLAYER_STATE_PREPARING && mCurrentPlayerState != PLAYER_STATE_COMPLETE && mCurrentPlayerState != PLAYER_STATE_ERROR) {
            return true;
        } else {
            return false;
        }
    }
}
