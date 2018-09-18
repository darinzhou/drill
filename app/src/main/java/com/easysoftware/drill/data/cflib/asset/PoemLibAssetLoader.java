package com.easysoftware.drill.data.cflib.asset;

import android.content.Context;

import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.cflib.asset.CFLibAssetUtil;
import com.easysoftware.drill.data.model.ChineseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PoemLibAssetLoader implements PoemLibLoader {
    public static final String LIB_FILE_NAME = "cf_lib_zhongxiaoxue_gushici.txt";//"poem.txt";

    private Context mContext;
    private InputStream mIS;

    public PoemLibAssetLoader(Context context) {
        mContext = context;
    }

    public PoemLibAssetLoader(InputStream is) {
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
