package com.xgimi.videodemo.menu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgimi.ui.utils.XGIMILOG;
import com.xgimi.videodemo.R;


import java.util.ArrayList;

public class XgimiMenuView extends RelativeLayout {

    public interface OnMenuListener {
        void OnMenuClicked(XgimiMenu.Menu menu);

        void OnSubMenuClicked(XgimiMenu.Menu subMenu, int position);

        void OnMenuValueChanged(XgimiMenu.Menu menu);
    }

    private OnMenuListener listener = null;

    public static int LayoutWidth = 500;
    public static int MenuItemHeight = 80;
    public static int ID = 0x1314;
    public static int ID_LISTVIEW = 0x0001;
    public static int ID_SUBLISTVIEW = 0x0002;
    public static int ID_SUBSUBLISTVIEW = 0x0003;

    public int NothingView = -1;
    public int MainMenuView = 1;
    public int SubMenuView = 2;
    public int SubSubMenuView = 3;
    public int CurrentView = NothingView;

    private XgimiMenu xgMenu;
    private ListView listView;
    public ListView subListView;
    public ListView subSubListView;
    private Context context;
    private ImageView moveImageView;
    private ImageView subMoveImageView;
    private TextView titleTextView;
    private View hideItem;
    private View subHideItem;

    private XgimiMenuAdapter.MenuAdapter menuAdapter;
    public XgimiMenuAdapter.SubMenuAdapter subMenuAdapter;
    public XgimiMenuAdapter.SubSubMenuAdapter subSubMenuAdapter;
    private int[] fromRect = new int[2];
    private int[] subFromRect = new int[2];
    private String TAG="subSubListView";



    public XgimiMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public XgimiMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public XgimiMenuView(Context context, XgimiMenu xgMenu, ViewGroup container) {
        super(context);
        this.context = context;
        this.xgMenu = xgMenu;
        initView();
        setId(ID);

        LayoutParams pm = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        pm.leftMargin = -100;//���⶯�����ټƳ��ʱ�����հ�
        pm.addRule(RelativeLayout.CENTER_VERTICAL);
        container.addView(this, pm);

        this.setVisibility(View.GONE);
    }
    


    
    public void update() {
        menuAdapter.notifyDataSetChanged();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < xgMenu.menus.get(0).subMenus.size(); i++) {
                    XgimiMenu.Menu menu = xgMenu.menus.get(0).subMenus.get(i);
                    if (menu.type == XgimiMenu.Type_CheckBox) {
                        if (menu.selected == true) {
                            listView.setSelection(i + 1);
                            listViewItemSelected.onItemSelected(null, menuAdapter.getViewAtPosition(i + 1), i + 1, menuAdapter.getItemId(i + 1));
                        }
                    }
                }
            }
        }, 0);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPosition(0);
            }
        }, 500);
    }

    private int getDensityPX(float data) {
        return (int) (data * context.getResources().getDisplayMetrics().density);

    }

    @SuppressLint("NewApi")
    private void initView() {
        this.setBackgroundResource(R.drawable.ic_bg_setting);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams lpm = new LayoutParams(
                getDensityPX(LayoutWidth),
                getDensityPX(MenuItemHeight * 6 - 25));
        lpm.addRule(CENTER_VERTICAL);
        lpm.leftMargin = 100;//���⶯�����ټƳ��ʱ�����հ�
        this.addView(relativeLayout, lpm);

        titleTextView = new TextView(context);
        titleTextView.setBackgroundColor(Color.parseColor("#00000000"));
        titleTextView.setAlpha(0.0f);
        titleTextView.setId(0x11111);
        titleTextView.setFocusable(false);
        titleTextView.setFocusableInTouchMode(false);
        titleTextView.setText(" ");
        titleTextView.setGravity(Gravity.CENTER_VERTICAL);
        titleTextView.setPadding(0, 0, 0, 0);
        titleTextView.setTextSize(27);
        titleTextView.setTextColor(Color.WHITE);
        int titleTextViewHeight = MenuItemHeight ;
//        if (Device.is4kjiguang()) {
//        	titleTextViewHeight = MenuItemHeight +100 ;
//		}
        LayoutParams tpm = new LayoutParams(
                LayoutParams.MATCH_PARENT
                , titleTextViewHeight );
//        tpm.leftMargin = 20;
        relativeLayout.addView(titleTextView, tpm);

        listView = new ListView(context);

        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setId(ID_LISTVIEW);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setBackgroundColor(Color.parseColor("#00000000"));
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(listViewItemClicked);
        listView.setOnItemSelectedListener(listViewItemSelected);
        listView.setDivider(context.getResources().getDrawable(R.drawable.ic_setting_left_a));
        listView.setDividerHeight(1);
        listView.setSmoothScrollbarEnabled(true);
        listView.setVerticalFadingEdgeEnabled(true);
        listView.setFadingEdgeLength(MenuItemHeight);
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

//		ArrayList<XgimiMenu.Menu> menuList = new ArrayList<XgimiMenu.Menu>();
//		for(int i = 0; i < xgMenu.menus.size(); i++)
//		{
//			menuList.add(xgMenu.menus.get(i));
//			menuList.addAll(xgMenu.menus.get(i).subMenus);
//		}
        menuAdapter = new XgimiMenuAdapter.MenuAdapter(context, xgMenu.menus, new XgimiMenuAdapter.BaseMenuAdapter.OnGetViewSave() {
            @Override
            public void onSaveView(int position, View view) {
                if (listView.getChildCount() == position) {
                    menuAdapter.setViewAtPostion(position, view);
                }
            }
        });
        listView.setAdapter(menuAdapter);

        LayoutParams pm = new LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT);
        pm.addRule(RelativeLayout.BELOW, titleTextView.getId());
        relativeLayout.addView(listView, pm);

        subListView = new ListView(context);
        subListView.setId(ID_SUBLISTVIEW);
        subListView.setAlpha(0.0f);
        subListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        subListView.setBackgroundColor(Color.parseColor("#00000000"));
        subListView.setVerticalScrollBarEnabled(false);
        subListView.setOnItemSelectedListener(subListViewItemSelected);
        subListView.setOnItemClickListener(subListViewItemClicked);
        subListView.setDivider(null);
        subListView.setItemsCanFocus(false);
        subListView.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);

        subMenuAdapter = new XgimiMenuAdapter.SubMenuAdapter(context, xgMenu.menus.get(0).subMenus);
        subListView.setAdapter(subMenuAdapter);
        subMenuAdapter.listView = subListView;
        LayoutParams spm = new LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
        spm.addRule(RelativeLayout.ALIGN_TOP, getDensityPX(100));
        spm.addRule(RelativeLayout.BELOW, titleTextView.getId());
        relativeLayout.addView(subListView, spm);

        subSubListView = new ListView(context);
        subSubListView.setId(ID_SUBSUBLISTVIEW);
        subSubListView.setAlpha(0.0f);
        subSubListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        subSubListView.setBackgroundColor(Color.parseColor("#00000000"));
        subSubListView.setVerticalScrollBarEnabled(false);
        subSubListView.setOnItemSelectedListener(subSubListViewItemSelected);
