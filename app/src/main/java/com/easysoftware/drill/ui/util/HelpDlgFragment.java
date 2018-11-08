package com.easysoftware.drill.ui.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easysoftware.drill.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.easysoftware.drill.util.Constants.HelpType.IDIOM;
import static com.easysoftware.drill.util.Constants.HelpType.POEM;
import static com.easysoftware.drill.util.Constants.HelpType.SOLITAIRE;

public class HelpDlgFragment extends DialogFragment {
    public static final String TYPE = "dlg_type";
    public static final String TEXT_LIST = "dlg_text_list";
    public static final int ANSWER_COLOR = Color.rgb(0, 128, 0);

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private OnItemClickListener mOnItemClickListener;

    public HelpDlgFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static HelpDlgFragment newInstance(int type, ArrayList<String> texts) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putStringArrayList(TEXT_LIST, texts);

        HelpDlgFragment fragment = new HelpDlgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HelpDlgFragment newInstance(int type, ArrayList<String> texts,
                                              OnItemClickListener onItemClickListener) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putStringArrayList(TEXT_LIST, texts);

        HelpDlgFragment fragment = new HelpDlgFragment();
        fragment.setArguments(args);
        fragment.setOnItemClickListener(onItemClickListener);
        return fragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
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
            TextView textView = ((TextView) view.findViewById(R.id.tvContent));
            textView.setTextColor(ANSWER_COLOR);
            textView.setText("\n" + texts.get(0));
            ((TextView) view.findViewById(R.id.tvPinyin)).setText(texts.get(1));
            ((TextView) view.findViewById(R.id.tvOther)).setText(texts.get(2));
            builder.setView(view);
        } else if (type == POEM) {
            int i = 0;

            TextView textView = new TextView(getActivity());
            textView.setText(texts.get(i++));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(ANSWER_COLOR);
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
        } else if (type == SOLITAIRE) {
            // title
            TextView textView = new TextView(getActivity());
            textView.setText(texts.get(0));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(ANSWER_COLOR);
            builder.setCustomTitle(textView);

            // init recycler view
            View view = inflater.inflate(R.layout.layout_solitaire_help, null);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            // build the item set by removing the title (first item)
            texts.remove(0);

            // create and specify an adapter
            RecyclerView.Adapter adapter = new SolitaireHelpRecyclerAdapter(texts,
                    new OnItemClickListener() {
                        @Override
                        public void onItemClick(String item) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(item);
                                dismiss();
                            }
                        }
                    });
            recyclerView.setAdapter(adapter);

            // use the view for dialog
            builder.setView(view);
        }

        return builder.create();
    }

    public static class SolitaireHelpRecyclerAdapter extends RecyclerView.Adapter<SolitaireHelpRecyclerAdapter.SHViewHolder> {

//        public interface OnItemClickListener {
//            void onItemClick(String item);
//        }

        private List<String> mItems;
        private OnItemClickListener mOnItemClickListener;

        public SolitaireHelpRecyclerAdapter(List<String> items, OnItemClickListener onItemClickListener) {
            mItems = items;
            mOnItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public SolitaireHelpRecyclerAdapter.SHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_solitaire_help, parent, false);
            return new SHViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SolitaireHelpRecyclerAdapter.SHViewHolder holder, int position) {
            holder.bind(mItems.get(position), mOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        static class SHViewHolder extends RecyclerView.ViewHolder {
            private TextView mTextView;

            SHViewHolder(TextView itemView) {
                super(itemView);
                mTextView = itemView;
            }

            void bind(String text, OnItemClickListener onItemClickListener) {
                mTextView.setText(text);
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(text);
                        }
                    }
                });
            }
        }
    }
}
