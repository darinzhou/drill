package com.easysoftware.drill.ui.recognition.poem7;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.recognition.RecognitionBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class Poem7RecognitionPresenter extends RecognitionBasePresenter {
    public static final int CF_LENGTH = 7;
    public static final int OBSF_LENGTH = 12;
    public static final String POEM_ANSWER_FORMAT = "\n  【答案】%s\n  【出处】";

    private PoemDbHelper mDbHelper;

    @Inject
    public Poem7RecognitionPresenter(PoemLibLoader cfLibLoader, PoemDbHelper dbHelper) {
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
        mCompositeDisposable.add(mDbHelper.getPoemsObservable(mCfri.getChineseFragment().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Poem>>() {
                    @Override
                    public void onNext(List<Poem> poems) {
                        // find the best matched idiom
                        Poem poem = poems.get(0);
                        for (Poem p : poems) {
                            if (p.sentencePosition(mCfri.toString()) != -1) {
                                poem = p;
                                break;
                            }
                        }

                        List<String> texts = new ArrayList<>();
                        texts.add(String.format(POEM_ANSWER_FORMAT, mCfri.getChineseFragment().toString()));
                        texts.add(poem.getTitleString());
                        texts.add(poem.getAuthorString());
                        texts.add(poem.getPrologue());
                        texts.add(poem.getContent());
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
