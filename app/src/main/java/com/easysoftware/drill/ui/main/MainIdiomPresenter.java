package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.CFItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.easysoftware.drill.util.Constants.HelpType.IDIOM;

public class MainIdiomPresenter extends MainBasePresenter {

    @Inject
    public MainIdiomPresenter(IdiomDbHelper dbHelper) {
        super();
        setDbHelper(dbHelper);
    }

    @Override
    public void onViewItemDetails(int position) {
        mView.displayItem(mCFItems.get(position).getFormattedTexts(), IDIOM);
    }

    @Override
    protected String buildTitle(CFItem item) {
        List<String> texts = item.getFormattedTexts();
        return texts.get(0) + "   " + texts.get(1);
    }

    @Override
    protected String buildContent(CFItem item) {
        List<String> texts = item.getFormattedTexts();
        return texts.get(2);
    }

}
