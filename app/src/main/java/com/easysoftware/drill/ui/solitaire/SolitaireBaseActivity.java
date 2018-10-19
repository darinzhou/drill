package com.easysoftware.drill.ui.solitaire;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.easysoftware.drill.R;
import com.easysoftware.drill.base.BaseActivity;
import com.easysoftware.drill.ui.recognition.RecognitionContract;
import com.easysoftware.drill.ui.util.AutoDismissDlgFragment;

import java.util.List;

public abstract class SolitaireBaseActivity extends BaseActivity implements RecognitionContract.View {
    public static final int COLOR_EMPTY = Color.argb(0xff, 0xaa, 0xaa, 0xaa);
    public static final int COLOR_FILLED = Color.argb(0xff, 0, 0xdd, 0xff);
    public static final int NOTIFICATION_DURATION = 2000;

    protected Button mHelp;
    protected ProgressBar mProgressBar;

    // will be injected in subclasses
    protected SolitaireBasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();

        // injection
        initInjection();

        // UI
        initTitle();
        initCFPairItemRecyclerView();
        initProgressBar();

        // bring progress bar in front of buttons
        ViewCompat.setTranslationZ(mProgressBar, 8);

        // Help button behavior
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onHelp();
            }
        });

        // select level and init presenter
        initPresenter();
    }

    protected abstract void initContentView();

    protected abstract void initTitle();

    protected abstract void initProgressBar();

    protected abstract void initCFPairItemRecyclerView();

    protected abstract void initPresenter();

    protected abstract void initInjection();

    protected abstract void cleanInjection();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
        cleanInjection();
    }

    //--------------------------------------------------------------------------------------------
    // implements methods of IdiomRecognitionContract.View
    //--------------------------------------------------------------------------------------------

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayNotificationForCorrectAnswer(int countTotal, int countCorrect) {
        String title = getResources().getString(R.string.correct);
        String message = getResources().getString(R.string.count_total, countTotal) + "\n" +
                getResources().getString(R.string.count_correct, countCorrect) + "\n" +
                getResources().getString(R.string.count_wrong, countTotal - countCorrect) + "\n" +
                getResources().getString(R.string.correct_rate, (countCorrect * 100) / countTotal) + "\n";
        AutoDismissDlgFragment dlg = AutoDismissDlgFragment.newInstance(title, message, NOTIFICATION_DURATION,
                new AutoDismissDlgFragment.OnDismissListener() {

                    @Override
                    public void onDismiss() {
//                        mPresenter.generateNext();
                    }
                });
        dlg.show(getSupportFragmentManager(), "auto_dismiss_dlg");
    }

    @Override
    public void displayNotificationForWrongAnswer(int countTotal, int countCorrect, String answer) {
        String title = getResources().getString(R.string.wrong);
        String message = getResources().getString(R.string.correct_answer, answer) + "\n\n" +
                getResources().getString(R.string.count_total, countTotal) + "\n" +
                getResources().getString(R.string.count_correct, countCorrect) + "\n" +
                getResources().getString(R.string.count_wrong, countTotal - countCorrect) + "\n" +
                getResources().getString(R.string.correct_rate, (countCorrect * 100) / countTotal) + "\n";
        AutoDismissDlgFragment dlg = AutoDismissDlgFragment.newInstance(title, message, NOTIFICATION_DURATION,
                new AutoDismissDlgFragment.OnDismissListener() {

                    @Override
                    public void onDismiss() {
//                        mPresenter.generateNext();
                    }
                });
        dlg.show(getSupportFragmentManager(), "auto_dismiss_dlg");
    }

    protected abstract void showHelp(List<String> texts);

    @Override
    public void displayHelp(List<String> texts) {
        showHelp(texts);
    }

}
