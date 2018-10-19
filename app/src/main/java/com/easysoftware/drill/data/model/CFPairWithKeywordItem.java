package com.easysoftware.drill.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class CFPairWithKeywordItem extends CFPairItem {
    public static final String VALID_KEYWORD_TEXT = "且符合要求";
    public static final String INVALID_KEYWORD_TEXT = "但不符合要求";

    // define types
    public static final int KEYWORD_HEAD_AND_TAIL = 0;
    public static final int KEYWORD_ANY_POSITION = 1;
    // Declare the @IntDef for type constants
    @IntDef({KEYWORD_HEAD_AND_TAIL, KEYWORD_ANY_POSITION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CFPType {}

    protected int mType;

    public CFPairWithKeywordItem(@CFPType int type, char keywordIn) {
        super(keywordIn);
        mType = type;
    }

    @Override
    public List<Integer> getKeywordPositionsInFirst() {
        List<Integer> kwPositions = new ArrayList<>();
        if (mType == KEYWORD_HEAD_AND_TAIL) {
            kwPositions.add(0);
            kwPositions.add(mFirst.length()-1);
        } else {
            int len = mFirst.length();
            for (int i=0; i< len; ++i) {
                if (mKeywordIn == mFirst.charAt(i)) {
                    kwPositions.add(i);
                }
            }
        }
        return kwPositions;
    }

    @Override
    public List<Integer> getKeywordPositionsInSecond() {
        List<Integer> kwPositions = new ArrayList<>();
        if (mType == KEYWORD_HEAD_AND_TAIL) {
            kwPositions.add(0);
            kwPositions.add(mSecond.length()-1);
        } else {
            int len = mSecond.length();
            for (int i=0; i< len; ++i) {
                if (mKeywordIn == mSecond.charAt(i)) {
                    kwPositions.add(i);
                }
            }
        }
        return kwPositions;
    }

    @Override
    protected boolean checkFirst(ChineseFragment first) {
        if (mFirst == null) {
            return false;
        }

        boolean succeed = false;
        if (mType == KEYWORD_HEAD_AND_TAIL) {
            succeed = first.getFirstChar() == mKeywordIn;
        } else {
            succeed = first.contains(mKeywordIn);
        }

        mFirstVerificationText += succeed ? VALID_KEYWORD_TEXT : INVALID_KEYWORD_TEXT;
        return succeed;
    }

    @Override
    protected boolean checkSecond(ChineseFragment second) {
        if (mFirst == null || mSecond == null) {
            return false;
        }

        boolean succeed = false;
        if (mType == KEYWORD_HEAD_AND_TAIL) {
            succeed = second.getFirstChar() == mFirst.getLastChar();
        } else {
            succeed = second.contains(mKeywordIn);
        }

        mSecondVerificationText += succeed ? VALID_KEYWORD_TEXT : INVALID_KEYWORD_TEXT;
        return succeed;
    }

    @Override
    protected char findKeywordOut() {
        if (mType == KEYWORD_HEAD_AND_TAIL) {
            return mSecond.getLastChar();
        }
        return mKeywordIn;
    }

}
