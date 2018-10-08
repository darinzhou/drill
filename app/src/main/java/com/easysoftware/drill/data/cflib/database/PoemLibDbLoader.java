package com.easysoftware.drill.data.cflib.database;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.data.database.PoemDbHelper;

import java.util.List;

import static com.easysoftware.drill.util.Constants.Level.LEVEL_KEY;

public class PoemLibDbLoader implements PoemLibLoader {

    private PoemDbHelper mPoemDbHelper;
    private LocalStorage mLocalStorage;

    public PoemLibDbLoader(PoemDbHelper poemDbHelper, LocalStorage localStorage) {
        mPoemDbHelper = poemDbHelper;
        mLocalStorage = localStorage;
    }

    @Override
    public List<ChineseFragment> load() {
        int level = mLocalStorage.read(LEVEL_KEY, 0);
        return mPoemDbHelper.getChineseFragments(level+1);
    }
}
