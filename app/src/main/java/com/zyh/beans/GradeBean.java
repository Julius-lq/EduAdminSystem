package com.zyh.beans;

import java.util.List;




public class GradeBean {
    private String code;
    private String msg;
    private List<Datas> data;

    public static class Datas{
        private String xueqi;
        private String courseName;
        private String score;
        private String type;
        private String xuefen;
        private String point;
        private String method;
        private String property;
        private String pscjUrl;
        private String nature;

        public String getXueqi() {
            return xueqi;
        }

        public void setXueqi(String xueqi) {
            this.xueqi = xueqi;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getXuefen() {
            return xuefen;
        }

        public void setXuefen(String xuefen) {
            this.xuefen = xuefen;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getPscjUrl() {
            return pscjUrl;
        }

        public void setPscjUrl(String pscjUrl) {
            this.pscjUrl = pscjUrl;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        @Override
        public String toString() {
            return "Datas{" +
                    "xueqi='" + xueqi + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", score='" + score + '\'' +
                    ", type='" + type + '\'' +
                    ", xuefen='" + xuefen + '\'' +
                    ", point='" + point + '\'' +
                    ", method='" + method + '\'' +
                    ", property='" + property + '\'' +
                    ", pscjUrl='" + pscjUrl + '\'' +
                    ", nature='" + nature + '\'' +
                    '}';
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

    public List<Datas> getData() {
        return data;
    }

    public void setData(List<Datas> data) {
        this.data = data;
    }
}
