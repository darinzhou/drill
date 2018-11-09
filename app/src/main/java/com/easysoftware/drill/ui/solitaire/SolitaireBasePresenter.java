package com.easysoftware.drill.ui.solitaire;

import android.text.TextUtils;
import android.util.Pair;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public abstract class SolitaireBasePresenter implements SolitaireContract.Presenter {
    protected SolitaireContract.View mView;
    protected CompositeDisposable mCompositeDisposable;

    protected CFItemDbHelper mDbHelper;
    protected Set<String> mUsedSet;
    protected List<CFPairItem> mCFPairItems;

    protected int mHelpCount;

    public SolitaireBasePresenter(CFItemDbHelper dbHelper) {
        mUsedSet = new HashSet<>();
        mCFPairItems = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
        mDbHelper = dbHelper;
    }

    @Override
    public void start(SolitaireContract.View view) {
        mView = view;
        init();
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

        setItemTextSize(viewHolder);

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
    public void onSubmitAnswer(String answer) {
        // last item
        int position = mCFPairItems.size()-1;
        SolitaireContract.CFPairItemView viewHolder = mView.getCFPairItemView(position);
        viewHolder.setAnswer(answer);
        verifyAnswer(answer, position);
    }

    @Override
    public void onViewDetailsFirst(int position) {
        mView.displayItemDetails(mCFPairItems.get(position).getFirstTexts());
    }

    @Override
    public void onViewDetailsSecond(int position) {
        mView.displayItemDetails(mCFPairItems.get(position).getSecondTexts());
    }

    @Override
    public void onHelp() {
        mHelpCount++;
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

    protected boolean checkLengthLimitation(String text) {
        return text.length() <= 8;
    }

    protected boolean isCFUsed(String text) {
        return mUsedSet.contains(text);
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

    protected int getCorrentAnswerCount(boolean isCurrectCorrect) {
        int count = mCFPairItems.size() - mHelpCount;
        if (!isCurrectCorrect) {
            count--;
        }
        return count;
    }

    protected void onCorrectAnswer(String message) {
        mView.displayNotificationForCorrectAnswer(getCorrentAnswerCount(true), message);
    }

    protected void onWrongAnswer(String message) {
        mView.displayNotificationForWrongAnswer(getCorrentAnswerCount(false), message);
    }

    protected void onDuplication(String message) {
        mView.displayNotificationForWrongAnswer(getCorrentAnswerCount(false), message);
    }

    protected void onSurrender(String message) {
        mView.displayNotificationForSurrender(getCorrentAnswerCount(true), message);
    }

    // abstract methods
    protected abstract void init();
    protected abstract void setItemTextSize(SolitaireContract.CFPairItemView viewHolder);
    protected abstract void verifyAnswer(String answer, int position);

}
