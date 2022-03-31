package com.zyh.utills;


import org.jetbrains.annotations.NotNull;

/**
 * @author zsw
 * @date 2019/11/19 20:28
 */

public class WeekDay implements Comparable<WeekDay> {
    private Integer weekId;
    private String weekMonStr;

    @Override
    public int compareTo(@NotNull WeekDay o) {
        return this.getWeekId() - o.getWeekId();
    }

    public WeekDay(){

    }

    public WeekDay(Integer weekId, String weekMonStr) {
        this.weekId = weekId;
        this.weekMonStr = weekMonStr;
    }

    public Integer getWeekId() {
        return weekId;
    }

    public void setWeekId(Integer weekId) {
        this.weekId = weekId;
    }

    public String getWeekMonStr() {
        return weekMonStr;
    }

    public void setWeekMonStr(String weekMonStr) {
        this.weekMonStr = weekMonStr;
    }
}
