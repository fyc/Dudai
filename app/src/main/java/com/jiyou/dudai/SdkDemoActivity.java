package com.jiyou.dudai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jiyou.jydudailib.api.JYProxySDK;
import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.constants.JYDStatusCode;
import com.jiyou.jydudailib.api.model.JYDPayParam;
import com.jiyou.jydudailib.api.model.JYDRoleParam;
import com.tencent.tmgp.jygamesdkdemo.R;


public class SdkDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static String LOG_TAG = "SdkDemoActivity";
    private Button initButton, loginButton, payButton, logout;
    private Button createRole, enterGame, roleUpLevel;
    private static Activity mActivity = null;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO GAME 游戏需自行检测自身是否重复, 检测到重复的Activity则要把自己finish掉
        // 注意：游戏需要加上去重判断以及finish重复的实例的逻辑，否则可能发生重复拉起游戏的问题。
        if (null != mActivity && !mActivity.equals(this)) {
            Log.d(LOG_TAG, "Warning!Reduplicate game activity was detected.Activity will finish immediately.");
            // TODO GAME 处理游戏被拉起的情况
            JYProxySDK.getInstance().onNewIntent(this, getIntent());
            this.finish();
            return;
        } else {

            // TODO GAME YSDK初始化
//            YSDKApi.onCreate(this);
//            JYProxySDK.getInstance().setActivity(SdkDemoActivity.this);
            JYProxySDK.getInstance().onCreate(this, savedInstanceState);
            // TODO GAME 处理游戏被拉起的情况
//            YSDKApi.handleIntent(this.getIntent());
            JYProxySDK.getInstance().onNewIntent(this, this.getIntent());
        }

        mActivity = this;

        findView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JYProxySDK.getInstance().onNewIntent(this, intent);
    }

    // TODO GAME 在onActivityResult中需要调用YSDKApi.onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JYProxySDK.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        JYProxySDK.getInstance().onStart(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onRestart()
    @Override
    protected void onRestart() {
        super.onRestart();
        JYProxySDK.getInstance().onRestart(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onResume()
    @Override
    protected void onResume() {
        super.onResume();
        JYProxySDK.getInstance().onResume(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onPause()
    @Override
    protected void onPause() {
        super.onPause();
        JYProxySDK.getInstance().onPause(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onStop()
    @Override
    protected void onStop() {
        super.onStop();
        JYProxySDK.getInstance().onStop(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onDestory()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        JYProxySDK.getInstance().onDestroy(this);

    }


    //游戏前必须首先进行初始化：
    //初始化主要进行匹配参数是否符合后台规定，一些赋值操作
    private void init() {
        JYProxySDK.getInstance().init(this, new JYDCallback<String>() {
            @Override
            public void callback(int code, String response) {
                switch (code) {
                    case JYDStatusCode.SUCCESS:
                        loginMethod();
                        Toast.makeText(SdkDemoActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
                        break;
                    case JYDStatusCode.FAILURE:
                        Toast.makeText(SdkDemoActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //登录:
    private void loginMethod() {
        JYProxySDK.getInstance().login(this, new JYDCallback<String>() {
            @Override
            public void callback(int code, String response) {
                switch (code) {
                    case JYDStatusCode.SUCCESS:
                        Toast.makeText(SdkDemoActivity.this, response, Toast.LENGTH_SHORT).show();
                        break;
                    case JYDStatusCode.FAILURE:
                        Toast.makeText(SdkDemoActivity.this, response, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //提交 Player 信息 上传玩家信息
    private void createRoleMethod() {
        JYDRoleParam roleParam = new JYDRoleParam();
        roleParam.setRoleId("1");//角色ID
        roleParam.setRoleName("风雨逍遥");// 角色名字
        roleParam.setRoleLevel(1);//角色等级
        roleParam.setServerId("302");//服务器ID
        roleParam.setServerName("服务器1");//服务器名字
        roleParam.setRoleCreateTime("1456397360");//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYProxySDK.getInstance().createRole(this, roleParam);

    }

    //提交 Player 信息 上传玩家信息
    private void enterGameMethod() {
        JYDRoleParam roleParam = new JYDRoleParam();
        roleParam.setRoleId("1");//角色ID
        roleParam.setRoleName("风雨逍遥");// 角色名字
        roleParam.setRoleLevel(1);//角色等级
        roleParam.setServerId("302");//服务器ID
        roleParam.setServerName("服务器1");//服务器名字
        roleParam.setRoleCreateTime("1456397360");//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYProxySDK.getInstance().enterGame(this, roleParam);

    }

    //提交 Player 信息 上传玩家信息
    private void roleUpLevelMethod() {
        JYDRoleParam roleParam = new JYDRoleParam();
        roleParam.setRoleId("1");//角色ID
        roleParam.setRoleName("风雨逍遥");// 角色名字
        roleParam.setRoleLevel(1);//角色等级
        roleParam.setServerId("302");//服务器ID
        roleParam.setServerName("服务器1");//服务器名字
        roleParam.setRoleCreateTime("1456397360");//获取服务器存储的角色创建时间,时间戳，单位秒，长度10，不可用本地手机时间，同一角色创建时间不可变，上线UC联运必需接入，（字符串类型，sdk 内如会有转换）
        JYProxySDK.getInstance().roleUpLevel(this, roleParam);

    }

    //支付：
    private void payMethod2() {
        JYDPayParam jyPayParam = new JYDPayParam();
        jyPayParam.setUserid("488427");
        jyPayParam.setCpBill(System.currentTimeMillis() + "");//cp（游戏方）订单
        jyPayParam.setProductId("1");//商品标识
        jyPayParam.setProductName("60元宝＝¥6");//商品名称
        jyPayParam.setProductDesc("60元宝＝¥6");//商品说明
        jyPayParam.setServerId("302");//服务器ID
        jyPayParam.setServerName("风起云涌");//服务器名字
        jyPayParam.setRoleId("1");//角色ID
        jyPayParam.setRoleName("风雨逍遥");// 角色名字
        jyPayParam.setRoleLevel(1);//角色等级
        jyPayParam.setPrice(1);// 价格(分)
        jyPayParam.setPayNotifyUrl("htts://xxxxxxx");
        jyPayParam.setExtension("扩展数据");//会原样返回给游戏

        JYProxySDK.getInstance().pay(this, jyPayParam);
    }


    private void logoutMethod() {
        JYProxySDK.getInstance().logout(this, new JYDCallback<String>() {
            @Override
            public void callback(int code, String response) {
                switch (code) {
                    case JYDStatusCode.SUCCESS:
                        Toast.makeText(SdkDemoActivity.this, response, Toast.LENGTH_SHORT).show();
                        break;
                    case JYDStatusCode.FAILURE:
                        Toast.makeText(SdkDemoActivity.this, response, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void findView() {
        initButton = findViewById(R.id.gameInitBtn);
        loginButton = findViewById(R.id.gameLoginBtn);
        payButton = findViewById(R.id.gamePayBtn);
        createRole = findViewById(R.id.createRole);
        enterGame = findViewById(R.id.enterGame);
        roleUpLevel = findViewById(R.id.roleUpLevel);
        logout = findViewById(R.id.logout);

        initButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
        createRole.setOnClickListener(this);
        enterGame.setOnClickListener(this);
        roleUpLevel.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        JYProxySDK.getInstance().onBackPressed(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gameInitBtn:
                init();
                break;
            case R.id.gameLoginBtn:
                loginMethod();
                break;
            case R.id.gamePayBtn:
                payMethod2();
                break;
            case R.id.createRole:
                createRoleMethod();
                break;
            case R.id.enterGame:
                enterGameMethod();
                break;
            case R.id.roleUpLevel:
                roleUpLevelMethod();
                break;
            case R.id.logout:
                logoutMethod();
                break;
        }
    }

}
