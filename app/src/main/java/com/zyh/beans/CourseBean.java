package com.zyh.beans;

import java.util.List;



public class CourseBean {
    private String code;
    private String msg;
    private List<List<Course>> data;


    public class Course{
        private String courseName;
        private String teacher;
        private String time;
        private String address;

        public Course(String courseName, String teacher, String time, String address) {
            this.courseName = courseName;
            this.teacher = teacher;
            this.time = time;
            this.address = address;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<List<Course>> getData() {
        return data;
    }

    public void setData(List<List<Course>> data) {
        this.data = data;
    }
}
