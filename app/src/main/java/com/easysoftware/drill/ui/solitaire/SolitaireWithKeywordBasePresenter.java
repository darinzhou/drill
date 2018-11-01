package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.CFPairItem;

import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SolitaireWithKeywordBasePresenter extends SolitaireBasePresenter {
    protected String mInitialKeyword;
    protected String mKeywordForNext;

    public SolitaireWithKeywordBasePresenter(CFItemDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected void init() {
        mView.inputInitialKeyword();
    }

    @Override
    public String getInitialKeyword() {
        return mInitialKeyword;
    }

    protected boolean generateAndAddItem(List<CFItem> idioms) {
        // randomly find one item
        Random rnd = new Random();
        int count = idioms.size();
        int checked = 0;
        int selected = rnd.nextInt(count);
        while (isCFUsed(idioms.get(selected).getText())) {
            checked++;
            // if all items were checked and all were used, return false
            if (checked == count) {
                return false;
            }

            selected = rnd.nextInt(idioms.size());
        }

        // found one item
        CFItem cfItem = idioms.get(selected);
        String cf = cfItem.getText();
        CFPairItem item = new CFPairItem();
        item.setKeywordIn(getKeywordForNext());
        item.setFirst(cf);
        item.setFirstKeywordPositions(findKeywordPositions(cf));
        item.setFirstVerificationText(getCorrectTextMessage());
        item.setFirstExplanation(getExplanationMessage(cfItem));
        item.setFirstTexts(cfItem.getFormattedTexts());

        updateKeywordForNext(cf);

        addItem(item);

        return true;
    }

    protected void checkAnswerWithDb(String answer, final int position) {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getCFItemObservable(answer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CFItem>() {
                    @Override
                    public void onNext(CFItem cfItem) {
                        String cf = cfItem.getText();
                        if (isCFUsed(cf)) {
                            onDuplication(getDuplicatedTextMessage() + cf);
                            return;
                        }

                        updateKeywordForNext(cf);

                        CFPairItem item = mCFPairItems.get(position);
                        item.setKeywordOut(getKeywordForNext());
                        item.setSecond(cf);
                        item.setSecondKeywordPositions(findKeywordPositions(cf));
                        item.setSecondVerificationText(getCorrectTextMessage());
                        item.setSecondExplanation(getExplanationMessage(cfItem));
                        item.setSecondTexts(cfItem.getFormattedTexts());

                        updateUsedSet(cf);
                        onCorrectAnswer(getCorrectTextMessage() + getValidKeywordTextMessage() + cf);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        onWrongAnswer(getWrongTextMessage() + answer);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                })
        );
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

    protected String getKeywordForNext() {
        return mKeywordForNext;
    }

    public void generateFirst(String keyword) {
        mInitialKeyword = keyword;
        mKeywordForNext = mInitialKeyword;
        mView.displayInitialKeyword();
        generateNext();
    }


    // abstract methods to be implemented in subclasses

    // functionalities
    protected abstract List<Integer> findKeywordPositions(String cf);
    protected abstract void updateKeywordForNext(String cf);
    protected abstract boolean checkKeyword(String answer);


    // displaying message
    protected abstract String getCorrectTextMessage();
    protected abstract String getWrongTextMessage();
    protected abstract String getDuplicatedTextMessage();
    protected abstract String getExplanationMessage(CFItem cfItem);
    protected abstract String getCannotFindTextMessage();
    protected abstract String getValidKeywordTextMessage();
    protected abstract String getInvalidKeywordTextMessage();

}
