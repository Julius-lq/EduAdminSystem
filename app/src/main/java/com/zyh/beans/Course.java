package com.zyh.beans;

import android.widget.TextView;




public class Course {
    private TextView courseName;
    private TextView courseAddress;
    private TextView courseProperty;

    public Course(TextView courseName, TextView courseAddress, TextView courseProperty) {
        this.courseName = courseName;
        this.courseAddress = courseAddress;
        this.courseProperty = courseProperty;
    }

    public TextView getCourseName() {
        return courseName;
    }

    public void setCourseName(TextView courseName) {
        this.courseName = courseName;
    }

    public TextView getCourseAddress() {
        return courseAddress;
    }

    public void setCourseAddress(TextView courseAddress) {
        this.courseAddress = courseAddress;
    }

    public TextView getCourseProperty() {
        return courseProperty;
    }

    public void setCourseProperty(TextView courseProperty) {
        this.courseProperty = courseProperty;
    }
}
