package com.xgimi.videodemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xgimi.ui.utils.XGIMILOG;
import com.xgimi.videodemo.menu.XgimiMenu;
import com.xgimi.videodemo.menu.XgimiMenuView;
//import com.xgimi.ui.view.menu.XgimiMenu;
//import com.xgimi.ui.view.menu.XgimiMenuView;

/**
 * Created by XGIMI on 2017/8/10.
 */

public class TestActivity extends Activity {

    private RelativeLayout mRlTest;
    private XgimiMenuView mMenuView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        mRlTest = (RelativeLayout) findViewById(R.id.rl_test);
        XgimiMenu xgimiMenu = new XgimiMenu();
        xgimiMenu.addMenu(new XgimiMenu.Menu("菜单1", R.drawable.ic_test_icon));
        xgimiMenu.addMenu(new XgimiMenu.Menu("菜单2", R.drawable.ic_test_icon));
        xgimiMenu.addMenu(new XgimiMenu.Menu("菜单3", R.drawable.ic_test_icon));
        xgimiMenu.addMenu(new XgimiMenu.Menu("菜单4", R.drawable.ic_test_icon));
        xgimiMenu.addMenu(new XgimiMenu.Menu("菜单5", R.drawable.ic_test_icon));
        XgimiMenu.Menu menu6 = new XgimiMenu.Menu("菜单6",R.drawable.ic_test_icon);
        XgimiMenu.Menu menuSub = new XgimiMenu.Menu("子菜单5", false);
        menuSub.setType(XgimiMenu.Type_IntBox);
        menu6.addSubMenu(new XgimiMenu.Menu("子菜单1", 0, 10, 5));
        menu6.addSubMenu(new XgimiMenu.Menu("子菜单2", false));
        menu6.addSubMenu(new XgimiMenu.Menu("子菜单3", false));
        menu6.addSubMenu(new XgimiMenu.Menu("子菜单4", false));
        menu6.addSubMenu(menuSub);
        xgimiMenu.addMenu(menu6);
        mMenuView = new XgimiMenuView(this, xgimiMenu, mRlTest);
        mMenuView.setOnMenuListener(new XgimiMenuView.OnMenuListener() {
            @Override
            public void OnMenuClicked(XgimiMenu.Menu menu) {
                XGIMILOG.D("");
            }

            @Override
            public void OnSubMenuClicked(XgimiMenu.Menu subMenu, int position) {
                XGIMILOG.D("");
            }

            @Override
            public void OnMenuValueChanged(XgimiMenu.Menu menu) {
                XGIMILOG.D("");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                if (mMenuView.getVisibility() == View.VISIBLE) {
                    mMenuView.back();
                } else {
                    mMenuView.show();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
