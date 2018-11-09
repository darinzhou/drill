package com.easysoftware.drill.ui.main;

import com.easysoftware.drill.data.database.PoemDbHelper;

import javax.inject.Inject;

public class MainPoemPresenter extends MainBasePresenter {

    @Inject
    public MainPoemPresenter(PoemDbHelper dbHelper) {
        super();
        setDbHelper(dbHelper);
    }
}
