package com.easysoftware.drill.ui.solitaire;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easysoftware.drill.R;
import com.easysoftware.drill.base.BaseActivity;
import com.easysoftware.drill.ui.util.AutoDismissDlgFragment;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.easysoftware.drill.util.Constants.HelpType.IDIOM;
import static com.easysoftware.drill.util.Constants.HelpType.SOLITAIRE;

public abstract class SolitaireBaseActivity extends BaseActivity implements SolitaireContract.View {
    public static final int COLOR_EMPTY = Color.argb(0xff, 0xaa, 0xaa, 0xaa);
    public static final int COLOR_FILLED = Color.argb(0xff, 0, 0xdd, 0xff);
    public static final int NOTIFICATION_DURATION = 2000;

    protected TextView mTvInstructions;
    protected TextView mTvKeywordInfo;
    protected Button mButtonHelp;
    protected Button mButtonNext;
    protected ProgressBar mProgressBar;

    protected RecyclerView mRecyclerView;
    protected CFPairItemsRecyclerAdapter mAdapter;

    // will be injected in subclasses
    protected SolitaireBasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire);

        // injection
        initInjection();

        // title
        initTitle();

        // controls
        mProgressBar = findViewById(R.id.progressBar);
        mButtonHelp = findViewById(R.id.btHelp);
        mButtonNext = findViewById(R.id.btNext);
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
                mPresenter.onSubmitAnswer();
            }
        });
        mTvInstructions = findViewById(R.id.tvInstructions);
        mTvKeywordInfo = findViewById(R.id.tvKeywordInfo);
        initInstructions();

        // items
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // bring progress bar in front of buttons
        ViewCompat.setTranslationZ(mProgressBar, 8);

        // select level and init presenter
        selectLevelAndInitPresenter();

        // following calls should be after presenter initialized
        mAdapter = new CFPairItemsRecyclerAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected abstract void initTitle();
    protected abstract void initInstructions();
    protected abstract void selectLevelAndInitPresenter();
    protected abstract void initInjection();
    protected abstract void cleanInjection();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
        cleanInjection();
    }

    //--------------------------------------------------------------------------------------------
    // implements methods of defined in contract
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
    public void displayNotificationForCorrectAnswer(int countCorrect, String text) {
        String title = getResources().getString(R.string.correct);
        String message =  text + "\n" +
                getResources().getString(R.string.count_correct, countCorrect);
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
    public void displayNotificationForWrongAnswer(int countCorrect, String text) {
        String title = getResources().getString(R.string.wrong);
        String message = text + "\n" +
                getResources().getString(R.string.count_correct, countCorrect) + "\n";
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
    public void displayNotificationForSurrender(int countCorrect, String text) {
        String title = getResources().getString(R.string.surrender);
        String message = text + "\n" +
                getResources().getString(R.string.count_correct, countCorrect) + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        TextView textView = Utils.createDlgTitle(this, title);
        builder.setCustomTitle(textView);

        builder.create().show();
    }

    @Override
    public void notifyLastItemChanged() {
        int last = mAdapter.getItemCount()-1;
        mAdapter.notifyItemChanged(last);
        mRecyclerView.scrollToPosition(last);
    }

    @Override
    public SolitaireContract.CFPairItemView getCFPairItemView(int position) {
        return (SolitaireContract.CFPairItemView) mRecyclerView.findViewHolderForAdapterPosition(position);
    }

    @Override
    public void displayHelp(List<String> texts) {
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(SOLITAIRE, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Solitaire Help");
    }

}
