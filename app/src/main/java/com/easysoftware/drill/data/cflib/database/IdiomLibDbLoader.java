package com.easysoftware.drill.data.cflib.database;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.ChineseFragment;

import java.util.List;

public class IdiomLibDbLoader implements IdiomLibLoader {

    private IdiomDbHelper mIdiomDbHelper;

    public IdiomLibDbLoader(IdiomDbHelper idiomDbHelper) {
        mIdiomDbHelper = idiomDbHelper;
    }

    @Override
    public List<ChineseFragment> load() {
        return mIdiomDbHelper.getChineseFragments();
    }
}
