package com.easysoftware.drill.ui.solitaire;

import com.easysoftware.drill.base.MvpContract;

import java.util.List;

public interface SolitaireContract {
    interface View extends MvpContract.MvpView {
        void notifyLastItemChanged();
        void displayNotificationForCorrectAnswer(int countCorrect, String text);
        void displayNotificationForWrongAnswer(int countCorrect, String text);
        void displayNotificationForSurrender(int countCorrect, String text);
        void displayItemDetails(List<String> texts);
        void displayHelp(List<String> texts);
        CFPairItemView getCFPairItemView(int position);
        void inputInitialKeyword();
        void displayKeyword();
    }

    interface CFPairItemView {
        void setItemTextSize(float sp);
        void setFirst(String text, List<Integer> keywordPositions, String explanation);
        void setSecond(String text, List<Integer> keywordPositions, String explanation);
        void emptySecond(boolean empty);
        String getAnswer();
        void setAnswer(String answer);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        String getKeyword();
        void onHelp();
        void generateNext();
        int getPairItemCount();
        void onBindPairItemView(CFPairItemView viewHolder, int position);
        void onSubmitAnswer();
        void onSubmitAnswer(String answer);
        void onViewDetailsFirst(int position);
        void onViewDetailsSecond(int position);
    }
}
