package com.easysoftware.drill.ui.recognition.idiom;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.Idiom;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.recognition.RecognitionBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class IdiomRecognitionPresenter extends RecognitionBasePresenter {
    public static final int CF_LENGTH = 4;
    public static final int OBSF_LENGTH = 9;
    public static final String IDIOM_OTHER_FORMAT = "【解释】\n%s\n\n【出处】\n%s\n\n【例子】\n%s";

    private IdiomDbHelper mDbHelper;

    @Inject
    public IdiomRecognitionPresenter(IdiomLibLoader cfLibLoader, IdiomDbHelper dbHelper) {
        super(cfLibLoader);
        mDbHelper = dbHelper;
    }

    @Override
    protected int initCFLength() {
        return CF_LENGTH;
    }

    @Override
    protected int initObfuscationLength() {
        return OBSF_LENGTH;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void help() {
        mView.showProgress();
        mCompositeDisposable.add(mDbHelper.getIdiomObservable(mCfri.getChineseFragment().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Idiom>() {
                    @Override
                    public void onNext(Idiom idiom) {
                        List<String> texts = new ArrayList<>();
                        texts.add(idiom.getContent());
                        texts.add(idiom.getPinyin());

                        String other = String.format(IDIOM_OTHER_FORMAT, idiom.getExplanation(),
                                idiom.getDerivation(), idiom.getExample());
                        texts.add(other);

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
}
