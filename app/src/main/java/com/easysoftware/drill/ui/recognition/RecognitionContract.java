package com.easysoftware.drill.ui.recognition;

import com.easysoftware.drill.base.MvpContract;

import java.io.FileNotFoundException;

public interface RecognitionContract {
    interface View extends MvpContract.MvpView {
        void displayChallenge(String obfuscation);
        void displayNotificationForCorrectAnswer(int countTotal, int countCorrect);
        void displayNotificationForWrongAnswer(int countTotal, int countCorrect, String answer);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void loadChineseFragmentLibrary() throws FileNotFoundException;
        void generateNext();
        void onNext(String result);
    }
}
