package com.jiyou.jydudailib.model;

public class JYDuDaiGameUrlBean {

    /**
     * status : success
     * code : 0
     * data : {"game_url":"http://sdk.7yol.cn/static/iframePage.html"}
     * message : Sign Error.
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * game_url : http://sdk.7yol.cn/static/iframePage.html
         */

        private String game_url;

        public String getGame_url() {
            return game_url;
        }

        public void setGame_url(String game_url) {
            this.game_url = game_url;
        }
    }
}
