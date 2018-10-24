package com.easysoftware.drill.ui.solitaire;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.easysoftware.drill.R;
import com.easysoftware.drill.ui.util.UiUtils;

import java.util.List;

public class CFPairItemsRecyclerAdapter extends RecyclerView.Adapter<CFPairItemsRecyclerAdapter.CFPairItemViewHolder> {
    private SolitaireContract.Presenter mPresenter;

    public CFPairItemsRecyclerAdapter(@NonNull SolitaireContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public CFPairItemsRecyclerAdapter.CFPairItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cfpair, parent, false);
        return new CFPairItemViewHolder(v, mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull CFPairItemsRecyclerAdapter.CFPairItemViewHolder holder, int position) {
        mPresenter.onBindPairItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getPairItemCount();
    }

    public static class CFPairItemViewHolder extends RecyclerView.ViewHolder implements SolitaireContract.CFPairItemView {

        public static final String KEYWORD_FORMAT = "<b><font color='red'>%s</font></b>";
        public static final String EXPLANATION_FORMAT = "<u><font color='blue'>%s</font></u>";

        private SolitaireContract.Presenter mPresenter;

        private TextView mTvFirstText;
        private TextView mTvFirstExplanation;

        private TextView mTvSecondText;
        private TextView mTvSecondExplanation;
        private EditText mEtSecond;

        public CFPairItemViewHolder(View itemView, SolitaireContract.Presenter presenter) {
            super(itemView);

            mPresenter = presenter;

            mTvFirstText = itemView.findViewById(R.id.tvFirstText);
            mTvFirstExplanation = itemView.findViewById(R.id.tvFirstExplanation);
            mTvFirstExplanation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onViewDetailsFirst(getAdapterPosition());
                }
            });

            mTvSecondText = itemView.findViewById(R.id.tvSecondText);
            mTvSecondExplanation = itemView.findViewById(R.id.tvSecondExplanation);
            mTvSecondExplanation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onViewDetailsSecond(getAdapterPosition());
                }
            });

            mEtSecond = itemView.findViewById(R.id.etSecond);
            emptySecond(true);
        }

        private String buildTextHtml(String text, List<Integer> keywordPositions) {
            String html = "";
            for (int i = 0; i < text.length(); ++i) {
                if (keywordPositions.contains(i)) {
                    html += String.format(KEYWORD_FORMAT, String.valueOf(text.charAt(i)));
                } else {
                    html += text.charAt(i);
                }
            }
            return html;
        }

        private String buildExplanationHtml(String explanation) {
            return String.format(EXPLANATION_FORMAT, explanation);
        }

        private void displayText(TextView textView, String text, List<Integer> keywordPositions) {
            String html = buildTextHtml(text, keywordPositions);
            UiUtils.displayHtml(textView, html);
        }

        private void displayExplanation(TextView textView, String explanation) {
            String html = buildExplanationHtml(explanation);
            UiUtils.displayHtml(textView, html);
        }

        @Override
        public void setFirst(String text, List<Integer> keywordPositions, String explanation) {
            displayText(mTvFirstText, text, keywordPositions);
            displayExplanation(mTvFirstExplanation, explanation);
        }

        @Override
        public void setSecond(String text, List<Integer> keywordPositions, String explanation) {
            displayText(mTvSecondText, text, keywordPositions);
            displayExplanation(mTvSecondExplanation, explanation);

            emptySecond(false);
        }

        @Override
        public void emptySecond(boolean empty) {
            if (empty) {
                mEtSecond.setText("");
                mEtSecond.setVisibility(View.VISIBLE);
                mTvSecondText.setVisibility(View.GONE);
                mTvSecondExplanation.setVisibility(View.GONE);
                mEtSecond.requestFocus();
            } else {
                mEtSecond.setVisibility(View.GONE);
                mTvSecondText.setVisibility(View.VISIBLE);
                mTvSecondExplanation.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public String getAnswer() {
            return mEtSecond.getText().toString();
        }
    }
}
