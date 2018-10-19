package com.easysoftware.drill.data.model;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class CFPairWithVersesItem extends CFPairItem {
    public static final String VALID_VERSE_TEXT = "诗句出自";
    public static final String INVALID_VERSE_TEXT = "诗句不正确";
    public static final String VALID_MATCH_TEXT = "且符合要求";
    public static final String INVALID_MATCH_TEXT = "但不符合要求";
    public static final String EXPLANATION_FORMAT1 = "%s·%s 《%s》";
    public static final String EXPLANATION_FORMAT2 = "%s·%s 《%s》（%s）";

    // define types
    public static final int MATCH_NEXT = 0;
    public static final int MATCH_PREVIOUS = 1;
    // Declare the @IntDef for type constants
    @IntDef({MATCH_NEXT, MATCH_PREVIOUS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CFPType {}

    protected int mType;
    protected List<Verse> mVerseList;
    protected Poem mFirstPoem;
    protected Poem mSecondPoem;

    public CFPairWithVersesItem(@CFPType int type, List<Verse> verseList) {
        super(' ');
        mType = type;
        mVerseList = verseList;
    }

    @Override
    protected boolean checkFirstCF(ChineseFragment cf) {
        mFirstPoem = Verse.findPoemWithSentence(cf.toString(), mVerseList);
        if (mFirstPoem != null) {
            mFirstVerificationText = VALID_VERSE_TEXT + getExplanation(mFirstPoem);
            return true;
        }

        mFirstVerificationText = INVALID_VERSE_TEXT;
        return false;
    }

    @Override
    protected boolean checkSecondCF(ChineseFragment cf) {
        mSecondPoem = Verse.findPoemWithSentence(cf.toString(), mVerseList);
        if (mSecondPoem != null) {
            mSecondVerificationText = VALID_VERSE_TEXT + getExplanation(mSecondPoem);
            return true;
        }

        mSecondVerificationText = INVALID_VERSE_TEXT;
        return false;
    }

    @Override
    protected boolean checkFirst(ChineseFragment first) {
        if (mFirst == null) {
            return false;
        }

        int pos =  mFirstPoem.sentencePosition(first.toString());
        boolean succeed = false;
        if (mType == MATCH_NEXT) {
            succeed = pos < mFirstPoem.getSentences().size()-1;
        } else {
            succeed = pos > 0;
        }

        mFirstVerificationText += succeed ? VALID_MATCH_TEXT : INVALID_MATCH_TEXT;
        return succeed;
    }

    @Override
    protected boolean checkSecond(ChineseFragment second) {
        if (mFirst == null || mSecond == null) {
            return false;
        }

        int p1 = mFirstPoem.sentencePosition(mFirst.toString());
        int p2 = mSecondPoem.sentencePosition(second.toString());
        boolean succeed = false;
        if (mType == MATCH_NEXT) {
            succeed = (p2 == p1 + 1);
        } else {
            succeed = (p2 == p1 - 1);
        }

        mSecondVerificationText += succeed ? VALID_MATCH_TEXT : INVALID_MATCH_TEXT;
        return succeed;
    }

    @Override
    protected char findKeywordOut() {
        return 0;
    }

    @Override
    public List<Integer> getKeywordPositionsInFirst() {
        return null;
    }

    @Override
    public List<Integer> getKeywordPositionsInSecond() {
        return null;
    }

    private String getExplanation(Poem poem) {
        if (TextUtils.isEmpty(poem.getSubtitle())) {
            return String.format(EXPLANATION_FORMAT1, poem.getPeriod(), poem.getAuthor(), poem.getTitle());
        }
        return String.format(EXPLANATION_FORMAT2, poem.getPeriod(), poem.getAuthor(), poem.getTitle(), poem.getSubtitle());
    }

    @Override
    public String getFirstExplanation() {
        return getExplanation(mFirstPoem);
    }

    @Override
    public String getSecondExplanation() {
        return getExplanation(mFirstPoem);
    }
}
