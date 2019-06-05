
package com.jiyou.jydudailib.api.model;

import java.util.HashMap;

public class JYDRoleParam {

    private String serverId = "0";
    private String serverName = "0";
    private String roleId = "0";
    private String roleName = "0";
    private String roleCreateTime = "0";
    private String roleLevelTime = "0";
    private int roleLevel = 0;

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

    public void setRoleCreateTime(String roleCreateTime) {
        if (roleCreateTime != null) {
            this.roleCreateTime = roleCreateTime;
        }
    }

    public String getRoleCreateTime() {
        return roleCreateTime;
    }

    public void setRoleLevelTime(String roleLevelTime) {
        if (roleLevelTime != null) {
            this.roleLevelTime = roleLevelTime;
        }
    }

    public String getRoleLevelTime() {
        return roleLevelTime;
    }

    @Override
    public String toString() {
        return "JYDRoleParam [" +
                ",\n服务器ID，字符串类型:\n  serverId=" + serverId +
                ",\n服务器名称，字符串类型:\n  serverName=" + serverName +
                ",\n角色ID，字符串类型:\n  roleId=" + roleId +
                ",\n角色名字，字符串类型:\n  roleName=" + roleName +
                ",\n角色等级, int类型:\n  roleLevel=" + roleLevel +
                ",\n角色创建时间,字符串类型:\n  roleCreateTime=" + roleCreateTime +
                ",\n角色升级时间,字符串类型:\n  roleLevelTime=" + roleLevelTime + ",\n]";
    }

    public static JYDRoleParam mapToJYRoleParam(HashMap hashMap) {
        HashMap<String, String> hashMap1 = hashMap;
        JYDRoleParam roleParam = new JYDRoleParam();
        roleParam.setRoleId(hashMap1.get("roleId"));//角色ID，字符串类型
        roleParam.setRoleName(hashMap1.get("roleName"));// 角色名字，字符串类型
        roleParam.setRoleLevel(Integer.parseInt(hashMap1.get("roleLevel")));//角色等级，int类型
        roleParam.setServerId(hashMap1.get("serverId"));//服务器ID，字符串类型
        roleParam.setServerName(hashMap1.get("serverName")); //服务器名字，字符串类型
        //获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        roleParam.setRoleCreateTime(hashMap1.get("roleCreateTime"));
        return roleParam;
    }

}
