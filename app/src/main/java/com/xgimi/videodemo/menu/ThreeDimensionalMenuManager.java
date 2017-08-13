package com.xgimi.xgimimenutest.menu;

import com.xgimi.xgimimenutest.R;

/**
 * Created by linzhixin on 2017/8/13.
 */

public class ThreeDimensionalMenuManager {

    public static final int THREE_DIMENSIONAL_CLOSE = 0;
    public static final int THREE_DIMENSIONAL_HORIZONTAL = 1;
    public static final int THREE_DIMENSIONAL_VERTICAL = 2;
    public static final int THREE_DIMENSIONAL_OTHER = 3;

    private int mCurrentThreeDimensionalState = THREE_DIMENSIONAL_CLOSE;

    private static XgimiMenu.Menu m3DMenu;
    private static ThreeDimensionalMenuManager mManager;

    public synchronized static ThreeDimensionalMenuManager getInstance() {
        if (mManager == null) {
            mManager = new ThreeDimensionalMenuManager();
        }

        return mManager;
    }

    private ThreeDimensionalMenuManager() {
        m3DMenu = new XgimiMenu.Menu("3D模式", R.drawable.ic_setting_3d);
        XgimiMenu.Menu subMenuClose3D = new XgimiMenu.Menu("关");
        subMenuClose3D.setType(XgimiMenu.Type_CheckBox);
        XgimiMenu.Menu subMenuZuoyou = new XgimiMenu.Menu("左右");
        subMenuZuoyou.setType(XgimiMenu.Type_CheckBox);
        XgimiMenu.Menu subMenuShangxia = new XgimiMenu.Menu("上下");
        subMenuShangxia.setType(XgimiMenu.Type_CheckBox);
        XgimiMenu.Menu subMenuQita = new XgimiMenu.Menu("其他");
        subMenuQita.setType(XgimiMenu.Type_CheckBox);

        switch (mCurrentThreeDimensionalState) {
            case THREE_DIMENSIONAL_CLOSE:
                subMenuClose3D.setSelected(true);
                subMenuQita.setSelected(false);
                subMenuShangxia.setSelected(false);
                subMenuZuoyou.setSelected(false);
                break;
            case THREE_DIMENSIONAL_HORIZONTAL:
                subMenuClose3D.setSelected(false);
                subMenuQita.setSelected(false);
                subMenuShangxia.setSelected(false);
                subMenuZuoyou.setSelected(true);
                break;
            case THREE_DIMENSIONAL_VERTICAL:
                subMenuClose3D.setSelected(false);
                subMenuQita.setSelected(false);
                subMenuShangxia.setSelected(true);
                subMenuZuoyou.setSelected(false);
                break;
            case THREE_DIMENSIONAL_OTHER:
                subMenuClose3D.setSelected(false);
                subMenuQita.setSelected(true);
                subMenuShangxia.setSelected(false);
                subMenuZuoyou.setSelected(false);
                break;
        }

        m3DMenu.addSubMenu(subMenuClose3D);
        m3DMenu.addSubMenu(subMenuZuoyou);
        m3DMenu.addSubMenu(subMenuShangxia);
        m3DMenu.addSubMenu(subMenuQita);
    }

    public int getCurrentThreeDimensionalState() {
        return mCurrentThreeDimensionalState;
    }

    public void setCurrentThreeDimensionalState(int mCurrentThreeDimensionalState) {
        this.mCurrentThreeDimensionalState = mCurrentThreeDimensionalState;
    }

    public XgimiMenu.Menu getMenu() {
        return m3DMenu;
    }
}
