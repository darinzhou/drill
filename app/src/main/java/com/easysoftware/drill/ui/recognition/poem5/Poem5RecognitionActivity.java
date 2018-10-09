package com.easysoftware.drill.ui.recognition.poem5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ToggleButton;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.recognition.RecognitionBaseActivity;

import java.util.List;

import javax.inject.Inject;

public class Poem5RecognitionActivity extends RecognitionBaseActivity {

    @Inject
    Poem5RecognitionPresenter mP5RPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_poem5_recognition);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.poem5_recognition);
    }

    @Override
    protected void initProgressBar() {
        mProgressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void initButtons() {
        mCharButtons = new ToggleButton[Poem5RecognitionPresenter.OBSF_LENGTH];
        mAnsButtons = new Button[Poem5RecognitionPresenter.CF_LENGTH];

        mNext = findViewById(R.id.btNext);

        int i = 0;
        mAnsButtons[i++] = findViewById(R.id.btAnsChar);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar1);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar2);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar3);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar4);

        i = 0;
        mCharButtons[i++] = findViewById(R.id.btChar);
        mCharButtons[i++] = findViewById(R.id.btChar1);
        mCharButtons[i++] = findViewById(R.id.btChar2);
        mCharButtons[i++] = findViewById(R.id.btChar3);
        mCharButtons[i++] = findViewById(R.id.btChar4);
        mCharButtons[i++] = findViewById(R.id.btChar5);
        mCharButtons[i++] = findViewById(R.id.btChar6);
        mCharButtons[i++] = findViewById(R.id.btChar7);
        mCharButtons[i++] = findViewById(R.id.btChar8);
    }

    @Override
    protected void initPresenter() {
        // presenter, was injected
        mP5RPresenter.start(this);
        mP5RPresenter.loadChineseFragmentLibrary();

        mPresenter = mP5RPresenter;
    }

    @Override
    protected void initInjection() {
        // DI injection
        ((DrillApp) getApplication()).createActivityComponent().inject(this);
    }

    @Override
    protected void cleanInjection() {
        ((DrillApp) getApplication()).releaseActivityComponent();
    }

    @Override
    public void displayHelp(List<String> texts) {

    }

}
