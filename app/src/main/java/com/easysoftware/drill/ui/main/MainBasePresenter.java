package com.easysoftware.drill.ui.main;

import android.text.TextUtils;

import com.easysoftware.drill.data.database.CFItemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.CFPairItem;
import com.easysoftware.drill.data.model.Verse;

import java.util.ArrayList;
import java.util.Arrays;
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
        CFItem item = mCFItems.get(position);
        viewHolder.display(buildTitle(item), buildContent(item));
    }

    @Override
    public Object performFiltering(String constraint) {
        constraint = constraint.trim();
        if (TextUtils.isEmpty(constraint)) {
            return mCFItems;
        }

        String[] keywords = constraint.split(" ");
        return mDbHelper.getCFItemsContainKeywords(new ArrayList<>(Arrays.asList(keywords)));
    }

    @Override
    public void updateFilterResults(Object values) {
        mCFItems = (List<CFItem>) values;
    }

    // abstract methods
    protected abstract String buildTitle(CFItem item);
    protected abstract String buildContent(CFItem item);

}
