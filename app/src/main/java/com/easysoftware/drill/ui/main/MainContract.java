package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.base.MvpContract;

import java.util.List;

public interface MainContract {
    interface View extends MvpContract.MvpView {
        void displayItem(List<String> texts, int type);
    }

    interface CFItemView {
        void display(String title, String content);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        int getCFItemCount();
        void onBindCFItemView(CFItemView viewHolder, int position);
        void onViewItemDetails(int position);
        Object performFiltering(String constraint);
        void updateFilterResults(Object values);
    }
}
