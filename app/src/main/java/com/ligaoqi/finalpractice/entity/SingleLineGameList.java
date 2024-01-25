package com.ligaoqi.finalpractice.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class SingleLineGameList implements MultiItemEntity {
    public String moduleCode;
    public String moduleName;
    public int style;
    public List<GameInfoBean> gameInfoList;
    public boolean isTheme;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

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

    public boolean isTheme() {
        return isTheme;
    }

    public void setTheme(boolean theme) {
        isTheme = theme;
    }


    @Override
    public int getItemType() {
        if(isTheme){
            return 0;
        }else{
            return style;
        }
    }
}
