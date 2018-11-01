package com.easysoftware.drill.ui.solitaire.headandtail.poem;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.Verse;
import com.easysoftware.drill.ui.solitaire.SolitaireContract;
import com.easysoftware.drill.ui.solitaire.SolitaireWithKeywordHeadAndTailBasePresenter;

import javax.inject.Inject;

public class PoemSolitairePresenter extends SolitaireWithKeywordHeadAndTailBasePresenter {
    public static final String VALID_IDIOM_TEXT = "诗句正确";
    public static final String INVALID_IDIOM_TEXT = "诗句不正确：";
    public static final String VALID_KEYWORD_TEXT = "且符合要求：";
    public static final String INVALID_KEYWORD_TEXT = "答案不符合要求：";
    public static final String DUPLICATED_IDIOM_TEXT = "该诗句已经用过， 不能重复使用：";
    public static final String CANNOT_FIND_CORRECT_ANSWER_TEXT = "找不到符合要求的诗句";

    @Inject
    public PoemSolitairePresenter(PoemDbHelper dbHelper) {
        super(dbHelper);
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
    protected String getExplanationMessage(CFItem cfItem) {
        return ((Verse)cfItem).getPoem().getMarkString();
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

    @Override
    protected void setItemTextSize(SolitaireContract.CFPairItemView viewHolder) {
        viewHolder.setItemTextSize(16);
    }
}