//		subSubListView.setOnItemClickListener(subSubListViewItemClicked);
        subSubListView.setDivider(null);
        subSubListView.setItemsCanFocus(false);
        subSubListView.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);

        ArrayList<XgimiMenu.Menu> ssmenus = new ArrayList<XgimiMenu.Menu>();
        subSubMenuAdapter = new XgimiMenuAdapter.SubSubMenuAdapter(context, ssmenus);
        subSubListView.setAdapter(subSubMenuAdapter);
        subSubMenuAdapter.listView = subSubListView;
        LayoutParams sspm = new LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
        sspm.addRule(RelativeLayout.ALIGN_TOP, getDensityPX(100));
        sspm.addRule(RelativeLayout.BELOW, titleTextView.getId());
        relativeLayout.addView(subSubListView, sspm);

        moveImageView = new ImageView(context);
        moveImageView.setAlpha(0.0f);
        moveImageView.setFocusable(false);
        moveImageView.setBackgroundColor(Color.parseColor("#00000000"));
        LayoutParams ipm = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
//		ipm.addRule(RelativeLayout.BELOW, titleTextView.getId());
        ipm.topMargin = getDensityPX(50);
        ipm.setMargins(-3, 0, 0, 0);
        relativeLayout.addView(moveImageView, ipm);

        subMoveImageView = new ImageView(context);
        subMoveImageView.setAlpha(0.0f);
        subMoveImageView.setFocusable(false);
        subMoveImageView.setBackgroundColor(Color.parseColor("#00000000"));
        LayoutParams sipm = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
