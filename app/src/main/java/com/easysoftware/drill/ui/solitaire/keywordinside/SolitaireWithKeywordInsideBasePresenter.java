package com.easysoftware.drill.ui.solitaire.keywordinside;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.ui.solitaire.SolitaireWithKeywordBasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SolitaireWithKeywordInsideBasePresenter extends SolitaireWithKeywordBasePresenter {

    public SolitaireWithKeywordInsideBasePresenter(CFItemDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void generateNext() {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getCFItemsContainKeywordObservable(mInitialKeyword)
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
        mCompositeDisposable.add(mDbHelper.getCFItemsContainKeywordObservable(mInitialKeyword)
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
        for (int i=0; i<cf.length(); ++i) {
            if (mInitialKeyword.equals(String.valueOf(cf.charAt(i)))) {
                kwPositions.add(i);
            }
        }
        return kwPositions;
    }

    @Override
    protected void updateKeywordForNext(String cf) {
    }

    @Override
    protected boolean checkKeyword(String answer) {
        return answer.contains(mInitialKeyword);
    }

}
