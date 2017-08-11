package com.xgimi.videodemo.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xgimi.videodemo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class XgimiMenuAdapter {
    public static class BaseMenuAdapter extends BaseAdapter {
        public static final int ID_TEXTVIEW = 0x100;
        public static final int ID_CHECKBOX = 0x101;
        public static final int ID_VALUEVIEW = 0x102;
        public int selectposition=0;
        public interface OnGetViewSave {
            void onSaveView(int position, View view);
        }

        public void setOnGetViewSave(OnGetViewSave onGetViewSave) {
            this.onGetViewSave = onGetViewSave;
        }

        OnGetViewSave onGetViewSave;
        public ListView listView = null;
        private XgimiMenuView.OnMenuListener listener = null;
        private Context context;
        protected ArrayList<XgimiMenu.Menu> menus;

        public ArrayList<XgimiMenu.Menu> getMenus() {
            return menus;
        }

        private HashMap<Integer, View> _viewMap = new HashMap<Integer, View>();

        public View getViewAtPosition(int pos) {
            return _viewMap.get(Integer.valueOf(pos));
        }

        public void setViewAtPostion(int position, View view) {
            _viewMap.put(position, view);
        }

        public BaseMenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus) {
            super();
            this.context = c;
            this.menus = menus;
        }

        @Override
        public int getItemViewType(int position) {
            XgimiMenu.Menu menu = menus.get(position);
            return menu.type;
        }

        @Override
        public int getViewTypeCount() {
            return 7;
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public Object getItem(int position) {
            if (position==-1) {
                return null;
            }
            if (position < menus.size()) {
                return menus.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            XgimiMenu.Menu menu = menus.get(position);
            int type = menu.type;
            if (convertView == null) {
                convertView = createView(type);
            }
            if (menu.parentMenu != null) {
                if (type == XgimiMenu.Type_IconText || type == XgimiMenu.Type_Text || type == XgimiMenu.Type_CheckBox) {
                    convertView.setPadding( getDensityPX(80),
                            getDensityPX(convertView.getPaddingTop()),
                            getDensityPX(convertView.getPaddingRight()),
                            getDensityPX(convertView.getPaddingBottom()));
                } else {
                    convertView.setPadding( getDensityPX(50),
                            getDensityPX(convertView.getPaddingTop()),
                            getDensityPX(convertView.getPaddingRight()),
                            getDensityPX(convertView.getPaddingBottom()));
                }
            }
            MenuHolder h = (MenuHolder) convertView.getTag();
//            h.menu = menu;
            if (type == XgimiMenu.Type_IconText) {
                ImageView i = h.imageView;
                i.setImageResource(menus.get(position).icon);

                TextView t = h.textView;
                t.setSingleLine(true);
    			t.setEllipsize(TruncateAt.MARQUEE);
    			t.setMarqueeRepeatLimit(-1);
                t.setText(menus.get(position).name);
            } else if (type == XgimiMenu.Type_CheckBox) {
                CheckBox c = h.checkBox;
                if (menus.get(position).selected) {
                    c.setBackgroundResource(R.drawable.ic_cb_on);
                } else {
                    c.setBackgroundResource(R.drawable.ic_cb_off);
                }

                TextView t = h.textView;
                t.setSingleLine(true);
    			t.setEllipsize(TruncateAt.MARQUEE);
    			t.setMarqueeRepeatLimit(-1);
                t.setText(menus.get(position).name);
            } else if (type == XgimiMenu.Type_IntBox) {
                TextView t = h.textView;
                TextView vt = h.valueTextView;
                t.setText(menus.get(position).name);
                t.setSingleLine(true);
    			t.setEllipsize(TruncateAt.MARQUEE);
    			t.setMarqueeRepeatLimit(-1);
                vt.setText(String.valueOf(menus.get(position).curValue));
                vt.setSingleLine(true);
    			vt.setEllipsize(TruncateAt.MARQUEE);
    			vt.setMarqueeRepeatLimit(-1);
                h.switchLeftImageView.setTag(h);
                h.switchRightImageView.setTag(h);
            } else if (type == XgimiMenu.Type_Int) {
                TextView t = h.textView;
                TextView vt = h.valueTextView;
                t.setText(menus.get(position).name);
                vt.setText(String.valueOf(menus.get(position).curValue));
            } else if (type == XgimiMenu.Type_TextBox) {
                TextView t = h.textView;
                TextView vt = h.valueTextView;
                t.setText(menus.get(position).name);
                t.setSingleLine(true);
    			t.setEllipsize(TruncateAt.MARQUEE);
    			t.setMarqueeRepeatLimit(-1);
                vt.setText(menus.get(position).curText);
                vt.setSingleLine(true);
    			vt.setEllipsize(TruncateAt.MARQUEE);
    			vt.setMarqueeRepeatLimit(-1);
                h.switchLeftImageView.setTag(h);
                h.switchRightImageView.setTag(h);
            } else if (type == XgimiMenu.Type_SubtitleSysc) {
                TextView t = h.valueTextView;
                t.setSingleLine(true);
    			t.setEllipsize(TruncateAt.MARQUEE);
    			t.setMarqueeRepeatLimit(-1);
                t.setText(menus.get(position).getCurrentValue() + "");
            }else if (type == XgimiMenu.Type_Text) {
                TextView t = h.textView;
                t.setText(menus.get(position).name);
            }

//            if (onGetViewSave != null) {
//                onGetViewSave.onSaveView(position, convertView);
//            }
//            if (parent.getChildCount() == position) {
            _viewMap.remove(Integer.valueOf(position));
            _viewMap.put(position, convertView);
//            }

            return convertView;
        }

        //获取绝对长度
        private int getDensityPX(float data) {
            return (int) (data * context.getResources().getDisplayMetrics().density);
        }
        @SuppressLint("NewApi")
        private View createView(int type) {
            LinearLayout layout = new LinearLayout(context);
            layout.setBackgroundColor(Color.TRANSPARENT);
            layout.setAlpha(0.5f);
            if (type == XgimiMenu.Type_Text) {
                TextView t = new TextView(context);
                t.setId(ID_TEXTVIEW);
                t.setTextColor(Color.WHITE);
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setTextSize(26);

                LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(
                        getDensityPX(XgimiMenuView.LayoutWidth),
                        getDensityPX(XgimiMenuView.MenuItemHeight));
//                tpm.setMargins(getDensityPX(20), 0, 0, 0);
                layout.addView(t, tpm);

                MenuHolder h = new MenuHolder();
                h.textView = t;
                layout.setTag(h);
            } else if (type == XgimiMenu.Type_IconText) {
                ImageView i = new ImageView(context);
                LinearLayout.LayoutParams ipm = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                ipm.setMargins(getDensityPX(10), 0, 0, 0);
                ipm.gravity = LinearLayout.VERTICAL;
                layout.addView(i, ipm);

                TextView t = new TextView(context);
                t.setId(ID_TEXTVIEW);
                t.setTextColor(Color.WHITE);
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setTextSize(26);

                LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(
                        getDensityPX(XgimiMenuView.LayoutWidth),
                        getDensityPX(XgimiMenuView.MenuItemHeight));
                tpm.setMargins(getDensityPX(20), 0, 0, 0);
                layout.addView(t, tpm);

                MenuHolder h = new MenuHolder();
                h.imageView = i;
                h.textView = t;
                layout.setTag(h);
            } else if (type == XgimiMenu.Type_CheckBox) {
                CheckBox c = new CheckBox(context);
                c.setId(ID_CHECKBOX);
                c.setGravity(Gravity.CENTER_VERTICAL);
                c.setChecked(false);
                c.setFocusable(false);
                c.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
                LinearLayout.LayoutParams cpm = new LinearLayout.LayoutParams(getDensityPX(25), getDensityPX(25));
                cpm.setMargins(0, 0, 0, 0);
                layout.addView(c, cpm);

                TextView t = new TextView(context);
                t.setId(ID_TEXTVIEW);
                t.setFocusable(false);
                t.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                t.setTextColor(Color.WHITE);
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setTextSize(26);
                t.setFocusable(false);
                LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(
                        getDensityPX(XgimiMenuView.LayoutWidth),
                        getDensityPX(XgimiMenuView.MenuItemHeight));
                tpm.setMargins(getDensityPX(20), 0, 0, 0);
                layout.addView(t, tpm);

                MenuHolder h = new MenuHolder();
                h.checkBox = c;
                h.textView = t;
                layout.setTag(h);
            } else if (type == XgimiMenu.Type_IntBox || type == XgimiMenu.Type_TextBox  ) {
            	TextView t = new TextView(context);
                t.setId(ID_TEXTVIEW);
                t.setTextColor(Color.WHITE);
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setTextSize(26);
                t.setFocusable(false);
                LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(
                        getDensityPX(130),
                        getDensityPX(XgimiMenuView.MenuItemHeight));
                tpm.setMargins(getDensityPX(20), 0, 0, 0);
                layout.addView(t, tpm);

                ImageView liv = new ImageView(context);
                liv.setImageResource(R.drawable.ic_left);
                LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lpm.setMargins(getDensityPX(50), 0, 0, 0);
                lpm.gravity = LinearLayout.VERTICAL;
                layout.addView(liv, lpm);
                liv.setClickable(true);
//                liv.setOnClickListener(leftSwitchClicked);

                TextView vt = new TextView(context);
                vt.setId(ID_VALUEVIEW);
                vt.setTextColor(Color.WHITE);
                vt.setGravity(Gravity.CENTER);
                vt.setTextSize(26);
                vt.setFocusable(false);
                LinearLayout.LayoutParams vtpm = new LinearLayout.LayoutParams(
                		getDensityPX(100),
                		getDensityPX(XgimiMenuView.MenuItemHeight));
                vtpm.setMargins(getDensityPX(20), 0, getDensityPX(20), 0);
                layout.addView(vt, vtpm);

                ImageView riv = new ImageView(context);
                riv.setImageResource(R.drawable.ic_right);
                LinearLayout.LayoutParams rpm = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                rpm.setMargins(0, 0, 0, 0);
                rpm.gravity = LinearLayout.VERTICAL;
                layout.addView(riv, rpm);
                riv.setClickable(true);
//                riv.setOnClickListener(rightSwitchClicked);

                MenuHolder h = new MenuHolder();
                h.textView = t;
                h.switchLeftImageView = liv;
                h.switchRightImageView = riv;
                h.valueTextView = vt;
                layout.setTag(h);
            }  else if(type == XgimiMenu.Type_Int){
            	 TextView t = new TextView(context);
                 t.setId(ID_TEXTVIEW);
                 t.setTextColor(Color.WHITE);
                 t.setGravity(Gravity.CENTER_VERTICAL);
                 t.setTextSize(26);
                 t.setFocusable(false);
                 LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(
                         getDensityPX(130),
                         getDensityPX(XgimiMenuView.MenuItemHeight));
                 tpm.setMargins(getDensityPX(20), 0, 0, 0);
                 layout.addView(t, tpm);

                 ImageView liv = new ImageView(context);
                 liv.setImageResource(R.drawable.ic_left);
                 LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(
                         LinearLayout.LayoutParams.WRAP_CONTENT,
                         LinearLayout.LayoutParams.MATCH_PARENT);
                 lpm.setMargins(getDensityPX(50), 0, 0, 0);
                 lpm.gravity = LinearLayout.VERTICAL;
                 layout.addView(liv, lpm);
 				 liv.setFocusable(false);;

                 TextView vt = new TextView(context);
                 vt.setId(ID_VALUEVIEW);
                 vt.setTextColor(Color.WHITE);
                 vt.setGravity(Gravity.CENTER);
                 vt.setTextSize(26);
                 vt.setFocusable(false);
                 LinearLayout.LayoutParams vtpm = new LinearLayout.LayoutParams(
                		 getDensityPX(100),
                		 getDensityPX(XgimiMenuView.MenuItemHeight));
                 vtpm.setMargins(getDensityPX(20), 0, getDensityPX(20), 0);
                 layout.addView(vt, vtpm);

                 ImageView riv = new ImageView(context);
                 riv.setImageResource(R.drawable.ic_right);
                 LinearLayout.LayoutParams rpm = new LinearLayout.LayoutParams(
                         LinearLayout.LayoutParams.WRAP_CONTENT,
                         LinearLayout.LayoutParams.MATCH_PARENT);
                 rpm.setMargins(0, 0, 0, 0);
                 rpm.gravity = LinearLayout.VERTICAL;
                 layout.addView(riv, rpm);
 				 riv.setFocusable(false);


                 MenuHolder h = new MenuHolder();
                 h.textView = t;
                 h.switchLeftImageView = liv;
                 h.switchRightImageView = riv;
                 h.valueTextView = vt;
                 layout.setTag(h);
            }else if (type == XgimiMenu.Type_SubtitleSysc) {
                layout.setAlpha(0.5f);
                layout.setGravity(Gravity.CENTER);

                TextView tv = new TextView(context);
                tv.setFocusable(false);
                tv.setTextSize(26);
                tv.setTextColor(Color.WHITE);
                tv.setText(context.getString(R.string.delay));
                LinearLayout.LayoutParams cpmtv = new LinearLayout.LayoutParams(getDensityPX(40), getDensityPX(20));
                cpmtv.setMargins(getDensityPX(80), 0, 0, 0);
                layout.addView(tv, cpmtv);

                ImageView cc = new ImageView(context);
                cc.setFocusable(false);
                cc.setBackgroundResource(R.drawable.ic_left);
                LinearLayout.LayoutParams cpm = new LinearLayout.LayoutParams(getDensityPX(20), getDensityPX(20));
                cpm.setMargins(getDensityPX(20), 0, 0, 0);
                layout.addView(cc, cpm);

                TextView t = new TextView(context);
                t.setId(ID_TEXTVIEW);
                t.setTextColor(Color.WHITE);
                t.setGravity(Gravity.CENTER);
                t.setTextSize(26);
                t.setFocusable(false);
                t.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                t.setSingleLine(true);
                LinearLayout.LayoutParams tpm = new LinearLayout.LayoutParams(getDensityPX(90),
                		getDensityPX(XgimiMenuView.MenuItemHeight));
                tpm.setMargins(getDensityPX(5), 0, 0, 0);
                layout.addView(t, tpm);

                ImageView c1 = new ImageView(context);
                c1.setFocusable(false);
                c1.setBackgroundResource(R.drawable.ic_right);
                LinearLayout.LayoutParams cpm1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                cpm1.setMargins(getDensityPX(5), 0, 0, 0);
                layout.addView(c1, cpm1);

                TextView tv1 = new TextView(context);
                tv1.setFocusable(false);
                tv1.setText("ms");
                tv1.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams cpm2 = new LinearLayout.LayoutParams(getDensityPX(25), getDensityPX(20));
                cpm2.setMargins(getDensityPX(5), 0, 0, 0);
                layout.addView(tv1, cpm2);

                MenuHolder h = new MenuHolder();
                h.valueTextView = t;
                layout.setTag(h);
            }

            return layout;
        }


        OnClickListener leftSwitchClicked = new OnClickListener() {

            @Override
            public void onClick(View v) {
                MenuHolder h = (MenuHolder) v.getTag();
                XgimiMenu.Menu m = h.menu;
                if (listener != null && (m.type == XgimiMenu.Type_IntBox || m.type == XgimiMenu.Type_TextBox )) {
                    m.previousValue();
                    if (m.type == XgimiMenu.Type_IntBox) {
                        h.valueTextView.setText(String.valueOf(m.curValue));
                    } else if (m.type == XgimiMenu.Type_TextBox) {
                        h.valueTextView.setText(m.curText);
                    } 
                    listener.OnMenuValueChanged(m);
                }
            }
        };

        OnClickListener rightSwitchClicked = new OnClickListener() {

            @Override
            public void onClick(View v) {
                MenuHolder h = (MenuHolder) v.getTag();
                XgimiMenu.Menu m = h.menu;
                if (listener != null && (m.type == XgimiMenu.Type_IntBox || m.type == XgimiMenu.Type_TextBox)  ) {
                    m.nextValue();
                    if (m.type == XgimiMenu.Type_IntBox) {
                        h.valueTextView.setText(String.valueOf(m.curValue));
                    } else if (m.type == XgimiMenu.Type_TextBox) {
                        h.valueTextView.setText(m.curText);
                    } 
                    listener.OnMenuValueChanged(m);
                }
            }
        };

        public void setOnMenuListener(XgimiMenuView.OnMenuListener listener) {
            this.listener = listener;
        }

        private class MenuHolder {
            ImageView imageView;
            TextView textView;
            CheckBox checkBox;
            TextView valueTextView;
            ImageView switchLeftImageView;
            ImageView switchRightImageView;

            XgimiMenu.Menu menu;
        }
    }

    public static class MenuAdapter extends BaseMenuAdapter {
        public MenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus) {
            super(c, menus);
        }

        public MenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus, OnGetViewSave onGetViewSave) {
            super(c, menus);
        }
    }

    public static class SubMenuAdapter extends BaseMenuAdapter {
        public SubMenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus) {
            super(c, menus);
        }

        public SubMenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus, OnGetViewSave onGetViewSave) {
            super(c, menus);
            super.setOnGetViewSave(onGetViewSave);
        }

        public void update(ArrayList<XgimiMenu.Menu> menus) {
            this.menus = menus;
            this.notifyDataSetChanged();
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            return v;
        }
    }

    public static class SubSubMenuAdapter extends BaseMenuAdapter {
        public SubSubMenuAdapter(Context c, ArrayList<XgimiMenu.Menu> menus) {
            super(c, menus);
        }

        public void update(ArrayList<XgimiMenu.Menu> menus) {
            this.menus = menus;
            this.notifyDataSetChanged();
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            return v;
        }
    }
}
