package com.easysoftware.drill.data.model;

import java.util.List;

public class CFPairItem {
    protected String mFirst;
    protected String mSecond;

    protected String mKeywordIn;
    protected String mKeywordOut;

    protected String mFirstVerificationText;
    protected String mSecondVerificationText;

    protected String mFirstExplanation;
    protected String mSecondExplanation;

    protected List<Integer> mFirstKeywordPositions;
    protected List<Integer> mSecondKeywordPositions;

    protected List<String> mFirstTexts;
    protected List<String> mSecondTexts;

    public CFPairItem() {
    }

    public void setFirst(String text) {
        mFirst = text;
    }
    public void setSecond(String text) {
        mSecond = text;
    }

    public void setKeywordIn(String keywordIn) {
        mKeywordIn = keywordIn;
    }
    public void setKeywordOut(String keywordOut) {
        mKeywordOut = keywordOut;
    }

    public void setFirstVerificationText(String firstVerificationText) {
        mFirstVerificationText = firstVerificationText;
    }
    public void setSecondVerificationText(String secondVerificationText) {
        mSecondVerificationText = secondVerificationText;
    }

    public void setFirstExplanation(String firstExplanation) {
        mFirstExplanation = firstExplanation;
    }
    public void setSecondExplanation(String secondExplanation) {
        mSecondExplanation = secondExplanation;
    }

    public void setFirstKeywordPositions(List<Integer> firstKeywordPositions) {
        mFirstKeywordPositions = firstKeywordPositions;
    }
    public void setSecondKeywordPositions(List<Integer> secondKeywordPositions) {
        mSecondKeywordPositions = secondKeywordPositions;
    }

    public void setFirstTexts(List<String> firstTexts) {
        mFirstTexts = firstTexts;
    }
    public void setSecondTexts(List<String> secondTexts) {
        mSecondTexts = secondTexts;
    }

    public boolean isMatched() {
        return mFirst != null && mSecond != null;
    }

    public String getFirst() {
        return mFirst;
    }
    public String getSecond() {
        return mSecond;
    }

    public String getKeywordIn() {
        return mKeywordIn;
    }
    public String getKeywordOut() {
        return mKeywordOut;
    }

    public String getFirstVerificationText() {
        return mFirstVerificationText;
    }
    public String getSecondVerificationText() {
        return mSecondVerificationText;
    }

    public String getFirstExplanation() {
        return mFirstExplanation;
    }
    public String getSecondExplanation() {
        return mSecondExplanation;
    }

    public List<Integer> getFirstKeywordPositions() {
        return mFirstKeywordPositions;
    }
    public List<Integer> getSecondKeywordPositions() {
        return mSecondKeywordPositions;
    }

    public List<String> getFirstTexts() {
        return mFirstTexts;
    }
    public List<String> getSecondTexts() {
        return mSecondTexts;
    }

//    protected abstract boolean checkFirstCF(ChineseFragment first);
//    protected abstract boolean checkSecondCF(ChineseFragment second);
//    protected abstract boolean checkFirst(ChineseFragment first);
//    protected abstract boolean checkSecond(ChineseFragment second);
//
//    protected abstract char findKeywordOut();
//
}
