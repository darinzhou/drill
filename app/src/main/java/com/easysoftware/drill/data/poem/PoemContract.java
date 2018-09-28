package com.easysoftware.drill.data.poem;

import android.provider.BaseColumns;

public final class PoemContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PoemContract() {}

    /* Inner class that defines the table contents */
    public static class PoemSentence implements BaseColumns {
        public static final String TABLE_NAME = "poem_sentence_table";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_PERIOD = "period";
        public static final String COLUMN_NAME_SN = "sn";
        public static final String COLUMN_NAME_WORDCOUNT = "wordcount";
        public static final String COLUMN_NAME_SENTENCE = "sentence";
    }
}