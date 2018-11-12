package com.easysoftware.drill.ui.main;

import android.text.TextUtils;
import android.util.Pair;

import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.data.model.Verse;
import com.easysoftware.drill.util.Utils;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.HelpType.POEM;

public class MainPoemPresenter extends MainBasePresenter {

    @Inject
    public MainPoemPresenter(PoemDbHelper dbHelper) {
        super();
        setDbHelper(dbHelper);
    }

    @Override
    public void onViewItemDetails(int position) {
        mView.displayItem(mCFItems.get(position).getFormattedTexts(), POEM);
    }

    @Override
    protected String buildTitle(CFItem item) {
        Verse verse = (Verse) item;
        Poem poem = verse.getPoem();

        String text = verse.getText();
        if (TextUtils.isEmpty(text)) {
            return poem.getMarkString();
        }

        Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(text);
        text = pair.first;
        if (TextUtils.isEmpty(text)) {
            return poem.getMarkString();
        }

        return text + " ~ " + poem.getMarkString();
    }

    @Override
    protected String buildContent(CFItem item) {
        Verse verse = (Verse) item;
        Poem poem = verse.getPoem();
        String text = "";
        for (String s : poem.getSentences()) {
            text += s;
        }
        return text;
    }

}
