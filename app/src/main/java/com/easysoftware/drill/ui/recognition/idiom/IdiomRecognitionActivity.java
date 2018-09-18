package com.easysoftware.drill.ui.recognition.idiom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ToggleButton;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.recognition.RecognitionBaseActivity;

import javax.inject.Inject;

public class IdiomRecognitionActivity extends RecognitionBaseActivity {

    @Inject
    IdiomRecognitionPresenter mIRPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_idiom_recognition);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.idiom_recognition);
    }

    @Override
    protected void initProgressBar() {
        mProgressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void initButtons() {
        mCharButtons = new ToggleButton[IdiomRecognitionPresenter.OBSF_LENGTH];
        mAnsButtons = new Button[IdiomRecognitionPresenter.CF_LENGTH];

        mNext = findViewById(R.id.btNext);

        int i = 0;
        mAnsButtons[i++] = findViewById(R.id.btAnsChar);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar1);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar2);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar3);

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
        mIRPresenter.start(this);
        mIRPresenter.loadChineseFragmentLibrary();

        mPresenter = mIRPresenter;
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
}