//		ipm.addRule(RelativeLayout.BELOW, titleTextView.getId());
        sipm.topMargin = getDensityPX(50);
        sipm.setMargins(-3, 0, 0, 0);
        relativeLayout.addView(subMoveImageView, sipm);
    }

    public void setOnMenuListener(OnMenuListener lis) {
        listener = lis;
        menuAdapter.setOnMenuListener(lis);
        subMenuAdapter.setOnMenuListener(lis);
    }

    @SuppressLint("NewApi")
    public void show() {
        if (CurrentView == MainMenuView) {
            return;
        }
        CurrentView = MainMenuView;
        this.setVisibility(View.VISIBLE);
        this.requestFocus();
        subListView.setFocusable(false);
        subListView.setFocusableInTouchMode(false);

        AnimationSet set = new AnimationSet(true);
        TranslateAnimation t = new TranslateAnimation(-getDensityPX(LayoutWidth), 0, 0, 0);
        OvershootInterpolator bounce = new OvershootInterpolator();
//		BounceInterpolator bounce = new BounceInterpolator();
        set.setInterpolator(bounce);
        t.setDuration(500);
        set.addAnimation(t);

        AlphaAnimation a = new AlphaAnimation(0, 1);
        a.setDuration(500);
        set.addAnimation(a);
        this.startAnimation(set);
        listView.bringToFront();
        listView.requestFocus();
    }

    @SuppressLint("NewApi")
    public void back() {
        if (listView.getAlpha() == 1) {
            this.hide();
        } else {
            if (CurrentView == SubSubMenuView) {
                backToSubMenu();
            } else if (CurrentView == SubMenuView) {
                backToMenu();
            }
        }
    }

    @SuppressLint("NewApi")
    public void hide() {
        if (CurrentView == NothingView) {
            return;
        }
        CurrentView = NothingView;
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation t = new TranslateAnimation(0, -getDensityPX(LayoutWidth), 0, 0);
        t.setDuration(500);
        set.addAnimation(t);

        AlphaAnimation a = new AlphaAnimation(1, 0);
        a.setDuration(500);
        set.addAnimation(a);
        AnticipateInterpolator i = new AnticipateInterpolator();
        set.setInterpolator(i);
        this.startAnimation(set);
        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hideItem != null && hideItem.equals(listView.getSelectedView())) {
                    hideItem.setAlpha(1);
                } else if (hideItem != null) {
                    hideItem.setAlpha(0.5f);
                }

                moveImageView.setAlpha(0.0f);
                subListView.setAlpha(0.0f);

                listView.setAlpha(1);
                listView.requestFocus();

                XgimiMenuView.this.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NewApi")
    private void backToMenu() {

        if (CurrentView == MainMenuView) {
            return;
        }
        CurrentView = MainMenuView;
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        subListView.setFocusable(false);
        subListView.setFocusableInTouchMode(false);
        AnimationSet sset = new AnimationSet(false);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        sa.setDuration(500);
        sset.addAnimation(sa);

        TranslateAnimation st = new TranslateAnimation(0, -subListView.getWidth(), 0, 0);
        AnticipateInterpolator i = new AnticipateInterpolator();
        st.setInterpolator(i);
        st.setDuration(500);
        sset.addAnimation(st);

        subListView.startAnimation(sset);
        sset.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String str = titleTextView.getText().toString();
                TextPaint paint = new TextPaint();
                paint.setTextSize(26);
//				float width = paint.measureText(str)*1.06f;
                float width = 0;
                subListView.setAlpha(0);
                int[] oRect = new int[2];
                int[] fromRect = new int[2];
                int[] toRect = new int[2];
                titleTextView.setAlpha(0);
                moveImageView.getLocationOnScreen(oRect);
                titleTextView.getLocationOnScreen(fromRect);
                toRect[0] = XgimiMenuView.this.fromRect[0];
                toRect[1] = XgimiMenuView.this.fromRect[1];
                TranslateAnimation it = new TranslateAnimation(fromRect[0] - oRect[0] + width, toRect[0] - oRect[0], fromRect[1] - oRect[1], toRect[1] - oRect[1]);
                it.setDuration(300);
                it.setFillAfter(true);
                moveImageView.startAnimation(it);
                it.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hideItem.setAlpha(1);
                        moveImageView.setAlpha(0.0f);
                        subListView.setAlpha(0.0f);

                        listView.setAlpha(1);
                        listView.requestFocus();
                        listView.bringToFront();
                        for (int i = listView.getFirstVisiblePosition(); i <= listView.getLastVisiblePosition(); i++) {
                            View v = menuAdapter.getViewAtPosition(i);
                            if (v == hideItem) {
                                continue;
                            }
                            int[] toRect = new int[2];
                            int[] fromRect = XgimiMenuView.this.fromRect;
                            v.getLocationOnScreen(toRect);
                            TranslateAnimation t = new TranslateAnimation(0, 0, fromRect[1] - toRect[1], 0);
                            t.setDuration(300);
                            v.startAnimation(t);
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("NewApi")
    private void backToSubMenu() {
        if (CurrentView == SubMenuView) {
            return;
        }
        CurrentView = SubMenuView;

        subListView.setFocusable(true);
        subListView.setFocusableInTouchMode(true);
        subSubListView.setFocusable(false);
        subSubListView.setFocusableInTouchMode(false);
        AnimationSet sset = new AnimationSet(false);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        sa.setDuration(500);
        sset.addAnimation(sa);

        TranslateAnimation st = new TranslateAnimation(0, -subSubListView.getWidth(), 0, 0);
        AnticipateInterpolator i = new AnticipateInterpolator();
        st.setInterpolator(i);
        st.setDuration(500);
        sset.addAnimation(st);

        subSubListView.startAnimation(sset);
        sset.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String str = titleTextView.getText().toString();
                TextPaint paint = new TextPaint();
                paint.setTextSize(26);
                float width = paint.measureText(str) * 1.06f;
//				float width = 0;
                subSubListView.setAlpha(0);
                int[] oRect = new int[2];
                int[] fromRect = new int[2];
                int[] toRect = new int[2];
                titleTextView.setAlpha(0);
                subMoveImageView.getLocationOnScreen(oRect);
                titleTextView.getLocationOnScreen(fromRect);
                toRect[0] = XgimiMenuView.this.subFromRect[0];
                toRect[1] = XgimiMenuView.this.subFromRect[1];
                TranslateAnimation it = new TranslateAnimation(fromRect[0] - oRect[0] + width, toRect[0] - oRect[0], fromRect[1] - oRect[1], toRect[1] - oRect[1]);
                it.setDuration(300);
                it.setFillAfter(true);
                subMoveImageView.startAnimation(it);
                it.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        subHideItem.setAlpha(1);
                        subMoveImageView.setAlpha(0.0f);
                        moveImageView.setAlpha(1.0f);
                        subSubListView.setAlpha(0.0f);

                        subListView.setAlpha(1);
                        subListView.requestFocus();
                        subListView.bringToFront();
                        for (int i = subListView.getFirstVisiblePosition(); i <= subListView.getLastVisiblePosition(); i++) {
                            View v = subMenuAdapter.getViewAtPosition(i);
                            if (v == subHideItem) {
                                continue;
                            }
                            int[] toRect = new int[2];
                            int[] fromRect = XgimiMenuView.this.subFromRect;
                            v.getLocationOnScreen(toRect);
                            TranslateAnimation t = new TranslateAnimation(0, 0, fromRect[1] - toRect[1], 0);
                            t.setDuration(300);
                            v.startAnimation(t);
                        }
                    }
                });
            }
        });
    }

    private View lastSelectedMenu = null;
    private int lastSelectedPosition = 0;
    private int moveCount = 0;
    private int subMoveCount = 0;
    @SuppressLint("NewApi")
    private OnItemSelectedListener listViewItemSelected = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (lastSelectedMenu != null) {
                lastSelectedMenu.setAlpha(0.5f);
                ScaleAnimation s = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f);
                s.setFillAfter(true);
                s.setDuration(300);
                lastSelectedMenu.startAnimation(s);

                if (moveCount == 0)//���moveImageView��һ�ζ���λ�ü��㲻׼ȷ���������
                {
                    int[] oRect = new int[2];
                    int[] toRect = new int[2];
                    int[] fRect = new int[2];
                    moveImageView.getLocationOnScreen(oRect);
                    moveImageView.setImageBitmap(convertViewToBitmap(view));
                    view.getLocationOnScreen(toRect);
                    titleTextView.getLocationOnScreen(fRect);
                    TranslateAnimation t = new TranslateAnimation(fRect[0] - oRect[0], toRect[0] - oRect[0], fRect[1] - oRect[1], toRect[1] - oRect[1]);
                    t.setDuration(100);
                    t.setFillAfter(true);
                    moveImageView.startAnimation(t);
                    moveCount++;
                }
            }
            view.setAlpha(1);
            ScaleAnimation s = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
            s.setFillAfter(true);
            s.setDuration(300);
            view.startAnimation(s);

            int count = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
            count = count / 2 + 1;
            if (position > lastSelectedPosition) {
                if (listView.getLastVisiblePosition() < menuAdapter.getCount()) {
                    if (position >= count) {
                        listView.smoothScrollBy(MenuItemHeight, 500);
                    }
                }
            }
            if (position < lastSelectedPosition) {
                if (listView.getLastVisiblePosition() > 0) {
                    if (position < menuAdapter.getCount() - count) {
                        listView.smoothScrollBy(-MenuItemHeight, 500);
                    }
                }
            }

            lastSelectedMenu = view;
            lastSelectedPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View lastSelectedSubMenu = null;
    private OnItemSelectedListener subListViewItemSelected = new OnItemSelectedListener() {

        @SuppressLint("NewApi")
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (view.equals(lastSelectedMenu)) {
                return;
            }
            XgimiMenu.Menu subMenu = (XgimiMenu.Menu) subMenuAdapter.getItem(position);
            if (subMenu.getType() == XgimiMenu.Type_CheckBox && 
            		(R.string.setting_subtitle==subMenu.getListenType()) && subMenu.isSelected()) {
            	lastSubmenuSelected=position;
			}
            if (lastSelectedSubMenu != null) {
                lastSelectedSubMenu.setAlpha(0.5f);
                ScaleAnimation s = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f);
                s.setDuration(300);
                s.setFillAfter(true);
                View v = lastSelectedSubMenu.findViewById(XgimiMenuAdapter.SubMenuAdapter.ID_TEXTVIEW);
                v.startAnimation(s);
            }
            view.setAlpha(1);
            ScaleAnimation s = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
            s.setFillAfter(true);
            s.setDuration(300);
            View v = view.findViewById(XgimiMenuAdapter.SubMenuAdapter.ID_TEXTVIEW);
            v.startAnimation(s);

            lastSelectedSubMenu = view;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View lastSelectedSubSubMenu = null;
    private OnItemSelectedListener subSubListViewItemSelected = new OnItemSelectedListener() {

        @SuppressLint("NewApi")
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (view.equals(lastSelectedSubSubMenu)) {
                return;
            }
            if (lastSelectedSubSubMenu != null) {
                lastSelectedSubSubMenu.setAlpha(0.5f);
                ScaleAnimation s = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f);
                s.setDuration(300);
                s.setFillAfter(true);
                View v = lastSelectedSubSubMenu.findViewById(XgimiMenuAdapter.SubMenuAdapter.ID_TEXTVIEW);
                v.startAnimation(s);
            }
            view.setAlpha(1);
            ScaleAnimation s = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
            s.setFillAfter(true);
            s.setDuration(300);
            View v = view.findViewById(XgimiMenuAdapter.SubMenuAdapter.ID_TEXTVIEW);
            v.startAnimation(s);

            lastSelectedSubSubMenu = view;
            Log.e("sublist","Selected"+ position+"");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    protected boolean isAnimate;

    private OnItemClickListener listViewItemClicked = new OnItemClickListener() {

        @SuppressLint("NewApi")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            XgimiMenu.Menu menu = (XgimiMenu.Menu) menuAdapter.getItem(position);
//			if(menu.parentMenu == null)
//			{
//				return;
//			}
           
            for (int i = 0; i < menuAdapter.getCount(); i++) {
                XgimiMenu.Menu m = (XgimiMenu.Menu) menuAdapter.getItem(i);
                
                if (m!=null && m.parentMenu !=null && m.parentMenu == menu.parentMenu)//ͬһ�����˵�
                {
                    m.setSelected(false);
                }
            }

            menu.setSelected(true);
            menuAdapter.notifyDataSetChanged();
            if (listener != null) {
                XGIMILOG.D("listViewItemClicked, listener != null");
                listener.OnMenuClicked(menu);
            }

            for (int i = listView.getFirstVisiblePosition() + 1; i <= listView.getLastVisiblePosition(); i++) {
                View v = menuAdapter.getViewAtPosition(i);
//                View v = listView.getChildAt(i);
                TextView tv = (TextView) v.findViewById(XgimiMenuAdapter.BaseMenuAdapter.ID_TEXTVIEW);
                if (!v.equals(view)) {
                    tv.setScaleX(1);
                    tv.setScaleY(1);
                    v.setAlpha(0.5f);
                } else {
                    if (v.getAlpha() != 1) {
                        ScaleAnimation s = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
                        s.setFillAfter(true);
                        s.setDuration(30);
                        tv.startAnimation(s);
                        v.setAlpha(1);
                    }
                }
            }
            if (moveCount == 0)//���moveImageView��һ�ζ���λ�ü��㲻׼ȷ���������
            {
                int[] oRect = new int[2];
                int[] toRect = new int[2];
                int[] fRect = new int[2];
                moveImageView.getLocationOnScreen(oRect);
                moveImageView.setImageBitmap(convertViewToBitmap(view));
                view.getLocationOnScreen(toRect);
                titleTextView.getLocationOnScreen(fRect);
                TranslateAnimation t = new TranslateAnimation(fRect[0] - oRect[0], toRect[0] - oRect[0], fRect[1] - oRect[1], toRect[1] - oRect[1]);
                t.setDuration(100);
                t.setFillAfter(true);
                moveImageView.startAnimation(t);
                moveCount++;
            }

            if (menu.subMenus.size() == 0) {
                return;
            }

            if (CurrentView == SubMenuView) {
                return;
            }
            CurrentView = SubMenuView;
            listView.setFocusable(false);
            listView.setFocusableInTouchMode(false);
            subListView.setFocusable(true);
            subListView.setFocusableInTouchMode(true);
            isAnimate = true;
            int duration = 300;
            for (int i = listView.getFirstVisiblePosition(); i <= listView.getLastVisiblePosition(); i++) {
                View v = menuAdapter.getViewAtPosition(i);
                if (v != view) {
                    int[] fromRect = new int[2];
                    int[] toRect = new int[2];
                    v.getLocationOnScreen(fromRect);
                    view.getLocationOnScreen(toRect);
                    TranslateAnimation t = new TranslateAnimation(0, 0, 0, toRect[1] - fromRect[1]);
                    t.setDuration(duration);
                    v.startAnimation(t);
                }
            }

            final XgimiMenu.Menu finalMenu = menu;
            View titleView = null;
            if (menu.type == XgimiMenu.Type_IconText) {
                titleView = view;
            } else {
                titleView = view.findViewById(XgimiMenuAdapter.MenuAdapter.ID_TEXTVIEW);
            }
            final View finalView = titleView;

            postDelayed(new Runnable() {

                @Override
                public void run() {
                    Bitmap bm = convertViewToBitmap(finalView);
                    moveImageView.setImageBitmap(bm);
                    moveImageView.setAlpha(1.0f);
                    moveImageView.setScaleType(ScaleType.FIT_START);
                    moveImageView.setScaleX(1.1f);
                    moveImageView.setScaleY(1.1f);


                    String str = finalMenu.name;
                    TextPaint paint = new TextPaint();
                    paint.setTextSize(26);
//					float width = paint.measureText(str)*1.06f;
                    float width = 0;

                    int[] oRect = new int[2];
                    int[] toRect = new int[2];

                    moveImageView.getLocationOnScreen(oRect);
                    titleTextView.getLocationOnScreen(toRect);
                    finalView.getLocationOnScreen(fromRect);

                    TranslateAnimation t = new TranslateAnimation(fromRect[0] - oRect[0], toRect[0] - oRect[0] + width, fromRect[1] - oRect[1], toRect[1] - oRect[1]);
                    t.setDuration(300);
                    t.setFillAfter(true);
                    moveImageView.startAnimation(t);
                    finalView.setAlpha(0);
                    hideItem = finalView;
                    listView.setAlpha(0);
                    subListView.requestFocus();
                    t.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            if (finalMenu.parentMenu != null) {
                                titleTextView.setAlpha(1);
                                titleTextView.setText(finalMenu.parentMenu.name);
                            }
                            isAnimate = false;
                            subListView.setAlpha(1);

                            AnimationSet sset = new AnimationSet(false);

                            AlphaAnimation sa = new AlphaAnimation(0, 1);
                            sa.setDuration(500);
                            sset.addAnimation(sa);
                            TranslateAnimation st = new TranslateAnimation(-subListView.getWidth(), 0, 0, 0);
                            OvershootInterpolator o = new OvershootInterpolator();
                            st.setInterpolator(o);
                            st.setDuration(500);
                            sset.addAnimation(st);

                            subListView.startAnimation(sset);
                            subMenuAdapter.update(finalMenu.subMenus);
                            subListView.bringToFront();
                            boolean b = true;
                            ArrayList<XgimiMenu.Menu> menus = menuAdapter.getMenus().get(lastSelectedPosition).subMenus;
                            for (int i = 0, menusSize = menus.size(); i < menusSize; i++) {
                                XgimiMenu.Menu subMenu = menus.get(i);
                                if (subMenu.isSelected()) {
                                    subListView.setSelection(i);
                                    b = false;
                                }
                            }
                            if (b) {
                                subListView.setSelection(0);
                            }
                        }
                    });
                }
            }, duration);
        }

        ;
    };

     int lastSubmenuSelected = 0;
     public int lastSubmenuSelectedS = 2;
    private OnItemClickListener subListViewItemClicked = new OnItemClickListener() {

        @SuppressLint("NewApi")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            for (int i = 0; i < subMenuAdapter.getCount(); i++) {
                XgimiMenu.Menu subMenu = (XgimiMenu.Menu) subMenuAdapter.getItem(i);
                subMenu.selected = false;
            }
            
            XgimiMenu.Menu subMenu = (XgimiMenu.Menu) subMenuAdapter.getItem(position);
            Log.e("subMenuString", subMenu.getListenType()+" "+subMenu.getName());
            if (subMenu.getType() == XgimiMenu.Type_CheckBox && 
            		(R.string.setting_subtitle==subMenu.getListenType()) ) {
            	lastSubmenuSelected=position;
			}
            if (subMenu.getType() == XgimiMenu.Type_CheckBox && 
            		(R.string.setting_play_mode==subMenu.getListenType()) ) {
            	lastSubmenuSelectedS=position;
            	Settings.System.putInt(context.getContentResolver(), "play_mode", position);
			}
            if (subMenu.getType() == XgimiMenu.Type_Int || subMenu.getType() == XgimiMenu.Type_SubtitleSysc ) {
            	 XgimiMenu.Menu subMenu1 = (XgimiMenu.Menu) subMenuAdapter.getItem(lastSubmenuSelected);
                 subMenu1.selected=true;
                 subMenuAdapter.notifyDataSetChanged();
                 if (listener != null) {
                     XGIMILOG.D("subListViewItemClicked, listener != null");
                     listener.OnSubMenuClicked(subMenu1, lastSubmenuSelected);
                 }
			}
            if ( subMenu.getType() == XgimiMenu.Type_Text) {
            	XgimiMenu.Menu subMenu1 = (XgimiMenu.Menu) subMenuAdapter.getItem(lastSubmenuSelectedS);
                subMenu1.selected=true;
                subMenuAdapter.notifyDataSetChanged();
			}
           
            if (subMenu != null && subMenu.getType() != XgimiMenu.Type_TextBox && subMenu.getType()
            		!= XgimiMenu.Type_SubtitleSysc && subMenu.getType() != XgimiMenu.Type_Int ) {
                subMenu.selected = true;
                subMenuAdapter.notifyDataSetChanged();
                if (listener != null) {
                    XGIMILOG.D("subListViewItemClicked, listener != null");
                    listener.OnSubMenuClicked(subMenu, position);
                }
            }
            
            for (int i = subListView.getFirstVisiblePosition(); i <= subListView.getLastVisiblePosition(); i++) {
                View v = subMenuAdapter.getViewAtPosition(i);
                TextView tv = (TextView) v.findViewById(XgimiMenuAdapter.BaseMenuAdapter.ID_TEXTVIEW);
                if (i != position) {
                    tv.setScaleX(1);
                    tv.setScaleY(1);
                    v.setAlpha(0.5f);
                } else {
                    if (v.getAlpha() != 1) {
                        ScaleAnimation s = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
                        s.setFillAfter(true);
                        s.setDuration(30);
                        tv.startAnimation(s);
                        v.setAlpha(1);
                    }
                }
            }
            if (subMoveCount == 0)//���moveImageView��һ�ζ���λ�ü��㲻׼ȷ���������
            {
                int[] oRect = new int[2];
                int[] toRect = new int[2];
                int[] fRect = new int[2];
                subMoveImageView.getLocationOnScreen(oRect);
                subMoveImageView.setImageBitmap(convertViewToBitmap(view));
                view.getLocationOnScreen(toRect);
                titleTextView.getLocationOnScreen(fRect);
                TranslateAnimation t = new TranslateAnimation(fRect[0] - oRect[0], toRect[0] - oRect[0],
                        fRect[1] - oRect[1], toRect[1] - oRect[1]);
                t.setDuration(100);
                t.setFillAfter(true);
                subMoveImageView.startAnimation(t);
                subMoveCount++;
            }

            if (subMenu != null) {
                if (subMenu.subMenus == null || subMenu.subMenus.size() == 0) {
                    return;
                }
            } else {
                return;
            }

            if (CurrentView == SubSubMenuView) {
                return;
            }
            CurrentView = SubSubMenuView;
            subListView.setFocusable(false);
            subListView.setFocusableInTouchMode(false);
            subSubListView.setFocusable(true);
            subSubListView.setSelection(0);
            subSubMenuAdapter.notifyDataSetChanged();
            subSubListView.setFocusableInTouchMode(true);
            isAnimate = true;
            int duration = 300;
            for (int i = subListView.getFirstVisiblePosition(); i <= subListView.getLastVisiblePosition(); i++) {
                View v = subMenuAdapter.getViewAtPosition(i);
                if (v != view) {
                    int[] fromRect = new int[2];
                    int[] toRect = new int[2];
                    v.getLocationOnScreen(fromRect);
                    view.getLocationOnScreen(toRect);
                    TranslateAnimation t = new TranslateAnimation(0, 0, 0, toRect[1] - fromRect[1]);
                    t.setDuration(duration);
                    v.startAnimation(t);
                }
            }

            final XgimiMenu.Menu finalMenu = subMenu;
            View titleView = null;
            if (subMenu.type == XgimiMenu.Type_IconText) {
                titleView = view;
            } else {
                titleView = view.findViewById(XgimiMenuAdapter.MenuAdapter.ID_TEXTVIEW);
            }
            final View finalView = titleView;

            postDelayed(new Runnable() {

                @Override
                public void run() {
                    Bitmap bm = convertViewToBitmap(finalView);
                    subMoveImageView.setImageBitmap(bm);
                    subMoveImageView.setAlpha(1.0f);
                    subMoveImageView.setScaleType(ScaleType.FIT_START);
                    subMoveImageView.setScaleX(1.1f);
                    subMoveImageView.setScaleY(1.1f);

                    String str = finalMenu.parentMenu.name;
                    TextPaint paint = new TextPaint();
                    paint.setTextSize(26);
                    float width = paint.measureText(str) * 1.06f;
//					float width = 0;

                    int[] oRect = new int[2];
                    int[] toRect = new int[2];

                    subMoveImageView.getLocationOnScreen(oRect);
                    titleTextView.getLocationOnScreen(toRect);
                    finalView.getLocationOnScreen(subFromRect);
                    float toXDlelta =toRect[0] - oRect[0] + width +52;
                    float toYDlelta =toRect[1] - oRect[1]  -20;
//                    if (DisplayUtil.WHis1280x800(context)) {
//                    	toXDlelta = toRect[0] - oRect[0] + width ;
//                    	toYDlelta =toRect[1] - oRect[1]  ;
//					}
//                    if (Device.is4kjiguang()) {
//                    	toXDlelta = toRect[0] - oRect[0] + width +210 ;
//                    	toYDlelta =toRect[1] - oRect[1] - 50  ;
//					}
                    TranslateAnimation t = new TranslateAnimation(subFromRect[0] - oRect[0], toXDlelta ,
                            subFromRect[1] - oRect[1], toYDlelta );
                    t.setDuration(300);
                    t.setFillAfter(true);
                    subMoveImageView.startAnimation(t);
                    moveImageView.setAlpha(0.0f);
                    finalView.setAlpha(0);
                    subHideItem = finalView;
                    subListView.setAlpha(0);
                    Log.d(TAG, "subSubListView:setAlpha=0");
//					subSubListView.requestFocus();
                    t.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (finalMenu.parentMenu != null && !finalMenu.parentMenu.name.equals(context.getResources().getString(R.string.setting_subtitle))) {
                                titleTextView.setAlpha(1);
                                titleTextView.setText(finalMenu.parentMenu.name);
                            }
                            isAnimate = false;
                            subSubListView.setAlpha(1);

                            AnimationSet sset = new AnimationSet(false);

                            AlphaAnimation sa = new AlphaAnimation(0, 1);
                            sa.setDuration(500);
                            sset.addAnimation(sa);

                            TranslateAnimation st = new TranslateAnimation(-subSubListView.getWidth(), 0, 0, 0);
                            OvershootInterpolator o = new OvershootInterpolator();
                            st.setInterpolator(o);
                            st.setDuration(500);
                            sset.addAnimation(st);

                            subSubListView.startAnimation(sset);
                            //TODO charge is picture mode self define ,update the sub sub menu data
//                            if (finalMenu.getName().equals(context.getString(R.string.self_define))) {
//                            	notifySubsubView();
//                            } else {
                                subSubMenuAdapter.update(finalMenu.subMenus);
//                            }
                            subSubListView.bringToFront();
//                            subSubListView.setSelection(0);
                            postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //��Ե�һ�ν�������˵���ѡ�����⴦��
                                    if (subSubMenuAdapter.getViewAtPosition(0).getAlpha() == 0.5) {
                                        subSubListViewItemSelected.onItemSelected(null,  subSubMenuAdapter.getViewAtPosition(0),    0,
                                                subSubMenuAdapter.getItemId(0));
                                    }
                                    subSubListView.requestFocus();
                                }
                            }, 500);
                        }
                    });
                }
            }, duration);
        }

        ;
    };

    public static Bitmap convertViewToBitmap(View view) {
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        view.buildDrawingCache();
//        Bitmap bitmap = view.getDrawingCache();
//        Bitmap b = Bitmap.createBitmap(bitmap);
//        return b;

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public void notifySubsubView() {
//        PlayerUtils playerUtils = new PlayerUtils(context);
//        subSubMenuAdapter.update(playerUtils.getColor());
    }

    public void notifyListView() {
        menuAdapter.notifyDataSetChanged();
    }

    public void notifySubView() {
        subMenuAdapter.notifyDataSetChanged();
    }

    private long menuTime = 0;

    public long getMenuTime() {
        return menuTime;
    }

    public boolean isChoiceShow() {
        return CurrentView == SubMenuView;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        menuTime = System.currentTimeMillis();
        if (this.getVisibility() == View.VISIBLE) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (isAnimate) {
                    return true;
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (CurrentView == SubSubMenuView) {
                        int pos = subSubListView.getSelectedItemPosition();
                        if (pos < 0 || pos >= subSubMenuAdapter.getCount()) {
                            return true;
                        }
                        
                        XgimiMenu.Menu menu = (XgimiMenu.Menu) subSubMenuAdapter.getItem(pos);
                        Log.d("curValue", "menu:"+menu.getName()+"");
                        menu.nextValue();
                        if (XgimiMenu.Type_TextBox == menu.getType()) {
                        	if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
        						menu.previousValue();
                            }
                            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                String text=menu.getCurText();
                                if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.autodownload)) ) {
                                	Settings.System.putInt(context.getContentResolver(), "autodownload", 0);
        						} else if(text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.autodownload))){
        							Settings.System.putInt(context.getContentResolver(), "autodownload", 1);
        						}
                                if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.savetolocal))) {
                                	Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 0);
        						} else if (text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.savetolocal))){
        							Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 1);
        						}
                            }
						}else if( XgimiMenu.Type_Int == menu.getType()) {
		                	int curValue = Settings.System.getInt(context.getContentResolver(), "CurValue",0);
		                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
		                    	curValue=curValue+1;
		                        if (curValue > 100) {
		                            curValue = 0;
		                        }
		                        menu.setCurValue(curValue);
		                        Settings.System.putInt(context.getContentResolver(), "CurValue", curValue);
		                        Intent intent1 = new Intent();
			       		        intent1.setAction("action.refreshFriend");  
			       		        context.sendBroadcast(intent1);
		                    }
						}else if(XgimiMenu.Type_SubtitleSysc == menu.getType()){
		                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
		                        int currentValue = menu.getCurrentValue() + 200;
		                        if (currentValue >= 4800) {
		                            currentValue = 4800;
		                        }
		                        Log.e("sublist","KEYCODE_DPAD_RIGHT"+currentValue+"");
		                        menu.setCurrentValue(currentValue);
		                    }
						}
                        if (listener != null) {
                            XGIMILOG.D("dispatchKeyEvent, listener != null");
                            listener.OnMenuValueChanged(menu);
                        }
                        subSubMenuAdapter.notifyDataSetChanged();
                        return true;
                    } else if (CurrentView == SubMenuView) {
                        if (process(event)) return true;
//                        int selectedItemPosition = subListView.getSelectedItemPosition();
//                        subListViewItemClicked.onItemClick(null,
//                                listView.getSelectedView(),
//                                selectedItemPosition,
//                                -1);
                        
                        return true;
                    } else {
                        if (listView.getSelectedItemPosition() != menuAdapter.getCount() - 1) {
                            listViewItemClicked.onItemClick(null,
                                    listView.getSelectedView(),
                                    listView.getSelectedItemPosition(),
                                    -1);
                        } else {
                            return true;
                        }
                    }
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
//                    if (process(event)) return true;
                    int selectedItemPosition = subListView.getSelectedItemPosition();
                    if (selectedItemPosition < 0 ) {
						return true;
					}
                    XgimiMenu.Menu menu1 = subMenuAdapter.getMenus().get(selectedItemPosition);
                     if (XgimiMenu.Type_TextBox == menu1.getType()){
						XgimiMenu.Menu menu = subMenuAdapter.getMenus().get(selectedItemPosition);
	                        menu.previousValue();
	                    if (listener != null) {
                            XGIMILOG.D("event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT");
	                        listener.OnMenuValueChanged(menu);
	                    }
	                    subMenuAdapter.notifyDataSetChanged();
//	                    subListView.setSelection(selectedItemPosition);
	                    return true;
					}
                    
                    if (CurrentView == SubSubMenuView) {
                        int pos = subSubListView.getSelectedItemPosition();
                        if (pos < 0 || pos >= subSubMenuAdapter.getCount()) {
                            return true;
                        }
                        XgimiMenu.Menu menu = (XgimiMenu.Menu) subSubMenuAdapter.getItem(pos);
                        menu.previousValue();
                        if (XgimiMenu.Type_TextBox == menu.getType()) {
                        	if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
        						String text=menu.getCurText();
                                if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.autodownload)) ) {
                                	Settings.System.putInt(context.getContentResolver(), "autodownload", 0);
        						} else if(text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.autodownload))){
        							Settings.System.putInt(context.getContentResolver(), "autodownload", 1);
        						}
                                if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.savetolocal))) {
                                	Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 0);
        						} else if (text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.savetolocal))){
        							Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 1);
        						}
                            }
                        	}else if(XgimiMenu.Type_Int == menu.getType()){
                            	if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                            		int curValue= Settings.System.getInt(context.getContentResolver(), "CurValue",0);
                            		Log.d("curValue", "curValue"+curValue+"");
                            		curValue=curValue -1;
                                    if (curValue < 0) {
                                        curValue = 100;
                                    }
                                    Log.d("curValue", "curValue+1="+curValue+"");
                                    menu.setCurValue(curValue);
                                    Settings.System.putInt(context.getContentResolver(), "CurValue", curValue);
                                    Intent intent = new Intent();
            	       		        intent.setAction("action.refreshFriend");  
            	       		        context.sendBroadcast(intent);
                                }
        					}else if (XgimiMenu.Type_SubtitleSysc == menu.getType()) {
                                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    int currentValue = menu.getCurrentValue() - 200;
                                    if (currentValue <= -4800) {
                                        currentValue = -4800;
                                    }
                                    Log.d("autodownload", "left-curValue:"+currentValue+"");
                                    menu.setCurrentValue(currentValue);
                                }
                            }
                        if (listener != null) {
                            XGIMILOG.D("");
                            listener.OnMenuValueChanged(menu);
                        }
                        subSubMenuAdapter.notifyDataSetChanged();
                        return true;
                    } else if (CurrentView == SubMenuView) {
//                        if (process(event)) return true;
                        int pos = subListView.getSelectedItemPosition();
                        if (pos < 0 || pos >= subMenuAdapter.getCount()) {
                            return true;
                        }
                        XgimiMenu.Menu menu = (XgimiMenu.Menu) subMenuAdapter.getItem(pos);
                        if (menu.getName().equals(context.getString(R.string.replace_speed)) || 
                        		menu.getName().equals(context.getString(R.string.pptplay_anim))) {
                            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                menu.previousValue();
                            }
                            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                menu.nextValue();
                            }
                            if (listener != null) {
                                XGIMILOG.D("");
                                listener.OnMenuValueChanged(menu);
                            }
                            subMenuAdapter.notifyDataSetChanged();
                            return true;
                        }
                        this.back();
                        return true;
                    } else {
                        this.back();
                        return true;
                    }
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (CurrentView == SubMenuView
                            || CurrentView == SubSubMenuView
                            || CurrentView == MainMenuView) {
                        this.back();
                        return true;
                    }
                } 

            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean process(KeyEvent event) {
        try {
            int selectedItemPosition = subListView.getSelectedItemPosition();
            final XgimiMenu.Menu menu = subMenuAdapter.getMenus().get(selectedItemPosition);
            switch (menu.getType()) {
                
                case XgimiMenu.Type_TextBox:
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                        menu.previousValue();
                    }
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        menu.nextValue();
                        String text=menu.getCurText();
                        if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.autodownload)) ) {
                        	Settings.System.putInt(context.getContentResolver(), "autodownload", 0);
						} else if(text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.autodownload))){
							Settings.System.putInt(context.getContentResolver(), "autodownload", 1);
						}
                        if (text.equals(context.getString(R.string.close)) && menu.getName().equals(context.getString(R.string.savetolocal))) {
                        	Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 0);
						} else if (text.equals(context.getString(R.string.open)) && menu.getName().equals(context.getString(R.string.savetolocal))){
							Settings.System.putInt(context.getContentResolver(), "deletesubtitletype", 1);
						}
                    }
                    if (listener != null) {
                        XGIMILOG.D("");
                        listener.OnMenuValueChanged(menu);
                    }
                    subMenuAdapter.notifyDataSetChanged();
//                    subListView.setSelection(selectedItemPosition);
                    return true;
                
                default:
                    return true;
            }
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
