package com.easysoftware.drill.ui.solitaire.headandtail.poem;

import android.content.res.Resources;
import android.os.Bundle;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.solitaire.SolitaireBaseActivity;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.InputDlgFragment;
import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.HelpType.POEM;

public class PoemSolitaireActivity extends SolitaireBaseActivity {

    @Inject
    PoemSolitairePresenter mIRPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.poem_solitaire);
    }

    @Override
    protected void initInstructions() {
        mTvInstructions.setText(R.string.poem_solitaire_instruction);
    }

    @Override
    public void displayInitialKeyword() {
        String html = getResources().getString(R.string.idiom_solitaire_keyword, mPresenter.getInitialKeyword());
        Utils.displayHtml(mTvKeywordInfo, html);
    }

    @Override
    protected void selectLevelAndInitPresenter() {
        // presenter, was injected
        mIRPresenter.start(this);
        mPresenter = mIRPresenter;
    }

    @Override
    protected void initInjection() {
        ((DrillApp) getApplication()).createActivityComponent().inject(this);
    }

    @Override
    protected void cleanInjection() {
        ((DrillApp) getApplication()).releaseActivityComponent();
    }

    @Override
    public void displayItemDetails(List<String> texts) {
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(POEM, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Idiom Help");
    }

    @Override
    public void inputInitialKeyword() {
        Resources res = getResources();
        InputDlgFragment idf  = InputDlgFragment.newInstance(
                res.getString(R.string.input_initial_keyword_ht_idiom_title),
                res.getString(R.string.input_initial_keyword_ht_poem_message),
                new InputDlgFragment.OnDismissListener() {
                    @Override
                    public void onOK(String input) {
                        mIRPresenter.generateFirst(input);
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }
                });
        idf.show(getSupportFragmentManager(), "Poem Initial Keyword");
    }
}
