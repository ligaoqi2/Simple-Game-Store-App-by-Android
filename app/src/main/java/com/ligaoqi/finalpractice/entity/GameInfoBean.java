package com.ligaoqi.finalpractice.entity;

public class GameInfoBean {
    public int id;
    public String gameName;
    public String icon;
    public String introduction;
    public String brief;
    public String tags;
    public String playNumFormat;
    public String versionName;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPlayNumFormat() {
        return playNumFormat;
    }

    public void setPlayNumFormat(String playNumFormat) {
        this.playNumFormat = playNumFormat;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
