package com.easysoftware.drill.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class CFPairWithKeywordIdiomItem extends CFPairWithKeywordItem {
    public static final String VALID_IDIOM_TEXT = "成语正确";
    public static final String INVALID_IDIOM_TEXT = "成语不正确";
    public static final String EXPLANATION_TEXT = "成语解释";

    protected List<ChineseFragment> mCFList;

    public CFPairWithKeywordIdiomItem(@CFPType int type, char keywordIn, List<ChineseFragment> cfList) {
        super(type, keywordIn);
        mType = type;
        mCFList = cfList;
    }

    @Override
    public String getFirstExplanation() {
        return EXPLANATION_TEXT;
    }

    @Override
    public String getSecondExplanation() {
        return EXPLANATION_TEXT;
    }

    protected boolean checkCF(ChineseFragment cf) {
        return mCFList.contains(cf);
    }

    @Override
    protected boolean checkFirstCF(ChineseFragment cf) {
        boolean succeed = checkCF(cf);
        mFirstVerificationText = succeed ? VALID_IDIOM_TEXT : INVALID_IDIOM_TEXT;
        return succeed;
    }

    @Override
    protected boolean checkSecondCF(ChineseFragment cf) {
        boolean succeed = checkCF(cf);
        mSecondVerificationText = succeed ? VALID_IDIOM_TEXT : INVALID_IDIOM_TEXT;
        return succeed;
    }

}
