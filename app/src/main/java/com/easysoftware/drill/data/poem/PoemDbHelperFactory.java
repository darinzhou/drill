package com.easysoftware.drill.data.poem;

import android.content.Context;

import com.easysoftware.drill.data.localstorage.LocalStorage;

import static com.easysoftware.drill.util.Constants.Level.LEVEL_KEY;

public class PoemDbHelperFactory {
    public static PoemDbHelper getPoemDbHelper(Context context, int level, boolean forceToOverrite) {
        switch (level) {
            case 2:
                return AdvancedPoemDbHelper.getInstance(context, forceToOverrite);
            case 1:
                return IntermediatePoemDbHelper.getInstance(context, forceToOverrite);
            case 0:
            default:
                return BasicPoemDbHelper.getInstance(context, forceToOverrite);
        }
    }

    public static PoemDbHelper getPoemDbHelper(Context context, LocalStorage localStorage,
                                               boolean forceToOverrite) {
        int level = localStorage.read(LEVEL_KEY, 0);
        return getPoemDbHelper(context, level, forceToOverrite);
    }
}
