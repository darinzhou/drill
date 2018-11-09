package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.CFPairItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public abstract class MainBasePresenter implements MainContract.Presenter {

    protected CFItemDbHelper mDbHelper;
    protected MainContract.View mView;
    protected CompositeDisposable mCompositeDisposable;
    protected List<CFItem> mCFItems;

    public MainBasePresenter() {
        mCompositeDisposable = new CompositeDisposable();
        mCFItems = new ArrayList<>();
    }

    protected void setDbHelper(CFItemDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public void start(MainContract.View view) {
        mView = view;
    }

    @Override
    public void stop() {
        mCompositeDisposable.dispose();
    }

    @Override
    public int getCFItemCount() {
        return mCFItems.size();
    }

    @Override
    public void onBindCFItemView(MainContract.CFItemView viewHolder, int position) {

    }

    @Override
    public void onDisplayItem(int position) {

    }
}
