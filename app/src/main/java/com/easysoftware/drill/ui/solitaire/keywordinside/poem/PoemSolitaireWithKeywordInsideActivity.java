package com.easysoftware.drill.ui.solitaire.keywordinside.poem;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.SolitaireBaseActivity;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.InputDlgFragment;
import com.easysoftware.drill.ui.util.SingleChoiceDlgFragment;
import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.HelpType.POEM;

public class PoemSolitaireWithKeywordInsideActivity extends SolitaireBaseActivity {

    @Inject
    PoemSolitaireWithKeywordInsidePresenter mIRPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTitle() {
        setTitle(R.string.poem_solitaire_keyword_inside);
    }

    @Override
    protected void initInstructions() {
        mTvInstructions.setText(R.string.poem_solitaire_keyword_inside_instruction);
    }

    @Override
    public void displayInitialKeyword() {
        String html = getResources().getString(R.string.solitaire_keyword_inside_keyword_info, mPresenter.getInitialKeyword());
        Utils.displayHtml(mTvKeywordInfo, html);
        mTvKeywordInfo.setVisibility(View.VISIBLE);
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
        String[] keywords = res.getStringArray(R.array.keywords_array);
        SingleChoiceDlgFragment dlg = SingleChoiceDlgFragment.newInstance(
                res.getString(R.string.keyword_title), keywords,
                new SingleChoiceDlgFragment.OnChooseListener() {
                    @Override
                    public void onChoose(int which) {
                        mIRPresenter.generateFirst(keywords[which]);                    }
                });
        dlg.setCancelable(false);
        dlg.show(getSupportFragmentManager(), "level_dlg");

    }
}
