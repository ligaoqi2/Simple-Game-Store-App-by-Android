package com.ligaoqi.finalpractice.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class HomeGameListBean implements MultiItemEntity {
    public String moduleCode;
    public String moduleName;
    public int style;
    public List<GameInfoBean> gameInfoList;
    public int total;

    public int size;
    public int current;
    public int pages;

    public boolean isTheme;

    public static final int TYPE_LAYOUT0 = 0;
    public static final int TYPE_LAYOUT1 = 1;
    public static final int TYPE_LAYOUT2 = 2;
    public static final int TYPE_LAYOUT3 = 3;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public List<GameInfoBean> getGameInfoList() {
        return gameInfoList;
    }

    public void setGameInfoList(List<GameInfoBean> gameInfoList) {
        this.gameInfoList = gameInfoList;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public boolean isTheme() {
        return isTheme;
    }

    public void setTheme(boolean theme) {
        isTheme = theme;
    }

    @Override
    public int getItemType() {
        if(isTheme){
            return TYPE_LAYOUT0;
        }else{
            return style;
        }
    }
}
