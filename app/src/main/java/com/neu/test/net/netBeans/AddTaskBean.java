package com.neu.test.net.netBeans;

public class AddTaskBean {

    /**
     * result : {"content":null,"message":"添加任务成功"}
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
         * content : null
         * message : 添加任务成功
         */

        private Object content;
        private String message;

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
