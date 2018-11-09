package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.data.database.IdiomDbHelper;

import javax.inject.Inject;

public class MainIdiomPresenter extends MainBasePresenter {

    @Inject
    public MainIdiomPresenter(IdiomDbHelper dbHelper) {
        super();
        setDbHelper(dbHelper);
    }
}
