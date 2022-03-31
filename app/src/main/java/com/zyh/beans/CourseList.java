package com.zyh.beans;


import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CourseList extends LitePalSupport {
    private String username;
    private String semester;
    public List<String> courseResponseDatas;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<String> getCourseResponseDatas() {
        return courseResponseDatas;
    }

    public void setCourseResponseDatas(List<String> courseResponseDatas) {
        this.courseResponseDatas = courseResponseDatas;
    }
}
