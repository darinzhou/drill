package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.data.model.Idiom;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SolitaireWithKeywordHeadAndTailBasePresenter extends SolitaireWithKeywordBasePresenter {

    public SolitaireWithKeywordHeadAndTailBasePresenter(CFItemDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void generateNext() {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getCFItemsStartwithObservable(getKeywordForNext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CFItem>>() {
                    @Override
                    public void onNext(List<CFItem> idioms) {
                        String cf = null;
                        CFItem cfItem = null;
                        for (CFItem cfi : idioms) {
                            String s = cfi.getText();
                            if (!isCFUsed(s)) {
                                cf = s;
                                cfItem = cfi;
                                break;
                            }
                        }

                        if (cf != null) {
                            CFPairItem item = new CFPairItem();
                            item.setKeywordIn(getKeywordForNext());
                            item.setFirst(cf);
                            item.setFirstKeywordPositions(findKeywordPositions(cf));
                            item.setFirstVerificationText(getCorrectTextMessage());
                            item.setFirstExplanation(getExplanationMessage());
                            item.setFirstTexts(cfItem.getFormattedTexts());

                            updateKeywordForNext(cf);

                            addItem(item);

                        } else {
                            onSurrender(getCannotFindTextMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        onSurrender(getCannotFindTextMessage());
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                })
        );
    }

    @Override
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
                        item.setSecondExplanation(getExplanationMessage());
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
    protected List<Integer> findKeywordPositions(String cf) {
        List<Integer> kwPositions = new ArrayList<>();
        kwPositions.add(0);
        kwPositions.add(cf.length() - 1);
        return kwPositions;
    }

    @Override
    protected String updateKeywordForNext(String cf) {
        mKeywordForNext = cf.substring(cf.length()-1);
        return mKeywordForNext;
    }

    @Override
    protected boolean checkKeyword(String answer) {
        return answer.startsWith(mKeywordForNext);
    }

}
