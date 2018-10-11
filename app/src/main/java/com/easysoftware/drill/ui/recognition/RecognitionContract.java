package com.easysoftware.drill.ui.recognition;

import com.easysoftware.drill.base.MvpContract;

import java.io.FileNotFoundException;
import java.util.List;

public interface RecognitionContract {
    interface View extends MvpContract.MvpView {
        void displayChallenge(String obfuscation);
        void displayNotificationForCorrectAnswer(int countTotal, int countCorrect);
        void displayNotificationForWrongAnswer(int countTotal, int countCorrect, String answer);
        void displayHelp(List<String> texts);
        void displayCorrectAnswer(String answer);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void setLevel(int level);
        void loadChineseFragmentLibrary() throws FileNotFoundException;
        void generateNext();
        void onNext(String result);
        void onHelp();
        boolean isHelpCalled();
    }
}
