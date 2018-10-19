package com.easysoftware.drill.ui.recognition;

import com.easysoftware.drill.data.cflib.CFLibraryLoader;
import com.easysoftware.drill.data.model.CFRecognitionItem;
import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.di.PerActivity;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public abstract class RecognitionBasePresenter implements RecognitionContract.Presenter {

    protected RecognitionContract.View mView;
    protected CompositeDisposable mCompositeDisposable;

    protected CFLibraryLoader mCFLibLoader;
    protected CFRecognitionItem.Builder mCfriBuilder;
    protected CFRecognitionItem mCfri;

    protected int mCFLength;
    protected int mObfuscationLength;

    protected int mCountTotal;
    protected int mCountCorrect;

    protected boolean mHelpCalled;

    public RecognitionBasePresenter() {
        mCFLength = initCFLength();
        mObfuscationLength = initObfuscationLength();
        mCompositeDisposable = new CompositeDisposable();
    }

    public RecognitionBasePresenter(CFLibraryLoader cfLibLoader) {
        this();
        mCFLibLoader = cfLibLoader;
    }

    protected abstract int initCFLength();

    protected abstract int initObfuscationLength();

    @Override
    public void start(RecognitionContract.View view) {
        mView = view;
        loadChineseFragmentLibrary();
    }

    protected DisposableObserver<CFRecognitionItem> createCfriObserver() {
        return new DisposableObserver<CFRecognitionItem>() {
            @Override
            public void onNext(CFRecognitionItem cfri) {
                mCfri = cfri;
                mView.displayChallenge(cfri.getObfuscation());
                mCountTotal++;
                mHelpCalled = false;
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgress();
            }

            @Override
            public void onComplete() {
                mView.hideProgress();
            }
        };
    }

    protected void loadChineseFragmentLibrary() {
        mView.showProgress();
        mCompositeDisposable.add(
                ChineseFragment.loadLibraryObservable(mCFLibLoader)
                        .switchMap(new Function<List<ChineseFragment>, ObservableSource<CFRecognitionItem>>() {
                            @Override
                            public ObservableSource<CFRecognitionItem> apply(List<ChineseFragment> cfList) throws Exception {
                                mCfriBuilder = new CFRecognitionItem.Builder(cfList, mCFLength, mObfuscationLength);
                                return mCfriBuilder.buildObservable();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(createCfriObserver())
        );
    }

    @Override
    public void stop() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void generateNext() {
        mView.showProgress();
        mCompositeDisposable.add(
                mCfriBuilder.buildObservable()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(createCfriObserver())
        );
    }

    @Override
    public void onNext(String result) {
        if (mHelpCalled) {
            generateNext();
            return;
        }

        if (mCfri.checkResult(result)) {
            mCountCorrect++;
            mView.displayNotificationForCorrectAnswer(mCountTotal, mCountCorrect);
        } else {
            mView.displayNotificationForWrongAnswer(mCountTotal, mCountCorrect, mCfri.getChineseFragment().toString());
        }
    }

    @Override
    public void setLevel(int level) {
    }

    protected abstract void help();

    @Override
    public void onHelp() {
        mHelpCalled = true;
        mView.displayCorrectAnswer(mCfri.getChineseFragment().toString());
        help();
    }

    public int getCFLength() {
        return mCFLength;
    }

    public int getObfuscationLength() {
        return mObfuscationLength;
    }
}
