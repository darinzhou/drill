package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.di.PerActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public abstract class SolitaireBasePresenter implements SolitaireContract.Presenter {

    protected SolitaireContract.View mView;
    protected CompositeDisposable mCompositeDisposable;

    protected List<CFPairItem> mCFPairItems;
    protected String mKeyword;
    protected int mCountCorrect = 0;

    public SolitaireBasePresenter() {
        mCFPairItems = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start(SolitaireContract.View view) {
        mView = view;
        mKeyword = generateKeyword();
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
        viewHolder.setFirst(item.getFirst().toString(),
                item.getKeywordPositionsInFirst(), item.getFirstExplanation());
    }

    @Override
    public void onSubmitAnswer(String answer, SolitaireContract.CFPairItemView viewHolder, int position) {
        CFPairItem item = mCFPairItems.get(position);
        if (item.setSecond(answer)) {
            // answer is correct
            mCountCorrect++;
            viewHolder.setSecond(item.getSecond().toString(), item.getKeywordPositionsInFirst(),
                    item.getSecondExplanation());
            mView.displayNotificationForCorrectAnswer(mCountCorrect, item.getSecondVerificationText());
        } else {
            // answer is wrong and display failure info
            mView.displayNotificationForWrongAnswer(mCountCorrect, item.getSecondVerificationText());
        }
    }

    // create new pair based on conditions and add it to the pair list
    // will be called in view, on dismiss of correct notification
    @Override
    public void generateNext() {
    }

    @Override
    public void onViewDetailsFirst(int position) {

    }

    @Override
    public void onViewDetailsSecond(int position) {

    }

    protected abstract String generateKeyword();

}
