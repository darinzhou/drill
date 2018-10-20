package com.easysoftware.drill.ui.solitaire.idiom;

import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.ui.solitaire.SolitaireBaseActivity;
import com.easysoftware.drill.ui.solitaire.SolitaireBasePresenter;
import com.easysoftware.drill.ui.util.HelpDlgFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.TYPE.POEM;

public class IdiomSolitaireActivity extends SolitaireBaseActivity {

    @Inject
    IdiomSolitairePresenter mIRPresenter;

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initInstructions() {

    }

    @Override
    protected void initKeywordInfo() {

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
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(POEM, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Poem Help");
    }
}
