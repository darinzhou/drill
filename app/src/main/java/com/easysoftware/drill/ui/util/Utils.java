package com.easysoftware.drill.ui.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import static com.easysoftware.drill.util.Constants.Dimension.DLG_TITLE_BACKGROUND_COLOR;
import static com.easysoftware.drill.util.Constants.Dimension.DLG_TITLE_HEIGHT;
import static com.easysoftware.drill.util.Constants.Dimension.DLG_TITLE_LEFT_PADDING;
import static com.easysoftware.drill.util.Constants.Dimension.DLG_TITLE_TEXT_COLOR;
import static com.easysoftware.drill.util.Constants.Dimension.DLG_TITLE_TEXT_SIZE;

public class Utils {
    public static final char[] PUNCTUATIONS = {',', ';', '?', '!', '.', '，', '；', '？', '！', '。', ':', '：', '、'};
    public static Pair<String, String> splitTextAndEndingPunctuation(String s) {
        int len = s.length();
        char endChar = s.charAt(len-1);
        String punctuation = "";
        for (char p : PUNCTUATIONS) {
            if (p == endChar) {
                s = s.substring(0, len-1);
                punctuation = "" + p;
            }
            s = s.replace(String.valueOf(p), "");
        }
        return new Pair<>(s, punctuation);
    }

    public static void displayHtml(TextView textView, String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px,
                context.getResources().getDisplayMetrics());
    }

    public static TextView createDlgTitle(Context context, String title) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        int height = (int)Utils.dp2px(context, DLG_TITLE_HEIGHT);
        textView.setHeight(height);
        textView.setPadding((int)Utils.dp2px(context, DLG_TITLE_LEFT_PADDING), 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DLG_TITLE_TEXT_SIZE);
        textView.setBackgroundColor(DLG_TITLE_BACKGROUND_COLOR);
        textView.setTextColor(DLG_TITLE_TEXT_COLOR);
        textView.setText(title);
        return textView;
    }
}
