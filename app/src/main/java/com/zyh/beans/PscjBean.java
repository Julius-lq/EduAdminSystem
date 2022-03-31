package com.zyh.beans;

import java.util.List;


public class PscjBean {
    private String code;
    private String msg;
    private Datas data;

    public class Datas{
        private String pscj;
        private String pscjBL;
        private String qmcj;
        private String qmcjBL;
        private String qzcj;
        private String qzcjBL;

        public String getPscj() {
            return pscj;
        }

        public void setPscj(String pscj) {
            this.pscj = pscj;
        }

        public String getPscjBL() {
            return pscjBL;
        }

        public void setPscjBL(String pscjBL) {
            this.pscjBL = pscjBL;
        }

        public String getQmcj() {
            return qmcj;
        }

        public void setQmcj(String qmcj) {
            this.qmcj = qmcj;
        }

        public String getQmcjBL() {
            return qmcjBL;
        }

        public void setQmcjBL(String qmcjBL) {
            this.qmcjBL = qmcjBL;
        }

        public String getQzcj() {
            return qzcj;
        }

        public void setQzcj(String qzcj) {
            this.qzcj = qzcj;
        }

        public String getQzcjBL() {
            return qzcjBL;
        }

        public void setQzcjBL(String qzcjBL) {
            this.qzcjBL = qzcjBL;
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

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }
}
