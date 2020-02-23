package com.neu.test.net.netBeans;

import java.util.List;

public class SelectItemResultBean {

    /**
     * result : {"content":[{"CHECKCONTENT":"现场作业人员是否具有有效证件","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"是否有安全检验合格标志，并按规定固定在电梯的显著位置，是否在检验有效期内","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"电梯内设置的报警装置是否可靠，联系是否畅通","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"自动扶梯和自动人行道入口处是否有安全开关并灵敏可靠","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"是否有维保记录","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"维保周期是否符合规定","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"}],"message":"获取成功"}
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
         * content : [{"CHECKCONTENT":"现场作业人员是否具有有效证件","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"是否有安全检验合格标志，并按规定固定在电梯的显著位置，是否在检验有效期内","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"电梯内设置的报警装置是否可靠，联系是否畅通","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"自动扶梯和自动人行道入口处是否有安全开关并灵敏可靠","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"是否有维保记录","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"},{"CHECKCONTENT":"维保周期是否符合规定","DEVCLASS":"3000","DEVID":"123456","REFHIM":"","REFJVI":"","REGISTERCODE":"","RUNWATERNUM":"","STATUS":"1","TASKID":"1affb4ca-1b34-4d99-9222-5ce1ed62afa5"}]
         * message : 获取成功
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
             * CHECKCONTENT : 现场作业人员是否具有有效证件
             * DEVCLASS : 3000
             * DEVID : 123456
             * REFHIM :
             * REFJVI :
             * REGISTERCODE :
             * RUNWATERNUM :
             * STATUS : 1
             * TASKID : 1affb4ca-1b34-4d99-9222-5ce1ed62afa5
             */

            private String CHECKCONTENT;
            private String DEVCLASS;
            private String DEVID;
            private String REFHIM;
            private String REFJVI;
            private String REGISTERCODE;
            private String RUNWATERNUM;
            private String STATUS;
            private String TASKID;

            public String getCHECKCONTENT() {
                return CHECKCONTENT;
            }

            public void setCHECKCONTENT(String CHECKCONTENT) {
                this.CHECKCONTENT = CHECKCONTENT;
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

            public String getREFHIM() {
                return REFHIM;
            }

            public void setREFHIM(String REFHIM) {
                this.REFHIM = REFHIM;
            }

            public String getREFJVI() {
                return REFJVI;
            }

            public void setREFJVI(String REFJVI) {
                this.REFJVI = REFJVI;
            }

            public String getREGISTERCODE() {
                return REGISTERCODE;
            }

            public void setREGISTERCODE(String REGISTERCODE) {
                this.REGISTERCODE = REGISTERCODE;
            }

            public String getRUNWATERNUM() {
                return RUNWATERNUM;
            }

            public void setRUNWATERNUM(String RUNWATERNUM) {
                this.RUNWATERNUM = RUNWATERNUM;
            }

            public String getSTATUS() {
                return STATUS;
            }

            public void setSTATUS(String STATUS) {
                this.STATUS = STATUS;
            }

            public String getTASKID() {
                return TASKID;
            }

            public void setTASKID(String TASKID) {
                this.TASKID = TASKID;
            }
        }
    }
}
