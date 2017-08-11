package com.xgimi.videodemo.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yub on 2017/6/9.
 */

public class BroadCastUitls {

    public static final String PREPARING = "preparing";//准备中
    public static final String PLAYING = "playing";//播放中
    public static final String PAUSING = "pausing";//暂停中
    public static final String BUFFERING = "buffering";//缓冲中
    public static final String BUFFER_END = "buffer_end";//缓冲完成
    public static final String SWITCHING = "switching";//切换中
    public static final String SEEKING = "seeking";//seek中
    public static final String SEEK_COMPLETE = "seek_complete";//seek完成
    public static final String STOPED = "stoped";//播放停止
    public static final String USER_GOING_TO_SEEK = "user_going_to_seek";//用户操作左右键

    /**
     * 播放视频时暂停播放音乐
     * @param context
     */
    public static void sendPauseMusic(Context context){
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        context.sendBroadcast(i);
    }
}
