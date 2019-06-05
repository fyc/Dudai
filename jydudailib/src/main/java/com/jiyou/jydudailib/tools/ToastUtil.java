package com.jiyou.jydudailib.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jiyou.jydudailib.config.ExtApp;


/**
 * @desc 吐司工具类
 */
public class ToastUtil {

    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Toast toast = null;
    private static boolean isShow = true;
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (isShow)
//          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            toast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        if (isShow)
//          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            toast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(final CharSequence message) {
        if (isShow)
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //          Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    toast(message, Toast.LENGTH_LONG);
                }
            }, 100);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLongHideSoftInput(final Context context, final CharSequence message) {
        if (isShow)
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
                    }
                    toast(message, Toast.LENGTH_LONG);
                }
            }, 100);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        if (isShow)
//          Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            toast(message, Toast.LENGTH_LONG);
    }

    public static void toast(int resId, int duration) {
        String message = ExtApp.app().getResources().getString(resId);
        toast(message, duration);
    }

    public static void toast(CharSequence message, int duration) {
        String m_ToastStr = "<font color='#000000'>" + message + "</font>";
        if (toast == null) {
            toast = Toast.makeText(ExtApp.app(), Html.fromHtml(m_ToastStr), duration);
//            toast = Toast.makeText(ExtApp.app(), message, duration);
        } else {
            toast.setText(Html.fromHtml(m_ToastStr));
        }
        toast.show();
    }

    /**
     * 显示自定义View的Toast
     *
     * @param context
     * @param message
     */
//    public static void showCustomToast(Context context, String message) {
//        //加载自定义Toast布局
//        View toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);
//        //初始化布局控件
//        TextView mTextView = (TextView) toastView.findViewById(R.id.toast_message);
//        //为控件设置内容
//        mTextView.setText(message);
//        //Toast的初始化
//        Toast toast = new Toast(context);
//        //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();//获取屏幕高度值
//        toast.setGravity(Gravity.TOP, 0, height / 2);//设置显示位置
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(toastView);
//        toast.show();
//    }
}
