package com.easysoftware.drill.data.cflib.asset;

import android.content.Context;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.model.ChineseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class IdiomLibAssetLoader implements IdiomLibLoader {
    public static final String LIB_FILE_NAME = "idioms.txt";

    private Context mContext;
    private InputStream mIS;

    public IdiomLibAssetLoader(Context context) {
        mContext = context;
    }

    public IdiomLibAssetLoader(InputStream is) {
        mIS = is;
    }

    @Override
    public List<ChineseFragment> load() throws IOException {
        if (mIS == null) {
            mIS = mContext.getAssets().open(LIB_FILE_NAME);
        }
        return CFLibAssetUtil.load(mIS);
    }
}
