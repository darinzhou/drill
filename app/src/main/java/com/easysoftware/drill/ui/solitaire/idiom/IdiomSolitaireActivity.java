package com.easysoftware.drill.ui.solitaire.idiom;

import android.os.Bundle;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.data.model.Idiom;
import com.easysoftware.drill.ui.solitaire.SolitaireBaseActivity;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.TYPE.IDIOM;
import static com.easysoftware.drill.util.Constants.TYPE.POEM;

public class IdiomSolitaireActivity extends SolitaireBaseActivity {

    @Inject
    IdiomSolitairePresenter mIRPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.idiom_solitaire);
    }

    @Override
    protected void initInstructions() {
        mTvInstructions.setText(R.string.idiom_solitaire_instruction);
    }

    @Override
    protected void initKeywordInfo() {
        String html = getResources().getString(R.string.idiom_solitaire_keyword, mPresenter.getInitialKeyword());
        UiUtils.displayHtml(mTvKeywordInfo, html);
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
    public void displayHelp(List<String> texts) {
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(IDIOM, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Idiom Help");
    }
}
