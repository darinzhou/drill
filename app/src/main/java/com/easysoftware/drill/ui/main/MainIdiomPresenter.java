package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.CFItem;

import java.util.List;

import javax.inject.Inject;

public class MainIdiomPresenter extends MainBasePresenter {

    @Inject
    public MainIdiomPresenter(IdiomDbHelper dbHelper) {
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
        List<String> texts = item.getFormattedTexts();
        return texts.get(0) + " " + texts.get(1);
    }

    @Override
    protected String buildContent(CFItem item) {
        List<String> texts = item.getFormattedTexts();
        return texts.get(2);
    }

}
