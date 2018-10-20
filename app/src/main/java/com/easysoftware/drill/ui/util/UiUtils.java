package com.easysoftware.drill.ui.util;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

public class UiUtils {

    public static void displayHtml(TextView textView, String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }

}
