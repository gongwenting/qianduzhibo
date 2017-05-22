package com.qiandu.live.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qiandu.live.R;

/**
 * @description: 吐司工具类
 *
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class ToastUtils {
    private static Toast toast;
    private static Toast toastWhite;
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    public static void showShort(Context context, String resId) {
        if (context != null && !TextUtils.isEmpty(resId)) {
            makeText(context, resId, LENGTH_SHORT).show();
        }
    }
    public static void showShort(Context context, int resId) {
        if (context != null )
            makeText(context, resId, LENGTH_SHORT).show();
    }

    public static ToastUtils makeText(Context context, int resId, int duration) {
        return makeText(context, context.getResources().getString(resId), duration);
    }

    public static ToastUtils makeText(Context context, CharSequence text, int duration) {
        return makeText(context, text, duration, Gravity.CENTER, 0, 0);
    }

    public static ToastUtils makeText(Context context, CharSequence text, int duration, int gravity, int xOffset, int yOffset) {
        if (toast == null) {
            toast = new Toast(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.toast_utils, null);
            toast.setView(root);
            toast.setGravity(gravity, xOffset, yOffset);
        }
        View root = toast.getView();
        TextView toastTv = (TextView) root.findViewById(R.id.toast_tv);
        toastTv.setText(text);
        toast.setDuration(duration);
        return new ToastUtils();
    }

    public void show() {
        if (toast != null)
            toast.show();
    }

    public void showWhite() {
        if (toastWhite != null)
            toastWhite.show();
    }
}
