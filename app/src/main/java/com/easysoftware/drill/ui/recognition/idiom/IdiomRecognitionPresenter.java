package com.easysoftware.drill.ui.recognition.idiom;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.recognition.RecognitionBasePresenter;

import javax.inject.Inject;

@PerActivity
public class IdiomRecognitionPresenter extends RecognitionBasePresenter {
    public static final int CF_LENGTH = 4;
    public static final int OBSF_LENGTH = 9;

    @Inject
    public IdiomRecognitionPresenter(IdiomLibLoader cfLibLoader) {
        super(cfLibLoader);
    }

    @Override
    protected int initCFLength() {
        return CF_LENGTH;
    }

    @Override
    protected int initObfuscationLength() {
        return OBSF_LENGTH;
    }
}
