package com.jiyou.jydudailib.api.model;

import org.json.JSONException;
import org.json.JSONObject;

public class JYDPayParam {

    private String cpBill = "";
    private String productId = "";
    private String productName = "";
    private String productDesc = "";
    private int price = 0; //分
    private String serverId = "0";
    private String serverName = "";
    private String roleId = "0";
    private String roleName = "";
    private int roleLevel = 0;
    private String payNotifyUrl = "";
    private String vip = "";
    private String orderID = "";
    private String extension = "";
    private String payType = "office";
    private int retryCount = 0;
    private long createTime = 0;
    private String userId = "";

    public String getUserid() {
        return userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    public String getCpBill() {
        return cpBill;
    }

    public void setCpBill(String cpBill) {
        if (cpBill != null) {
            this.cpBill = cpBill;
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        if (productId != null) {
            this.productId = productId;
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null) {
            this.productName = productName;
        }
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        if (productDesc != null) {
            this.productDesc = productDesc;
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        if (serverId != null) {
            this.serverId = serverId;
        }
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        if (serverName != null) {
            this.serverName = serverName;
        }
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        if (roleId != null) {
            this.roleId = roleId;
        }
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        if (roleName != null) {
            this.roleName = roleName;
        }
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        if (payNotifyUrl != null) {
            this.payNotifyUrl = payNotifyUrl;
        }
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        if (vip != null) {
            this.vip = vip;
        }
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        if (orderID != null) {
            this.orderID = orderID;
        }
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String type) {
        payType = type;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        if (extension != null) {
            this.extension = extension;
        }
    }

    //订单重试次数
    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int count) {
        retryCount = count;
    }

    //订单创建时间
    public long getCreateTime() {
        return createTime;
    }

    public void setCreteTime(long time) {
        createTime = time;
    }

    @Override
    public String toString() {
        return "JYPayParam [" +
                "\n游戏方订单，字符串类型:\n  cpBill=" + cpBill +
                "\n游戏方传入userId，字符串类型:\n  userid=" + userId +
                ",\n价格(单位元)(float 类型):\n  price=" + price +
                ",\n商品标识 ，字符串类型:\n  productId=" + productId +
                ",\n商品名称，字符串类型:\n  productName=" + productName +
                ",\n商品说明，字符串类型:\n  productDesc=" + productDesc +
                ",\n服务器ID，字符串类型:\n  serverId=" + serverId +
                ",\n服务器名称，字符串类型:\n  serverName=" + serverName +
                ",\n角色ID，字符串类型:\n  roleId=" + roleId +
                ",\n角色名字，字符串类型:\n  roleName=" + roleName +
                ",\n角色等级,int类型:\n  roleLevel=" + roleLevel +
                ",\n支付回调地址,选接:\n  payNotifyUrl=" + payNotifyUrl +
                ",\n游戏角色VIP等级,选接:\n  vip=" + vip +
                ",\nsdk订单,游戏方不用接入:\n  orderID=" + orderID +
                ",\n游戏支付扩展字段:\n  extension=" + extension + ",\n]";
    }

    public String toJsonString() {
        JSONObject js = new JSONObject();
        try {
            js.put("cpBill", cpBill);
            js.put("userId", userId);
            js.put("productId", productId);
            js.put("productName", productName);
            js.put("productDesc", productDesc);
            js.put("price", price);
            js.put("serverId", serverId);
            js.put("serverName", serverName);
            js.put("roleId", roleId);
            js.put("roleName", roleName);
            js.put("roleLevel", roleLevel);
            js.put("payNotifyUrl", payNotifyUrl);
            js.put("vip", vip);
            js.put("orderID", orderID);
            js.put("retryCount", retryCount);
            js.put("createTime", createTime);
            js.put("extension", extension);
            js.put("payType", payType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();
    }

    public void JsontoObj(String jsstr) {

        try {
            JSONObject js = new JSONObject(jsstr);
            cpBill = js.has("cpBill") ? js.getString("cpBill") : "";
            productId = js.has("productId") ? js.getString("productId") : "";
            productName = js.has("productName") ? js.getString("productName") : "";
            productDesc = js.has("productDesc") ? js.getString("productDesc") : "";
            price = (js.has("price") ? js.getInt("price") : 0);
            serverId = js.has("serverId") ? js.getString("serverId") : "0";
            serverName = js.has("serverName") ? js.getString("serverName") : "";
            roleId = js.has("roleId") ? js.getString("roleId") : "0";
            roleName = js.has("roleName") ? js.getString("roleName") : "";
            roleLevel = js.has("roleLevel") ? js.getInt("roleLevel") : 1;
            payNotifyUrl = js.has("payNotifyUrl") ? js.getString("payNotifyUrl") : "";
            vip = js.has("vip") ? js.getString("vip") : "";
            orderID = js.has("orderID") ? js.getString("orderID") : "";
            retryCount = js.has("retryCount") ? js.getInt("retryCount") : 0;
            createTime = js.has("createTime") ? js.getLong("createTime") : 0;
            extension = js.has("extension") ? js.getString("extension") : "";
            payType = js.has("payType") ? js.getString("payType") : "office";
        } catch (JSONException ex) {

        }
    }
}
