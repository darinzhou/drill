package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;

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
        mCompositeDisposable.add(mDbHelper.getCFItemsStartwithObservable(getKeywordForNext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CFItem>>() {
                    @Override
                    public void onNext(List<CFItem> idioms) {
                        if (!generateAndAddItem(idioms)) {
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
