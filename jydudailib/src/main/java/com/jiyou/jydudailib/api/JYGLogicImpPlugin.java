package com.jiyou.jydudailib.api;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.jiyou.general.callback.JYGCallback;
import com.jiyou.general.model.JYPayParam;
import com.jiyou.general.model.JYRoleParam;
import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.model.JYDPayParam;
import com.jiyou.jydudailib.api.model.JYDRoleParam;
import com.jiyou.jydudailib.base.IDDLogic;
import com.jiyou.jygeneralimp.api.JYGLogicImp;

public class JYGLogicImpPlugin implements IDDLogic {
    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        JYGLogicImp.getInstance().onCreate(context, savedInstanceState);
    }

    @Override
    public void onStart(Context context) {
        JYGLogicImp.getInstance().onStart(context);
    }

    @Override
    public void onRestart(Context context) {
        JYGLogicImp.getInstance().onRestart(context);
    }

    @Override
    public void onResume(Context context) {
        JYGLogicImp.getInstance().onResume(context);
    }

    @Override
    public void onPause(Context context) {
        JYGLogicImp.getInstance().onPause(context);
    }

    @Override
    public void onStop(Context context) {
        JYGLogicImp.getInstance().onStop(context);
    }

    @Override
    public void onDestroy(Context context) {
        JYGLogicImp.getInstance().onDestroy(context);
    }

    @Override
    public void onNewIntent(Context context, Intent newIntent) {
        JYGLogicImp.getInstance().onNewIntent(context, newIntent);
    }

    @Override
    public void onSaveInstanceState(Context context, Bundle outState) {
        JYGLogicImp.getInstance().onSaveInstanceState(context, outState);
    }

    @Override
    public void onConfigurationChanged(Context context, Configuration newConfig) {
        JYGLogicImp.getInstance().onConfigurationChanged(context, newConfig);
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        JYGLogicImp.getInstance().onActivityResult(context, requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed(Context context) {
        return JYGLogicImp.getInstance().onBackPressed(context);
    }

    @Override
    public void init(Context context, final JYDCallback callback) {
        JYGLogicImp.getInstance().init(context, new JYGCallback() {
            @Override
            public void callback(int i, Object o) {
                callback.callback(i, o);
            }
        });
    }

    @Override
    public void login(Context context, final JYDCallback callback) {
        JYGLogicImp.getInstance().login(context, new JYGCallback() {
            @Override
            public void callback(int i, Object o) {
                callback.callback(i, o);
            }
        });
    }

    @Override
    public void logout(Context context, final JYDCallback callback) {
        JYGLogicImp.getInstance().logout(context, new JYGCallback() {
            @Override
            public void callback(int i, Object o) {
                callback.callback(i, o);
            }
        });
    }

    @Override
    public void pay(Context context, JYDPayParam payParam) {
        JYPayParam jyPayParam = new JYPayParam();
        jyPayParam.setUserid(payParam.getUserid());
        jyPayParam.setCpBill(payParam.getCpBill());//cp（游戏方）订单
        jyPayParam.setProductId(payParam.getProductId());//商品标识
        jyPayParam.setProductName(payParam.getProductName());//商品名称
        jyPayParam.setProductDesc(payParam.getProductDesc());//商品说明
        jyPayParam.setServerId(payParam.getServerId());//服务器ID
        jyPayParam.setServerName(payParam.getServerName());//服务器名字
        jyPayParam.setRoleId(payParam.getRoleId());//角色ID
        jyPayParam.setRoleName(payParam.getRoleName());// 角色名字
        jyPayParam.setRoleLevel(payParam.getRoleLevel());//角色等级
        jyPayParam.setPrice(payParam.getPrice() / 100f);// 价格(分)
        jyPayParam.setPayNotifyUrl(payParam.getPayNotifyUrl());
        jyPayParam.setExtension(payParam.getExtension());//会原样返回给游戏
        JYGLogicImp.getInstance().pay(context, jyPayParam);
    }

    @Override
    public void createRole(Context context, JYDRoleParam param) {
        JYRoleParam roleParam = new JYRoleParam();
        roleParam.setRoleId(param.getRoleId());//角色ID
        roleParam.setRoleName(param.getRoleName());// 角色名字
        roleParam.setRoleLevel(param.getRoleLevel());//角色等级
        roleParam.setServerId(param.getServerId());//服务器ID
        roleParam.setServerName(param.getServerName());//服务器名字
        roleParam.setRoleCreateTime(param.getRoleCreateTime());//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYGLogicImp.getInstance().createRole(context, roleParam);
    }

    @Override
    public void enterGame(Context context, JYDRoleParam param) {
        JYRoleParam roleParam = new JYRoleParam();
        roleParam.setRoleId(param.getRoleId());//角色ID
        roleParam.setRoleName(param.getRoleName());// 角色名字
        roleParam.setRoleLevel(param.getRoleLevel());//角色等级
        roleParam.setServerId(param.getServerId());//服务器ID
        roleParam.setServerName(param.getServerName());//服务器名字
        roleParam.setRoleCreateTime(param.getRoleCreateTime());//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYGLogicImp.getInstance().enterGame(context, roleParam);
    }

    @Override
    public void roleUpLevel(Context context, JYDRoleParam param) {
        JYRoleParam roleParam = new JYRoleParam();
        roleParam.setRoleId(param.getRoleId());//角色ID
        roleParam.setRoleName(param.getRoleName());// 角色名字
        roleParam.setRoleLevel(param.getRoleLevel());//角色等级
        roleParam.setServerId(param.getServerId());//服务器ID
        roleParam.setServerName(param.getServerName());//服务器名字
        roleParam.setRoleCreateTime(param.getRoleCreateTime());//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYGLogicImp.getInstance().roleUpLevel(context, roleParam);
    }

    @Override
    public void loginAuthNotify(Context context, String str) {
    }

    @Override
    public void getGameUrl(Context context, JYDCallback callback) {

    }
}
