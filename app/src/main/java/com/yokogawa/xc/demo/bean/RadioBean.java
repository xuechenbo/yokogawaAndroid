package com.yokogawa.xc.demo.bean;


public class RadioBean {

    private RadioBean.LastProject lastProject;

    public RadioBean.LastProject getLastProject() {
        return lastProject;
    }

    public void setLastProject(RadioBean.LastProject lastProject) {
        this.lastProject = lastProject;
    }

    public static class LastProject {
        @Override
        public String toString() {
            return "{" +
                    "isShow=" + isShow +
                    ", value='" + value + '\'' +
                    '}';
        }

        private boolean isShow;
        private String value;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
