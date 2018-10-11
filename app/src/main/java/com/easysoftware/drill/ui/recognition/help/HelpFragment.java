package com.easysoftware.drill.ui.recognition.help;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.easysoftware.drill.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.easysoftware.drill.util.Constants.IntentExtra.TYPE;
import static com.easysoftware.drill.util.Constants.IntentExtra.TEXT_LIST;
import static com.easysoftware.drill.util.Constants.TYPE.IDIOM;

public class HelpFragment extends DialogFragment {
    private List<String> mTexts;

    public HelpFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static HelpFragment newInstance(int type, ArrayList<String> texts) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putStringArrayList(TEXT_LIST, texts);

        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int type = getArguments().getInt(TYPE);
        List<String> texts = getArguments().getStringArrayList(TEXT_LIST);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (type == IDIOM) {
            View view = inflater.inflate(R.layout.item_idiom, null);
            ((TextView) view.findViewById(R.id.tvContent)).setText("\n" + texts.get(0));
            ((TextView) view.findViewById(R.id.tvPinyin)).setText(texts.get(1));
            ((TextView) view.findViewById(R.id.tvOther)).setText(texts.get(2));
            builder.setView(view);
        } else {
            int i = 0;

            TextView textView = new TextView(getActivity());
            textView.setText(texts.get(i++));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(Color.RED);
            builder.setCustomTitle(textView);

            View view = inflater.inflate(R.layout.item_poem, null);
            ((TextView) view.findViewById(R.id.tvTitle)).setText(texts.get(i++));
            ((TextView) view.findViewById(R.id.tvAuthor)).setText(texts.get(i++));
            String s = texts.get(i++);
            if (TextUtils.isEmpty(s)) {
                ((TextView) view.findViewById(R.id.tvPrologue)).setVisibility(View.GONE);
            } else {
                ((TextView) view.findViewById(R.id.tvPrologue)).setText(s);
            }
            ((TextView) view.findViewById(R.id.tvContent)).setText(texts.get(i++));
            builder.setView(view);
        }

        return builder.create();
    }
}
