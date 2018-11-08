package com.easysoftware.drill.ui.recognition;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.easysoftware.drill.R;

import com.easysoftware.drill.base.BaseActivity;
import com.easysoftware.drill.ui.util.AutoDismissDlgFragment;

import java.util.HashMap;

public abstract class RecognitionBaseActivity extends BaseActivity implements RecognitionContract.View {
    public static final int COLOR_EMPTY = Color.argb(0xff, 0xaa, 0xaa, 0xaa);
    public static final int COLOR_FILLED = Color.argb(0xff, 0, 0xdd, 0xff);
    public static final int NOTIFICATION_DURATION = 2000;

    protected ToggleButton[] mCharButtons;
    protected Button[] mAnsButtons;
    protected Button mButtonHelp;
    protected Button mButtonNext;
    protected ProgressBar mProgressBar;

    protected HashMap<ToggleButton, Button> mAnsMap = new HashMap<>();

    // will be injected in subclasses
    protected RecognitionBasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();

        // injection
        initInjection();

        // UI
        initTitle();
        initButtons();
        initProgressBar();

        // bring progress bar in front of buttons
        ViewCompat.setTranslationZ(mProgressBar, 8);

        // Help button behavior
        mButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onHelp();
            }
        });

        // Next button behavior
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onNext(getAnswer());
                clearAnswer();
                lockButtons(false);
            }
        });

        // Char buttons behavior
        for (ToggleButton tb : mCharButtons) {
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Button firstEmptyAnsButton = getFirstEmptyAnsButton();
                        if (firstEmptyAnsButton != null) {
                            firstEmptyAnsButton.setText(buttonView.getText());
                            firstEmptyAnsButton.setBackgroundColor(COLOR_FILLED);
                            mAnsMap.put((ToggleButton) buttonView, firstEmptyAnsButton);
                        } else {
                            buttonView.setChecked(false);
                        }
                    } else {
                        Button ansButton = mAnsMap.get(buttonView);
                        if (ansButton != null) {
                            mAnsMap.get(buttonView).setText("");
                            mAnsMap.get(buttonView).setBackgroundColor(COLOR_EMPTY);
                            mAnsMap.remove((ToggleButton) buttonView);
                        }
                    }
                }
            });
        }

        // select level and init presenter
        selectLevelAndInitPresenter();
    }

    protected abstract void initContentView();

    protected abstract void initTitle();

    protected abstract void initProgressBar();

    protected abstract void initButtons();

    protected abstract void selectLevelAndInitPresenter();

    protected abstract void initInjection();

    protected abstract void cleanInjection();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.stop();
        }
        cleanInjection();
    }

    protected Button getFirstEmptyAnsButton() {
        if (mAnsMap.size() == mPresenter.getCFLength()) {
            return null;
        }

        for (Button bt : mAnsButtons) {
            if (bt.getText().toString().isEmpty()) {
                return bt;
            }
        }
        return null;
    }

    protected String getAnswer() {
        String s = "";
        for (Button bt : mAnsButtons) {
            s += bt.getText().toString();
        }
        return s;
    }

    protected void clearAnswer() {
        mAnsMap.clear();
        for (Button bt : mAnsButtons) {
            bt.setBackgroundColor(COLOR_EMPTY);
            bt.setText("");
        }
    }

    protected void lockButtons(boolean lock) {
        for (ToggleButton tb : mCharButtons) {
            tb.setClickable(!lock);
        }
    }

    //--------------------------------------------------------------------------------------------
    // implements methods of in contract
    //--------------------------------------------------------------------------------------------

    @Override
    public void displayChallenge(String obfuscation) {
        for (int i = 0; i < obfuscation.length(); ++i) {
            mCharButtons[i].setChecked(false);
            String c = Character.toString(obfuscation.charAt(i));
            mCharButtons[i].setText(c);
            mCharButtons[i].setTextOn(c);
            mCharButtons[i].setTextOff(c);
        }
    }

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
                        mPresenter.generateNext();
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
                        mPresenter.generateNext();
                    }
                });
        dlg.show(getSupportFragmentManager(), "auto_dismiss_dlg");
    }

    @Override
    public void displayCorrectAnswer(String answer) {
        clearAnswer();
        for (int i = 0; i < answer.length(); ++i) {
            for (ToggleButton tb : mCharButtons) {
                if (!tb.isChecked()) {
                    if (answer.charAt(i) == tb.getText().charAt(0)) {
                        tb.performClick();
                    }
                }
            }
        }
        lockButtons(true);
    }
}
