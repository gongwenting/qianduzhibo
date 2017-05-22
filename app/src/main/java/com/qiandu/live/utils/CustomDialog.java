package com.qiandu.live.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qiandu.live.R;

/**
 * Created by admin on 2017/5/7.
 */
public class CustomDialog  extends Dialog {

    private static int default_width = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static int default_height = ViewGroup.LayoutParams.WRAP_CONTENT;

    public CustomDialog(Context context, int layout, int style, int gravity) {
        this(context, default_width, default_height, layout, style, false, gravity);
    }

    public CustomDialog(Context context, int layout, int width, int style, int gravity) {
        this(context, width, default_height, layout, style, false, gravity);
    }

    public CustomDialog(Context context, int width, int height, int layout,
                        int style, boolean isFullScreen, int gravity) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        // 代码修改，FILL_PARENT也会留出一个边
        if (width < 0) {
            int[] widthAndHeight = getSrceenPixels(context);
            if (isFullScreen) {
                params.width = (int) (widthAndHeight[0]);
            } else {
                params.width = (int) (widthAndHeight[0] - 20 * density);
            }
        } else {
            params.width = (int) (width * density);
            // params.width = width;
        }

        if (height < 0) {
            params.height = default_height;
        } else {
            params.height = (int) (height * density);
        }
        params.gravity = gravity;
        window.setAttributes(params);
    }

    public CustomDialog(Context context, int layout) {
        super(context, R.style.live_dialog_style);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(layout, null);
        FrameLayout.LayoutParams paramsF = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        paramsF.setMargins(dip2px(context, 20), 0, dip2px(context, 20), 0);
        setContentView(layoutView, paramsF);
        Button btnCancel = (Button) layoutView.findViewById(R.id.dialog_cancel);
        Button btnConfirm = (Button) layoutView.findViewById(R.id.dialog_confirm);
        TextView title = (TextView) layoutView.findViewById(R.id.dialog_message_info);
        title.setText(context.getResources().getString(R.string.live_cancel));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
                if (mCustomDialogListener != null) {
                    mCustomDialogListener.onConfirmClick();
                }
            }
        });
    }

    CustomDialogListener mCustomDialogListener;

    public void setCustomDialogListener(CustomDialogListener customDialogListener) {
        mCustomDialogListener = customDialogListener;
    }

    public interface CustomDialogListener {
        void onConfirmClick();
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public CustomDialog(Context context, int width, int layout, int style,
                        boolean isFullScreen) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        // 代码修改，FILL_PARENT也会留出一个边
        int[] widthAndHeight = getSrceenPixels(context);
        params.width = (int) (widthAndHeight[0] - width * density);
        params.height = default_height;

        if (isFullScreen) {
            params.width = (int) widthAndHeight[0];
            params.height = default_height;
        }
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

    private int[] getSrceenPixels(Context context) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        windowManager.getDefaultDisplay().getMetrics(displaysMetrics);
        int[] widthAndHeight = new int[2];
        widthAndHeight[0] = displaysMetrics.widthPixels;
        widthAndHeight[1] = displaysMetrics.heightPixels;
        return widthAndHeight;
    }

}
