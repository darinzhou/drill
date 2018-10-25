package com.easysoftware.drill.ui.solitaire.idiom;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.ui.solitaire.SolitaireWithKeywordHeadAndTailBasePresenter;

import javax.inject.Inject;

public class IdiomSolitairePresenter extends SolitaireWithKeywordHeadAndTailBasePresenter {
    public static final String VALID_IDIOM_TEXT = "成语正确";
    public static final String INVALID_IDIOM_TEXT = "成语不正确：";
    public static final String VALID_KEYWORD_TEXT = "且符合要求：";
    public static final String INVALID_KEYWORD_TEXT = "答案不符合要求：";
    public static final String DUPLICATED_IDIOM_TEXT = "该成语已经用过， 不能重复使用：";
    public static final String CANNOT_FIND_CORRECT_ANSWER_TEXT = "找不到符合要求的成语";
    public static final String EXPLANATION_TEXT = "成语解释";

    @Inject
    public IdiomSolitairePresenter(IdiomDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected String generateInitialKeyword() {
        return "一";
    }

    @Override
    protected String getCorrectTextMessage() {
        return VALID_IDIOM_TEXT;
    }

    @Override
    protected String getWrongTextMessage() {
        return INVALID_IDIOM_TEXT;
    }

    @Override
    protected String getDuplicatedTextMessage() {
        return DUPLICATED_IDIOM_TEXT;
    }

    @Override
    protected String getExplanationMessage() {
        return EXPLANATION_TEXT;
    }

    @Override
    protected String getCannotFindTextMessage() {
        return CANNOT_FIND_CORRECT_ANSWER_TEXT;
    }

    @Override
    protected String getValidKeywordTextMessage() {
        return VALID_KEYWORD_TEXT;
    }

    @Override
    protected String getInvalidKeywordTextMessage() {
        return INVALID_KEYWORD_TEXT;
    }
}
