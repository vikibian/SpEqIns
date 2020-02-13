package com.neu.test.net.netBeans;

public class LoginBean {

    /**
     * result : {"content":{"ACCOUNTSTAUTS":"","DEVCLASS":"","IS_DEL":false,"LOGINNAME":"test","LOGINPWD":"testtest","PERMISSION":"","REGISTATUS":"","ROLEID":"","UNITID":"","USERID":1,"USERNAME":"","USEUNITNAME":""},"message":"密码错误"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * content : {"ACCOUNTSTAUTS":"","DEVCLASS":"","IS_DEL":false,"LOGINNAME":"test","LOGINPWD":"testtest","PERMISSION":"","REGISTATUS":"","ROLEID":"","UNITID":"","USERID":1,"USERNAME":"","USEUNITNAME":""}
         * message : 密码错误
         */

        private ContentBean content;
        private String message;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static class ContentBean {
            /**
             * ACCOUNTSTAUTS :
             * DEVCLASS :
             * IS_DEL : false
             * LOGINNAME : test
             * LOGINPWD : testtest
             * PERMISSION :
             * REGISTATUS :
             * ROLEID :
             * UNITID :
             * USERID : 1
             * USERNAME :
             * USEUNITNAME :
             */

            private String ACCOUNTSTAUTS;
            private String DEVCLASS;
            private boolean IS_DEL;
            private String LOGINNAME;
            private String LOGINPWD;
            private String PERMISSION;
            private String REGISTATUS;
            private String ROLEID;
            private String UNITID;
            private int USERID;
            private String USERNAME;
            private String USEUNITNAME;

            public String getACCOUNTSTAUTS() {
                return ACCOUNTSTAUTS;
            }

            public void setACCOUNTSTAUTS(String ACCOUNTSTAUTS) {
                this.ACCOUNTSTAUTS = ACCOUNTSTAUTS;
            }

            public String getDEVCLASS() {
                return DEVCLASS;
            }

            public void setDEVCLASS(String DEVCLASS) {
                this.DEVCLASS = DEVCLASS;
            }

            public boolean isIS_DEL() {
                return IS_DEL;
            }

            public void setIS_DEL(boolean IS_DEL) {
                this.IS_DEL = IS_DEL;
            }

            public String getLOGINNAME() {
                return LOGINNAME;
            }

            public void setLOGINNAME(String LOGINNAME) {
                this.LOGINNAME = LOGINNAME;
            }

            public String getLOGINPWD() {
                return LOGINPWD;
            }

            public void setLOGINPWD(String LOGINPWD) {
                this.LOGINPWD = LOGINPWD;
            }

            public String getPERMISSION() {
                return PERMISSION;
            }

            public void setPERMISSION(String PERMISSION) {
                this.PERMISSION = PERMISSION;
            }

            public String getREGISTATUS() {
                return REGISTATUS;
            }

            public void setREGISTATUS(String REGISTATUS) {
                this.REGISTATUS = REGISTATUS;
            }

            public String getROLEID() {
                return ROLEID;
            }

            public void setROLEID(String ROLEID) {
                this.ROLEID = ROLEID;
            }

            public String getUNITID() {
                return UNITID;
            }

            public void setUNITID(String UNITID) {
                this.UNITID = UNITID;
            }

            public int getUSERID() {
                return USERID;
            }

            public void setUSERID(int USERID) {
                this.USERID = USERID;
            }

            public String getUSERNAME() {
                return USERNAME;
            }

            public void setUSERNAME(String USERNAME) {
                this.USERNAME = USERNAME;
            }

            public String getUSEUNITNAME() {
                return USEUNITNAME;
            }

            public void setUSEUNITNAME(String USEUNITNAME) {
                this.USEUNITNAME = USEUNITNAME;
            }
        }
    }
}
