package com.easysoftware.drill.ui.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

public class SingleChoiceDlgFragment extends DialogFragment {
    public static final String TITLE_KEY = "dlg_title";
    public static final String ITEMS_KEY = "dlg_items";

    // event listener
    public interface OnChooseListener {
        void onChoose(int which);
    }

    private OnChooseListener mOnChooseListener;

    public SingleChoiceDlgFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SingleChoiceDlgFragment newInstance(String title, String[] items,
                                                      OnChooseListener listener) {
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putStringArray(ITEMS_KEY, items);

        SingleChoiceDlgFragment fragment = new SingleChoiceDlgFragment();
        fragment.setArguments(args);
        fragment.setOnChooseListener(listener);
        return fragment;
    }

    public void setOnChooseListener(OnChooseListener listener) {
        mOnChooseListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE_KEY);
        String[] items = getArguments().getStringArray(ITEMS_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnChooseListener != null) {
                            mOnChooseListener.onChoose(which);
                        }
                    }
                })
                .setCancelable(false);
        return builder.create();
    }
}
