package com.neu.test.net.netBeans;

import java.util.List;

public class GetTaskBean {

    /**
     * result : {"content":[{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31245","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"0183728a-b5ce-4182-aae3-936598fae3d9","TASKTYPE":"自查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31248","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"2c40f185-4150-4686-8477-74ed192eaee8","TASKTYPE":"复查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31252","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"e9a3b31e-f616-421f-9e27-c8c66b1f89a4","TASKTYPE":"复查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"27797","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"05aef5e3-7f74-430b-b0e9-00638f467bfa","TASKTYPE":"随机"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"27798","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"ec25e19d-82eb-4c9a-9308-74888e11b60c","TASKTYPE":"随机"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28025","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"0b417591-9daa-45ee-b3bb-6f2aa7d0a047","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28026","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"95136290-851e-4916-8307-747ef34c8e19","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28027","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"5d165d68-a0e9-4e41-885c-b88b16dbc95f","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31243","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"2093aa9e-d1d7-493f-87f7-205dc139d9db","TASKTYPE":"自查"}],"message":"获取任务成功"}
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
         * content : [{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31245","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"0183728a-b5ce-4182-aae3-936598fae3d9","TASKTYPE":"自查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31248","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"2c40f185-4150-4686-8477-74ed192eaee8","TASKTYPE":"复查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31252","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"e9a3b31e-f616-421f-9e27-c8c66b1f89a4","TASKTYPE":"复查"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"27797","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"05aef5e3-7f74-430b-b0e9-00638f467bfa","TASKTYPE":"随机"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"27798","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"ec25e19d-82eb-4c9a-9308-74888e11b60c","TASKTYPE":"随机"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28025","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"0b417591-9daa-45ee-b3bb-6f2aa7d0a047","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28026","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"95136290-851e-4916-8307-747ef34c8e19","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"28027","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"5d165d68-a0e9-4e41-885c-b88b16dbc95f","TASKTYPE":"上级"},{"CHECKDATE":null,"DEADLINE":null,"DEVCLASS":"3000","DEVID":"31243","LOGINNAME":"test","NEXT_INSSEIFTIME":null,"RESULT":"0","TASKID":"2093aa9e-d1d7-493f-87f7-205dc139d9db","TASKTYPE":"自查"}]
         * message : 获取任务成功
         */

        private String message;
        private List<ContentBean> content;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * CHECKDATE : null
             * DEADLINE : null
             * DEVCLASS : 3000
             * DEVID : 31245
             * LOGINNAME : test
             * NEXT_INSSEIFTIME : null
             * RESULT : 0
             * TASKID : 0183728a-b5ce-4182-aae3-936598fae3d9
             * TASKTYPE : 自查
             */

            private Object CHECKDATE;
            private Object DEADLINE;
            private String DEVCLASS;
            private String DEVID;
            private String LOGINNAME;
            private Object NEXT_INSSEIFTIME;
            private String RESULT;
            private String TASKID;
            private String TASKTYPE;

            public Object getCHECKDATE() {
                return CHECKDATE;
            }

            public void setCHECKDATE(Object CHECKDATE) {
                this.CHECKDATE = CHECKDATE;
            }

            public Object getDEADLINE() {
                return DEADLINE;
            }

            public void setDEADLINE(Object DEADLINE) {
                this.DEADLINE = DEADLINE;
            }

            public String getDEVCLASS() {
                return DEVCLASS;
            }

            public void setDEVCLASS(String DEVCLASS) {
                this.DEVCLASS = DEVCLASS;
            }

            public String getDEVID() {
                return DEVID;
            }

            public void setDEVID(String DEVID) {
                this.DEVID = DEVID;
            }

            public String getLOGINNAME() {
                return LOGINNAME;
            }

            public void setLOGINNAME(String LOGINNAME) {
                this.LOGINNAME = LOGINNAME;
            }

            public Object getNEXT_INSSEIFTIME() {
                return NEXT_INSSEIFTIME;
            }

            public void setNEXT_INSSEIFTIME(Object NEXT_INSSEIFTIME) {
                this.NEXT_INSSEIFTIME = NEXT_INSSEIFTIME;
            }

            public String getRESULT() {
                return RESULT;
            }

            public void setRESULT(String RESULT) {
                this.RESULT = RESULT;
            }

            public String getTASKID() {
                return TASKID;
            }

            public void setTASKID(String TASKID) {
                this.TASKID = TASKID;
            }

            public String getTASKTYPE() {
                return TASKTYPE;
            }

            public void setTASKTYPE(String TASKTYPE) {
                this.TASKTYPE = TASKTYPE;
            }
        }
    }
}
