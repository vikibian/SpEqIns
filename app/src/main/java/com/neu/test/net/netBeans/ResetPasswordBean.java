package com.neu.test.net.netBeans;

public class ResetPasswordBean {

    /**
     * result : {"content":null,"message":"更改成功"}
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
         * message : 更改成功
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
