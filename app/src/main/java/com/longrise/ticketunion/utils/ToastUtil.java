package com.longrise.ticketunion.utils;

import android.widget.Toast;

import com.longrise.ticketunion.ui.activity.MyApplication;

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(String tips) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(tips);
        }
        mToast.show();
    }
}
