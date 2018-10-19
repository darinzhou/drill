package com.easysoftware.drill.data.model;

import java.util.List;

public abstract class CFPairItem {
    protected ChineseFragment mFirst;
    protected ChineseFragment mSecond;
    protected char mKeywordIn;
    protected char mKeywordOut;

    // should be set in setFirst/Second, checkFirst/Second and checkFitstCF/SecondCF
    protected String mFirstVerificationText;
    protected String mSecondVerificationText;

    public CFPairItem(char keywordIn) {
        mKeywordIn = keywordIn;
    }

    public boolean setFirst(String text) {
        ChineseFragment first = new ChineseFragment(text);
        if (!checkFirstCF(first) || !checkFirst(first)) {
            return false;
        }

        mFirst = first;
        return true;
    }

    public boolean setSecond(String text) {
        ChineseFragment second = new ChineseFragment(text);
        if (!checkSecondCF(second) || !checkSecond(second)) {
            return false;
        }

        mSecond = second;
        mKeywordOut = findKeywordOut();

        return true;
    }

    public boolean isMatched() {
        return mFirst != null && mSecond != null;
    }

    public ChineseFragment getFirst() {
        return mFirst;
    }

    public ChineseFragment getSecond() {
        return mSecond;
    }

    public char getKeywordIn() {
        return mKeywordIn;
    }

    public char getKeywordOut() {
        return mKeywordOut;
    }

    public String getFirstVerificationText() {
        return mFirstVerificationText;
    }

    public String getSecondVerificationText() {
        return mSecondVerificationText;
    }

    protected abstract boolean checkFirstCF(ChineseFragment first);
    protected abstract boolean checkSecondCF(ChineseFragment second);
    protected abstract boolean checkFirst(ChineseFragment first);
    protected abstract boolean checkSecond(ChineseFragment second);

    protected abstract char findKeywordOut();

    public abstract List<Integer> getKeywordPositionsInFirst();
    public abstract List<Integer> getKeywordPositionsInSecond();

    public abstract String getFirstExplanation();
    public abstract String getSecondExplanation();
}
