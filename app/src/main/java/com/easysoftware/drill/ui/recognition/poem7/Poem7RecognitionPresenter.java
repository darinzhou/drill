package com.easysoftware.drill.ui.recognition.poem7;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.recognition.RecognitionBasePresenter;

import javax.inject.Inject;

@PerActivity
public class Poem7RecognitionPresenter extends RecognitionBasePresenter {
    public static final int CF_LENGTH = 7;
    public static final int OBSF_LENGTH = 12;

    @Inject
    public Poem7RecognitionPresenter(PoemLibLoader cfLibLoader) {
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
