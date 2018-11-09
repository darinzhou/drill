package com.easysoftware.drill.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.easysoftware.drill.R;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        Resources res = context.getResources();

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setHeight((int)res.getDimension(R.dimen.dlg_title_height));
        textView.setPadding((int)res.getDimension(R.dimen.dlg_title_left_padding), 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.dlg_title_text_size));
        textView.setBackgroundColor(res.getColor(R.color.colorDlgTitleBackground));
        textView.setTextColor(res.getColor(R.color.colorDlgTitleTextColor));
        textView.setText(title);
        return textView;
    }
}
