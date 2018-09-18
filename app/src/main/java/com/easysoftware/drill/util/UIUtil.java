package com.easysoftware.drill.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

public class UIUtil {

    public interface AutoDismissAlertDialogCallback {
        void performActionAfterDismiss();
    }

    public static void showAutoDismissAlertDialog(Context context, String title, String message,
                                                  int durationinMilliSecond,
                                                  AutoDismissAlertDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    if (callback != null) {
                        callback.performActionAfterDismiss();
                    }
                }
            }
        }, durationinMilliSecond);
    }
}
