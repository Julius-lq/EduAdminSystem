package com.zyh.beans;

import android.widget.TextView;




public class CourseId {
    private int courseNameId;
    private int courseAddressId;
    private int coursePropertyId;

    public CourseId(int courseNameId, int courseAddressId, int coursePropertyId) {
        this.courseNameId = courseNameId;
        this.courseAddressId = courseAddressId;
        this.coursePropertyId = coursePropertyId;
    }

    public int getCourseNameId() {
        return courseNameId;
    }

    public void setCourseNameId(int courseNameId) {
        this.courseNameId = courseNameId;
    }

    public int getCourseAddressId() {
        return courseAddressId;
    }

    public void setCourseAddressId(int courseAddressId) {
        this.courseAddressId = courseAddressId;
    }

    public int getCoursePropertyId() {
        return coursePropertyId;
    }

    public void setCoursePropertyId(int coursePropertyId) {
        this.coursePropertyId = coursePropertyId;
    }
}
