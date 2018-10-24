package com.easysoftware.drill.data.model;

import android.text.TextUtils;

import java.util.List;

public class CFPairWithKeywordPoemItem extends CFPairWithKeywordItem {
    public static final String VALID_VERSE_TEXT = "诗句出自";
    public static final String INVALID_VERSE_TEXT = "诗句不正确";
    public static final String EXPLANATION_FORMAT1 = "%s·%s 《%s》";
    public static final String EXPLANATION_FORMAT2 = "%s·%s 《%s》（%s）";

    protected List<Verse> mVerseList;
    protected Poem mFirstPoem;
    protected Poem mSecondPoem;

    public CFPairWithKeywordPoemItem(@CFPType int type, char keywordIn, List<Verse> cfList) {
        super(type, keywordIn);
        mType = type;
        mVerseList = cfList;
    }

    protected boolean checkFirstCF(ChineseFragment cf) {
        mFirstPoem = Verse.findPoemWithSentence(cf.toString(), mVerseList);
        if (mFirstPoem != null) {
            mFirstVerificationText = VALID_VERSE_TEXT + getExplanation(mFirstPoem);
            return true;
        }

        mFirstVerificationText = INVALID_VERSE_TEXT;
        return false;
    }

    protected boolean checkSecondCF(ChineseFragment cf) {
        mSecondPoem = Verse.findPoemWithSentence(cf.toString(), mVerseList);
        if (mSecondPoem != null) {
            mSecondVerificationText = VALID_VERSE_TEXT + getExplanation(mSecondPoem);
            return true;
        }

        mSecondVerificationText = INVALID_VERSE_TEXT;
        return false;
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
