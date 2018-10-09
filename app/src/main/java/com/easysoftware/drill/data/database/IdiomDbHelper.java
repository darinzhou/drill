package com.easysoftware.drill.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.data.model.Idiom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class IdiomDbHelper extends DbHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "idioms.db";

    private static IdiomDbHelper sInstance;

    public IdiomDbHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public static IdiomDbHelper getInstance(Context context, boolean forceToOverwrite) {
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
        sInstance = new IdiomDbHelper(context);
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected String getString(Cursor cursor, String columName) {
        byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(columName));
        return blobToString(blob);
    }

    public List<Idiom> getIdioms(String content) {
        // Filter results WHERE "title" = 'My Title'
        String selection = IdiomContract.IdiomTable.COLUMN_NAME_CONTENT + " like ?";

        // Where clause arguments
        String[] selectionArgs = {"%" + content + "%"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                IdiomContract.IdiomTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Idiom> idioms = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_CONTENT);
                String pinyin = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_PINYIN);
                String explanation = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_EXPLANATION);
                String derivation = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_DERIVATION);
                String example = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_EXAMPLE);
                idioms.add(new Idiom(text, pinyin, explanation, derivation, example));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return idioms;
    }
    public Observable<List<Idiom>> getIdiomsObservable(String content) {
        return Observable.fromCallable(new Callable<List<Idiom>>() {
            @Override
            public List<Idiom> call() throws Exception {
                return getIdioms(content);
            }
        });
    }

    public List<ChineseFragment> getChineseFragments() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                IdiomContract.IdiomTable.COLUMN_NAME_CONTENT
        };

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                IdiomContract.IdiomTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<ChineseFragment> cfList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String s = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_CONTENT);
                if (!s.isEmpty()) {
                    cfList.add(new ChineseFragment(s));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return cfList;
    }
}