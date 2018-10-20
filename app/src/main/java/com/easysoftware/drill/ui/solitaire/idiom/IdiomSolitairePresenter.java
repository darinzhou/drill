package com.easysoftware.drill.ui.solitaire.idiom;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.ui.solitaire.SolitaireBasePresenter;

import javax.inject.Inject;

public class IdiomSolitairePresenter extends SolitaireBasePresenter {
    private IdiomDbHelper mDbHelper;

    @Inject
    public IdiomSolitairePresenter(IdiomDbHelper dbHelper) {
        super();
        mDbHelper = dbHelper;
    }

    @Override
    protected String generateKeyword() {
        return null;
    }

    @Override
    public void onHelp() {

    }

    @Override
    public void generateNext() {

    }

    @Override
    public void onViewDetailsFirst(int position) {

    }

    @Override
    public void onViewDetailsSecond(int position) {

    }
}
