package com.easysoftware.drill.ui.recognition.poem5;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.di.PerActivity;
import com.easysoftware.drill.ui.recognition.RecognitionBasePresenter;

import javax.inject.Inject;

@PerActivity
public class Poem5RecognitionPresenter extends RecognitionBasePresenter {
    public static final int CF_LENGTH = 5;
    public static final int OBSF_LENGTH = 9;

    private PoemDbHelper mDbHelper;

    @Inject
    public Poem5RecognitionPresenter(PoemLibLoader cfLibLoader, PoemDbHelper dbHelper) {
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
    public void onHelp() {

    }

}
