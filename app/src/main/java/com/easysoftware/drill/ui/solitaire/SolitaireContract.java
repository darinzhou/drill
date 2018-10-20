package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.base.MvpContract;

import java.util.List;

public interface SolitaireContract {
    interface View extends MvpContract.MvpView {
        void displayNotificationForCorrectAnswer(int countCorrect, String text);
        void displayNotificationForWrongAnswer(int countCorrect, String text);
        void displayHelp(List<String> texts);
        CFPairItemView getCFPairItemView(int position);
    }

    interface CFPairItemView {
        void setFirst(String text, List<Integer> keywordPositions, String explanation);
        void setSecond(String text, List<Integer> keywordPositions, String explanation);
        String getAnswer();
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void onHelp();
        void generateNext();
        int getPairItemCount();
        void onBindPairItemView(CFPairItemView viewHolder, int position);
        void onSubmitAnswer();
        void onViewDetailsFirst(int position);
        void onViewDetailsSecond(int position);
    }
}
