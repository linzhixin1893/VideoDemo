package com.xgimi.videodemo.menu;


import java.util.ArrayList;

public class XgimiMenu {

    public static final int Type_Text = 0;//纯文本
    public static final int Type_IconText = 1;//图标+文字
    public static final int Type_IntBox = 2;//数字左右选择
    public static final int Type_TextBox = 3;//文本左右选择
    public static final int Type_CheckBox = 4;//勾选框
    public static final int Type_SubtitleSysc = 5;//勾选框
    public static final int Type_Int = 6;//数字左右选择
    public ArrayList<Menu> menus = new ArrayList<Menu>();

    public void addMenu(Menu m) {
        menus.add(m);
    }

    public void clear() {
        menus.clear();
    }

    public static class Menu {
        public Menu parentMenu = null;//
        public String name;//字
        public boolean selected = false;
        public int type;//类型
        public int listenType;//类型
        public int curValue;
        public int minValue;
        public int maxValue;
        public ArrayList<String> texts;
        public String curText;
        public int icon;
        public int thirdId;

        private String currentStrValue;
        private int currentValue;
        public ArrayList<Menu> subMenus = new ArrayList<Menu>();
        public Menu() {
        }

        public int getListenType() {

            return listenType;
        }


        public int getThirdId() {
            return thirdId;
        }

        public void setThirdId(int thirdId) {
            this.thirdId = thirdId;
        }

        public void setListenType(int listenType) {
            this.listenType = listenType;
        }

        public Menu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(Menu parentMenu) {
            this.parentMenu = parentMenu;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCurValue() {
            return curValue;
        }

        public void setCurValue(int curValue) {
            this.curValue = curValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }

        public ArrayList<String> getTexts() {
            return texts;
        }

        public void setTexts(ArrayList<String> texts) {
            this.texts = texts;
        }

        public String getCurText() {
            return curText;
        }

        public void setCurText(String curText) {
            this.curText = curText;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getCurrentStrValue() {
            return currentStrValue;
        }

        public void setCurrentStrValue(String currentStrValue) {
            this.currentStrValue = currentStrValue;
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(int currentValue) {
            this.currentValue = currentValue;
        }

        public ArrayList<Menu> getSubMenus() {
            return subMenus;
        }

        public void setSubMenus(ArrayList<Menu> subMenus) {
            this.subMenus = subMenus;
        }

        public Menu(int a) {
            super();
            this.type = Type_Text;
            this.name = name;
        }

        public Menu(String name, int icon) {
            super();
            this.type = Type_IconText;
            this.name = name;
            this.icon = icon;
        }

        public Menu(String name, boolean selected) {
            super();
            this.type = Type_CheckBox;
            this.name = name;
            this.selected = selected;
        }

        public Menu(String name, int minValue, int maxValue, int curValue) {
            super();
            this.type = Type_IntBox;
            this.name = name;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.curValue = curValue;
        }
        
        public Menu(String name ) {
            super();
            this.type = Type_Int;
            this.name = name;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.curValue = curValue;
        }
        
        public Menu(String name, ArrayList<String> texts, String curText) {
            super();
            this.type = Type_TextBox;
            this.name = name;
            this.texts = texts;
            this.curText = curText;
        }

        public void addSubMenu(Menu menu) {
            subMenus.add(menu);
            menu.parentMenu = this;
        }

        public void addSubMenu(Menu menu,int position) {
            subMenus.add(position,menu);
            menu.parentMenu = this;
        }

        public void setSelected(boolean selected) {
            if (type == Type_CheckBox) {
                this.selected = selected;
            }
        }

        public void nextValue() {
            if (type == Type_IntBox) {
                curValue++;

                if (curValue > maxValue) {
                    curValue = minValue;
                }
                setCurrentValue(curValue);
            } else if (type == Type_TextBox) {
                int pos = texts.indexOf(curText);
                pos++;
                if (pos == texts.size()) {
                    pos = 0;
                }
                curText = texts.get(pos);
            }
        }

        public void previousValue() {
            if (type == Type_IntBox ) {
                curValue--;
                if (curValue < minValue) {
                    curValue = maxValue;
                }
                setCurrentValue(curValue);
            } else if (type == Type_TextBox) {
                int pos = texts.indexOf(curText);
                pos--;
                if (pos == -1) {
                    pos = texts.size() - 1;
                }
                curText = texts.get(pos);
            }
        }
    }
}
