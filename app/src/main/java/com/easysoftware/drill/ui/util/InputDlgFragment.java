package com.easysoftware.drill.ui.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easysoftware.drill.R;

import java.util.List;
import java.util.Objects;

public class InputDlgFragment extends DialogFragment {
    public static final String TITLE = "dlg_title";
    public static final String MESSAGE = "dlg_message";
    public static final int ANSWER_COLOR = Color.rgb(0, 128, 0);

    private boolean mEnableOK = false;

    // event listener
    public interface OnDismissListener {
        void onOK(String input);
        void onCancel();
    }

    private OnDismissListener mOnDismissListener;

    public InputDlgFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static InputDlgFragment newInstance(String title, String message,
                                               OnDismissListener listener) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);

        InputDlgFragment fragment = new InputDlgFragment();
        fragment.setArguments(args);
        fragment.setOnDismissListener(listener);
        return fragment;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_inputdialog, null);
        ((TextView) view.findViewById(R.id.tvMessage)).setText(message);

        final EditText etInput = view.findViewById(R.id.etInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        if (mOnDismissListener != null) {
                            mOnDismissListener.onCancel();
                        }
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        if (mOnDismissListener != null) {
                            mOnDismissListener.onOK(etInput.getText().toString().substring(0,1));
                        }
                    }
                })
                .setCancelable(false);

        builder.setView(view);

        TextView textView = Utils.createDlgTitle(getActivity(), title);
        builder.setCustomTitle(textView);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(mEnableOK);
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEnableOK = !TextUtils.isEmpty(etInput.getText());
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(mEnableOK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return dialog;
    }
}
