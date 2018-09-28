package com.easysoftware.drill.data.poem;

import android.content.Context;

import java.io.IOException;

public class IntermediatePoemDbHelper extends PoemDbHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "intermediate.db";

    private static PoemDbHelper sInstance;

    public IntermediatePoemDbHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public static PoemDbHelper getInstance(Context context, boolean forceToOverwrite) {
        if (sInstance != null) {
            return sInstance;
        }

        // copy db
        if (forceToOverwrite || !dbExists(context, DATABASE_NAME)) {
            try {
                copyDb(context, DATABASE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // create instance
        sInstance = new BasicPoemDbHelper(context);
        return sInstance;
    }
}
