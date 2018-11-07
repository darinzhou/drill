package com.easysoftware.drill.ui.solitaire.keywordheadandtail;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.ui.solitaire.SolitaireWithKeywordBasePresenter;

import java.util.ArrayList;
import java.util.List;

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
        mCompositeDisposable.add(mDbHelper.getCFItemsStartwithObservable(mKeywordForNext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CFItem>>() {
                    @Override
                    public void onNext(List<CFItem> cfItems) {
                        if (!generateAndAddItem(cfItems)) {
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
    public void onHelp() {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getCFItemsStartwithObservable(mKeywordForNext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CFItem>>() {
                    @Override
                    public void onNext(List<CFItem> cfItems) {
                        List<String> texts = new ArrayList<>();
                        texts.add(String.format(SOLITAIRE_HELP_TITLE_FORMAT, getCurrentItem().getFirst()));
                        for (CFItem cfItem : cfItems) {
                            String text = cfItem.getText();
                            if (!isCFUsed(text) && checkLengthLimitation(text)) {
                                texts.add(text);
                            }
                        }
                        mView.displayHelp(texts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
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
    protected void updateKeywordForNext(String cf) {
        mKeywordForNext = cf.substring(cf.length()-1);
    }

    @Override
    protected boolean checkKeyword(String answer) {
        return answer.startsWith(mKeywordForNext);
    }

}
