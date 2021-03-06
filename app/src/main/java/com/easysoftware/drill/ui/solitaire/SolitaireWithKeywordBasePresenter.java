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
    protected String mKeyword;

    public SolitaireWithKeywordBasePresenter(CFItemDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected void init() {
        mView.inputInitialKeyword();
    }

    @Override
    public String getKeyword() {
        return mKeyword;
    }

    protected boolean generateAndAddItem(List<CFItem> idioms) {
        int count = idioms.size();
        if (count == 0) {
            return false;
        }

        // randomly find one item
        Random rnd = new Random();
        int checked = 0;
        int selected = rnd.nextInt(count);
        CFItem cfItem = idioms.get(selected);
        String cf = cfItem.getText();
        while (isCFUsed(cf) || !checkLengthLimitation(cf)) {
            checked++;
            // if all items were checked and all were used, return false
            if (checked == count) {
                return false;
            }

            selected = rnd.nextInt(idioms.size());
            cfItem = idioms.get(selected);
            cf = cfItem.getText();
        }

        // found one item
        CFPairItem item = new CFPairItem();
        item.setKeywordIn(mKeyword);
        item.setFirst(cf);
        item.setFirstKeywordPositions(findKeywordPositions(cf));
        item.setFirstVerificationText(getCorrectTextMessage());
        item.setFirstExplanation(getExplanationMessage(cfItem));
        item.setFirstTexts(cfItem.getFormattedTexts());

        updateKeyword(cf);

        addItem(item);

        return true;
    }

    protected void checkAnswerWithDb(final String answer, final int position) {
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

                        updateKeyword(cf);

                        CFPairItem item = mCFPairItems.get(position);
                        item.setKeywordOut(mKeyword);
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

    public void generateFirst(String keyword) {
        mKeyword = keyword;
        mView.displayKeyword();
        generateNext();
    }


    // abstract methods to be implemented in subclasses

    // functionalities
    protected abstract List<Integer> findKeywordPositions(String cf);
    protected abstract void updateKeyword(String cf);
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
