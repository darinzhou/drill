package com.easysoftware.drill.ui.main;

import android.text.TextUtils;

import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.data.model.Verse;

import java.util.List;

import javax.inject.Inject;

public class MainPoemPresenter extends MainBasePresenter {

    @Inject
    public MainPoemPresenter(PoemDbHelper dbHelper) {
        super();
        setDbHelper(dbHelper);
    }

    @Override
    public Object performFiltering(String constraint) {
        return null;
    }

    @Override
    public void updateFilterResults(Object values) {

    }

    @Override
    protected String buildTitle(CFItem item) {
        Verse verse = (Verse) item;
        Poem poem = verse.getPoem();
        String text = verse.getText();

        if (TextUtils.isEmpty(text)) {
            return poem.getMarkString();
        }

        return text + " ～ " + poem.getMarkString();
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
