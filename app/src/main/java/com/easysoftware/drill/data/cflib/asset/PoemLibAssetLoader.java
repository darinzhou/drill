package com.easysoftware.drill.data.cflib.asset;

import android.content.Context;
import android.text.TextUtils;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.localstorage.SharedPrefStorage;
import com.easysoftware.drill.data.model.ChineseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.easysoftware.drill.util.Constants.Level.LEVEL_KEY;

public class PoemLibAssetLoader implements PoemLibLoader {
    public static final String BASIC_LIB_FILE_NAME = "basic.txt";
    public static final String INTERMEDIATE_LIB_FILE_NAME = "intermediate.txt";
    public static final String ADVANCED_LIB_FILE_NAME = "advanced.txt";

    private Context mContext;
    private LocalStorage mLocalStorage;
    private InputStream mIS;

    public PoemLibAssetLoader(Context context, LocalStorage localStorage) {
        mContext = context;
        mLocalStorage = localStorage;
    }

    public PoemLibAssetLoader(InputStream is) {
        mIS = is;
    }

    public String getPoemLibAssetFilename(int level) {
        switch (level) {
            case 2:
                return ADVANCED_LIB_FILE_NAME;
            case 1:
                return INTERMEDIATE_LIB_FILE_NAME;
            case 0:
            default:
                return BASIC_LIB_FILE_NAME;
        }
    }

    @Override
    public List<ChineseFragment> load() throws IOException {
        if (mIS == null) {
            int level = mLocalStorage.read(LEVEL_KEY, 0);
            mIS = mContext.getAssets().open(getPoemLibAssetFilename(level));
        }
        return CFLibAssetUtil.load(mIS);
    }
}
