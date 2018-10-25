package com.easysoftware.drill.ui.solitaire;

import android.text.TextUtils;
import android.util.Pair;

import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public abstract class SolitaireBasePresenter implements SolitaireContract.Presenter {

    protected SolitaireContract.View mView;
    protected CompositeDisposable mCompositeDisposable;

    protected Set<String> mUsedSet;
    protected List<CFPairItem> mCFPairItems;
    protected String mInitialKeyword;
    protected String mKeywordForNext;
    protected int mCountCorrect = 0;

    public SolitaireBasePresenter() {
        mUsedSet = new HashSet<>();
        mCFPairItems = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start(SolitaireContract.View view) {
        mView = view;
        mInitialKeyword = generateInitialKeyword();
        mKeywordForNext = mInitialKeyword;
        generateNext();
    }

    @Override
    public void stop() {
        mCompositeDisposable.dispose();
    }

    @Override
    public int getPairItemCount() {
        return mCFPairItems.size();
    }

    @Override
    public void onBindPairItemView(SolitaireContract.CFPairItemView viewHolder, int position) {
        CFPairItem item = mCFPairItems.get(position);

        viewHolder.setFirst(item.getFirst(),
                item.getFirstKeywordPositions(), item.getFirstExplanation());

        if (item.isMatched()) {
            viewHolder.setSecond(item.getSecond(),
                    item.getSecondKeywordPositions(), item.getSecondExplanation());
        } else {
            viewHolder.emptySecond(true);
        }
    }

    @Override
    public void onSubmitAnswer() {
        // last item
        int position = mCFPairItems.size()-1;
        SolitaireContract.CFPairItemView viewHolder = mView.getCFPairItemView(position);
        String answer = viewHolder.getAnswer();
        verifyAnswer(answer, position);
    }

    @Override
    public String getInitialKeyword() {
        return mInitialKeyword;
    }

    protected String formatAnswer(String answer) {
        if (TextUtils.isEmpty(answer)) {
            return "";
        }
        // remove spaces
        answer = answer.replace(" ", "");
        // remove ending punctuation
        Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(answer);
        return pair.first;
    }

    protected boolean isCFUsed(String text) {
        return mUsedSet.contains(text);
    }

    protected String getKeywordForNext() {
        return mKeywordForNext;
    }

    protected CFPairItem getCurrentItem() {
        if (mCFPairItems == null || mCFPairItems.isEmpty()) {
            return null;
        }
        return mCFPairItems.get(mCFPairItems.size()-1);
    }

    protected void updateUsedSet(String cf) {
        mUsedSet.add(cf);
        mView.notifyLastItemChanged();
    }

    protected void addItem(CFPairItem item) {
        mCFPairItems.add(item);
        updateUsedSet(item.getFirst());
    }

    protected abstract String generateInitialKeyword();
    protected abstract String updateKeywordForNext(String cf);

    protected abstract void verifyAnswer(String answer, int position);
    protected abstract void onCorrectAnswer(String message);
    protected abstract void onWrongAnswer(String message);
    protected abstract void onSurrender(String message);
    protected abstract void onDuplication(String message);

}
