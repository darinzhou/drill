package com.easysoftware.drill.ui.solitaire.idiom;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.data.model.Idiom;
import com.easysoftware.drill.ui.solitaire.SolitaireBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class IdiomSolitairePresenter extends SolitaireBasePresenter {
    public static final String VALID_IDIOM_TEXT = "成语正确";
    public static final String INVALID_IDIOM_TEXT = "成语不正确：";
    public static final String VALID_KEYWORD_TEXT = "且符合要求：";
    public static final String INVALID_KEYWORD_TEXT = "答案不符合要求：";
    public static final String DUPLICATED_IDIOM_TEXT = "该成语已经用过， 不能重复使用：";
    public static final String CANNOT_FIND_CORRECT_ANSWER_TEXT = "找不到符合要求的成语";
    public static final String EXPLANATION_TEXT = "成语解释";

    private IdiomDbHelper mDbHelper;

    @Inject
    public IdiomSolitairePresenter(IdiomDbHelper dbHelper) {
        super();
        mDbHelper = dbHelper;
    }

    @Override
    protected String generateInitialKeyword() {
        return "一";
    }

    @Override
    public void onHelp() {

    }

    @Override
    public void generateNext() {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getIdiomsStartwithObservable(getKeywordForNext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Idiom>>() {
                    @Override
                    public void onNext(List<Idiom> idioms) {
                        String cf = null;
                        Idiom idiom = null;
                        for (Idiom id : idioms) {
                            String s = id.getText();
                            if (!isCFUsed(s)) {
                                cf = s;
                                idiom = id;
                                break;
                            }
                        }

                        if (cf != null) {
                            CFPairItem item = new CFPairItem();
                            item.setKeywordIn(getKeywordForNext());
                            item.setFirst(cf);
                            item.setFirstKeywordPositions(findKeywordPositions(cf));
                            item.setFirstVerificationText(VALID_IDIOM_TEXT);
                            item.setFirstExplanation(EXPLANATION_TEXT);
                            item.setFirstTexts(Idiom.getFormatedTexts(idiom));

                            updateKeywordForNext(cf);

                            addItem(item);

                        } else {
                            onSurrender(CANNOT_FIND_CORRECT_ANSWER_TEXT);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        onSurrender(CANNOT_FIND_CORRECT_ANSWER_TEXT);
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
        if (!checkKeyword(answer)) {
            onWrongAnswer(INVALID_KEYWORD_TEXT + answer);
            return;
        }

        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getIdiomObservable(answer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Idiom>() {
                    @Override
                    public void onNext(Idiom idiom) {
                        String cf = idiom.getText();
                        if (isCFUsed(cf)) {
                            onDuplication(DUPLICATED_IDIOM_TEXT + cf);
                            return;
                        }

                        updateKeywordForNext(cf);

                        CFPairItem item = mCFPairItems.get(position);
                        item.setKeywordOut(getKeywordForNext());
                        item.setSecond(cf);
                        item.setSecondKeywordPositions(findKeywordPositions(cf));
                        item.setSecondVerificationText(VALID_IDIOM_TEXT);
                        item.setSecondExplanation(EXPLANATION_TEXT);
                        item.setSecondTexts(Idiom.getFormatedTexts(idiom));

                        updateUsedSet(cf);
                        onCorrectAnswer(VALID_IDIOM_TEXT + VALID_KEYWORD_TEXT + cf);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        onWrongAnswer(INVALID_IDIOM_TEXT + answer);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                })
        );

    }

    @Override
    public void onViewDetailsFirst(int position) {
        mView.displayHelp(mCFPairItems.get(position).getFirstTexts());
    }

    @Override
    public void onViewDetailsSecond(int position) {
        mView.displayHelp(mCFPairItems.get(position).getSecondTexts());
    }

    public List<Integer> findKeywordPositions(String cf) {
        List<Integer> kwPositions = new ArrayList<>();
        kwPositions.add(0);
        kwPositions.add(cf.length() - 1);
        return kwPositions;
    }

    public String updateKeywordForNext(String cf) {
        mKeywordForNext = cf.substring(cf.length()-1);
        return mKeywordForNext;
    }

    protected boolean checkKeyword(String answer) {
        return answer.startsWith(mKeywordForNext);
    }

    @Override
    protected void onCorrectAnswer(String message) {
        mView.displayNotificationForCorrectAnswer(mCFPairItems.size(), message);
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
}
