package com.easysoftware.drill.data.database;

import android.provider.BaseColumns;

public final class IdiomContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private IdiomContract() {}

    /* Inner class that defines the table contents */
    public static class IdiomTable implements BaseColumns {
        public static final String TABLE_NAME = "idiom_table";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_PINYIN = "pinyin";
        public static final String COLUMN_NAME_EXPLANATION = "explanation";
        public static final String COLUMN_NAME_DERIVATION = "derivation";
        public static final String COLUMN_NAME_EXAMPLE = "example";
    }
}