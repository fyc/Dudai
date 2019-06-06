package com.jiyou.jydudailib.model;

public class JYDuOrderBean {

    /**
     * status : success
     * code : 0
     * data : {"order_sn":"201905291000000898020000","callback_url":"http://zeus.7.io/v1/pay/callback/1/1/1"}
     * message: Sign Error.
     */

    private String status;
    private int code;
    private DataBean data;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * order_sn : 201905291000000898020000
         * callback_url : http://zeus.7.io/v1/pay/callback/1/1/1
         */

        private String order_sn;
        private String callback_url;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getCallback_url() {
            return callback_url;
        }

        public void setCallback_url(String callback_url) {
            this.callback_url = callback_url;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
