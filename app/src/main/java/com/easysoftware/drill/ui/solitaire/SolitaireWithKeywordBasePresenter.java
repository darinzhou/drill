package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.CFPairItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SolitaireWithKeywordBasePresenter extends SolitaireBasePresenter {
    protected CFItemDbHelper mDbHelper;

    public SolitaireWithKeywordBasePresenter(CFItemDbHelper dbHelper) {
        super();
        mDbHelper = dbHelper;
    }

    @Override
    protected void verifyAnswer(String answer, final int position) {
        answer = formatAnswer(answer);

        if (!checkKeyword(answer)) {
            onWrongAnswer(getInvalidKeywordTextMessage() + answer);
            return;
        }
        checkAnswerWithDb(answer, position);
    }

    @Override
    public void onViewDetailsFirst(int position) {
        mView.displayHelp(mCFPairItems.get(position).getFirstTexts());
    }

    @Override
    public void onViewDetailsSecond(int position) {
        mView.displayHelp(mCFPairItems.get(position).getSecondTexts());
    }

    @Override
    protected void onCorrectAnswer(String message) {
        mView.displayNotificationForCorrectAnswer(mCFPairItems.size(), message);
    }

    @Override
    public void onHelp() {

    }

    @Override
    protected void onWrongAnswer(String message) {

    }

    @Override
    protected void onSurrender(String message) {

    }

    @Override
    protected void onDuplication(String message) {

    }

    // abstract methods to be implemented in subclasses

    protected abstract String getCorrectTextMessage();
    protected abstract String getWrongTextMessage();
    protected abstract String getDuplicatedTextMessage();
    protected abstract String getExplanationMessage();
    protected abstract String getCannotFindTextMessage();
    protected abstract String getValidKeywordTextMessage();
    protected abstract String getInvalidKeywordTextMessage();

    protected abstract void checkAnswerWithDb(String answer, int position);

    protected abstract List<Integer> findKeywordPositions(String cf);

    protected abstract String updateKeywordForNext(String cf);

    protected abstract boolean checkKeyword(String answer);

}
