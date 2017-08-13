package com.xgimi.xgimimenutest.menu;

import com.xgimi.xgimimenutest.R;

import java.util.ArrayList;

/**
 * Created by linzhixin on 2017/8/13.
 */

public class ImageModeMenuManager {

    private static XgimiMenu.Menu mImageModeMenu;
    private static ImageModeMenuManager mManager;

    public synchronized static ImageModeMenuManager getInstance() {
        if (mManager == null) {
            mManager = new ImageModeMenuManager();
        }
        return mManager;
    }

    private ImageModeMenuManager() {
        mImageModeMenu = new XgimiMenu.Menu("图像模式", R.drawable.ic_play_mode);
        XgimiMenu.Menu subQita = new XgimiMenu.Menu("其他");
        subQita.setType(XgimiMenu.Type_CheckBox);

        XgimiMenu.Menu subUserDefine = new XgimiMenu.Menu("自定义");
        subUserDefine.setType(XgimiMenu.Type_CheckBox);

        ArrayList<String> modeList = new ArrayList<>();
        modeList.add("用户");
        modeList.add("暖");
        modeList.add("冷");
        modeList.add("标准");
        XgimiMenu.Menu subImageTempMode = new XgimiMenu.Menu("模式", modeList, "用户");
        subImageTempMode.setType(XgimiMenu.Type_TextBox);
        XgimiMenu.Menu subRedValue = new XgimiMenu.Menu("红", 0, 100, 50);
        subRedValue.setType(XgimiMenu.Type_IntBox);
        XgimiMenu.Menu subGreenValue = new XgimiMenu.Menu("绿", 0, 100, 50);
        subGreenValue.setType(XgimiMenu.Type_IntBox);
        XgimiMenu.Menu subBlueValue = new XgimiMenu.Menu("蓝", 0, 100, 50);
        subBlueValue.setType(XgimiMenu.Type_IntBox);

        XgimiMenu.Menu subOther = new XgimiMenu.Menu("测试");
        subOther.setType(XgimiMenu.Type_CheckBox);
        subUserDefine.addSubMenu(subOther);
        subUserDefine.addSubMenu(subImageTempMode);
        subUserDefine.addSubMenu(subRedValue);
        subUserDefine.addSubMenu(subGreenValue);
        subUserDefine.addSubMenu(subBlueValue);
        mImageModeMenu.addSubMenu(subQita);
        mImageModeMenu.addSubMenu(subUserDefine);
    }

    public XgimiMenu.Menu getMenu() {
        return mImageModeMenu;
    }
}
