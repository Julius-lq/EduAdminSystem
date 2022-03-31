package com.zyh.beans;

import java.io.Serializable;


public class LoginBean implements Serializable {
    private String code;
    private Datas data;


    public class Datas implements Serializable {
        private String cookie;
        private String token;
        private String nowXueqi;
        private StuInfo stuInfo;
        private String nowDate;
        private String nowWeek;
        private String totalWeek;


        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNowXueqi() {
            return nowXueqi;
        }

        public void setNowXueqi(String nowXueqi) {
            this.nowXueqi = nowXueqi;
        }

        public StuInfo getStuInfo() {
            return stuInfo;
        }

        public void setStuInfo(StuInfo stuInfo) {
            this.stuInfo = stuInfo;
        }

        public String getNowDate() {
            return nowDate;
        }

        public void setNowDate(String nowDate) {
            this.nowDate = nowDate;
        }

        public String getNowWeek() {
            return nowWeek;
        }

        public void setNowWeek(String nowWeek) {
            this.nowWeek = nowWeek;
        }

        public String getTotalWeek() {
            return totalWeek;
        }

        public void setTotalWeek(String totalWeek) {
            this.totalWeek = totalWeek;
        }


        public class StuInfo implements Serializable {
            private String id;
            private String name;
            private String stuId;
            private String college;
            private String major;
            private String className;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStuId() {
                return stuId;
            }

            public void setStuId(String stuId) {
                this.stuId = stuId;
            }

            public String getCollege() {
                return college;
            }

            public void setCollege(String college) {
                this.college = college;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }
        }
    }

    public LoginBean(String code, Datas data) {
        this.code = code;
        this.data = data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Datas getData() {
        return data;
    }
}
