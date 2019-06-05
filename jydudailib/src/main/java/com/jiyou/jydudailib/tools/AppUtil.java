package com.jiyou.jydudailib.tools;

import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONObject;

/**
 */
public class AppUtil {
    public static final String platformKey = "dkmGameSdk";
    /**
     * SD卡目录
     */
    public static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 应用根目录
     */
    public static String ROOT_PATH = SDCARD_PATH + "/Android/data/dkmGameSdk/";

    /**
     * 用户数据目录
     */
    public static String USER_DATA_PATH = ROOT_PATH + "/UserData/";
    /**
     * 下载目录
     */
    public static String DOWNLOAD_PATH = SDCARD_PATH + "/dkmGameSdk/downs/";
    /**
     * 配置目录
     */
    public static String CONFIG_PATH = ROOT_PATH + "/Config/";

    /**
     * 配置文件路径
     */
    //存放用户信息
    public static String CONFIG_FILE = CONFIG_PATH + "dkmGameSdk_config.cfg";

    //存放所有app信息
    public static String AKCONFIG_FILE = CONFIG_PATH + "dkmAkGameSdk_config.cfg";

    /**
     * @return String 返回类型
     * @Title: getDownsPath(获取下载目录)
     * @author Jeahong
     * @data 2013年9月4日 下午3:04:38
     */
    public static String getDownsPath() {
        if (!FileUtilEx.isSDCardAvailable()) {
            return "";
        }
        if (FileUtilEx.createFolder(DOWNLOAD_PATH, FileUtilEx.MODE_UNCOVER)) {
            return DOWNLOAD_PATH;
        }
        return "";
    }

    /***
     * 根据关键字获取本地数据
     * @ConfigPath: 存放数据路径
     * @name 存放json key 值
     * @fallback 获取失败返回值
     * @return String 返回类型
     */
    public static String getLocalConfigData(String ConfigPath, String name, String fallback) {
        if (!FileUtilEx.isSDCardAvailable()) {
            return null;
        }
        if (FileUtilEx.isExist(ConfigPath)) {
            byte[] data = FileUtilEx.getFileData(ConfigPath);
            if (data != null && data.length > 0) {
                // 先解密
                unEncrypt(data);
                String json = new String(data);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        String srcData = jsonObj.optString(name, fallback);
                        return srcData;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /***
     * 根据关键字保存本地数据
     * @ConfigPath: 存放数据路径
     * @name 存放json key 值
     * @src 存放json value 值
     * @return void 返回类型
     */
    public static void saveLocalConfigData(String ConfigPath, String name, String src) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(src)) {
            return;
        }
        if (!FileUtilEx.isSDCardAvailable()) {
            return;
        }
        if (!FileUtilEx.isExist(ConfigPath)) {
            FileUtilEx.createFile(ConfigPath, FileUtilEx.MODE_COVER);
        }

        if (FileUtilEx.isExist(ConfigPath)) {
            byte[] filedata = FileUtilEx.getFileData(ConfigPath);
            if (filedata != null && filedata.length > 0) {
                unEncrypt(filedata);
                String json = new String(filedata);
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    jsonObj.put(name, src);
                    byte[] data = jsonObj.toString().getBytes();
                    // 先加密
                    encrypt(data);
                    FileUtilEx.rewriteData(ConfigPath, data);
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(name, src);
                    byte[] data = jsonObj.toString().getBytes();
                    // 先加密
                    encrypt(data);
                    FileUtilEx.rewriteData(ConfigPath, data);
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 获取最新登录的用户数据文件路径
     *
     * @return 用户数据文件路径
     */
    public static String getLatestUserDataFilePath() {
        if (!FileUtilEx.isSDCardAvailable()) {
            return null;
        }
        if (FileUtilEx.isExist(CONFIG_FILE)) {
            byte[] data = FileUtilEx.getFileData(CONFIG_FILE);
            if (data != null && data.length > 0) {
                // 先解密
                unEncrypt(data);
                String json = new String(data);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        String userDataFilePath = jsonObj.getString("userDataFilePath");
                        return userDataFilePath;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 保存最新使用的用户数据文件路径
     *
     * @param userDataFilePath 用户数据文件路径
     */
    public static void saveLatestUserDataFilePath(String userDataFilePath) {
        if (TextUtils.isEmpty(userDataFilePath)) {
            return;
        }
        if (!FileUtilEx.isSDCardAvailable()) {
            return;
        }
        if (!FileUtilEx.isExist(CONFIG_FILE)) {
            FileUtilEx.createFile(CONFIG_FILE, FileUtilEx.MODE_COVER);
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userDataFilePath", userDataFilePath);
            byte[] data = jsonObj.toString().getBytes();
            // 先加密
            encrypt(data);
            FileUtilEx.rewriteData(CONFIG_FILE, data);
        } catch (Exception e) {
        }
    }

    private final static byte[] encrypt_key = new byte[]{0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f};

    /**
     * 加密
     *
     * @param source
     */
    public static void encrypt(byte[] source) {
        if (source != null && source.length > 0) {
            int length = source.length;
            int keyLen = encrypt_key.length;
            for (int i = 0; i < length; i++) {
                source[i] ^= encrypt_key[i % keyLen];
            }
        }
    }

    /**
     * 解密
     *
     * @param source
     */
    public static void unEncrypt(byte[] source) {
        if (source != null && source.length > 0) {
            int length = source.length;
            int keyLen = encrypt_key.length;
            for (int i = 0; i < length; i++) {
                source[i] ^= encrypt_key[i % keyLen];
            }
        }
    }
}
