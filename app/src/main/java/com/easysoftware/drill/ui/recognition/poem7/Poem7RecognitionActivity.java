package com.easysoftware.drill.ui.recognition.poem7;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Button;
import android.widget.ToggleButton;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.recognition.RecognitionBaseActivity;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.SingleChoiceDlgFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.HelpType.POEM;

public class Poem7RecognitionActivity extends RecognitionBaseActivity {

    @Inject
    Poem7RecognitionPresenter mP7RPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_poem7_recognition);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.poem7_recognition);
    }

    @Override
    protected void initProgressBar() {
        mProgressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void initButtons() {
        mCharButtons = new ToggleButton[Poem7RecognitionPresenter.OBSF_LENGTH];
        mAnsButtons = new Button[Poem7RecognitionPresenter.CF_LENGTH];

        mButtonHelp = findViewById(R.id.btHelp);
        mButtonNext = findViewById(R.id.btNext);

        int i = 0;
        mAnsButtons[i++] = findViewById(R.id.btAnsChar);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar1);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar2);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar3);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar4);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar5);
        mAnsButtons[i++] = findViewById(R.id.btAnsChar6);

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
        mCharButtons[i++] = findViewById(R.id.btChar9);
        mCharButtons[i++] = findViewById(R.id.btChar10);
        mCharButtons[i++] = findViewById(R.id.btChar11);
    }

    @Override
    protected void selectLevelAndInitPresenter() {
        Resources res = getResources();
        SingleChoiceDlgFragment dlg = SingleChoiceDlgFragment.newInstance(
                res.getString(R.string.level_title), res.getStringArray(R.array.levels_array),
                new SingleChoiceDlgFragment.OnChooseListener() {
                    @Override
                    public void onChoose(int which) {
                        // presenter, was injected
                        mP7RPresenter.setLevel(which);
                        mP7RPresenter.start(Poem7RecognitionActivity.this);
                        mPresenter = mP7RPresenter;
                    }

                    @Override
                    public void onCancel() {
                        NavUtils.navigateUpFromSameTask(Poem7RecognitionActivity.this);
                    }
                });
        dlg.setCancelable(false);
        dlg.show(getSupportFragmentManager(), "level_dlg");
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
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(POEM, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Poem Help");
    }

}
