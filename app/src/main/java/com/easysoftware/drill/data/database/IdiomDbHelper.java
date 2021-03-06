package com.easysoftware.drill.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.data.model.Idiom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class IdiomDbHelper extends DbHelper implements CFItemDbHelper {
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

    private String getString(Cursor cursor, String columName) {
        byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(columName));
        return blobToString(blob);
    }

    public List<ChineseFragment> getChineseFragments() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                IdiomContract.IdiomTable.COLUMN_NAME_TEXT
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
                String s = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
                if (!s.isEmpty()) {
                    cfList.add(new ChineseFragment(s));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return cfList;
    }

    public Idiom getIdiom(String content) {
        // Filter results WHERE "title" = 'My Title'
        String selection = IdiomContract.IdiomTable.COLUMN_NAME_TEXT + "=?";

        // Where clause arguments
        String[] selectionArgs = {content};

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

        Idiom idiom = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
                String pinyin = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_PINYIN);
                String explanation = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_EXPLANATION);
                String derivation = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_DERIVATION);
                String example = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_EXAMPLE);
                idiom = new Idiom(text, pinyin, explanation, derivation, example);
                break;
            } while (cursor.moveToNext());

            cursor.close();
        }

        return idiom;
    }
    public Observable<Idiom> getIdiomObservable(String content) {
        return Observable.fromCallable(new Callable<Idiom>() {
            @Override
            public Idiom call() throws Exception {
                return getIdiom(content);
            }
        });
    }

    public List<Idiom> getIdiomsStartwith(String start) {
        // Filter results WHERE "title" = 'My Title'
        String selection = IdiomContract.IdiomTable.COLUMN_NAME_TEXT + " like ?";

        // Where clause arguments
        String[] selectionArgs = {start + "%"};

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
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
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
    public Observable<List<Idiom>> getIdiomsStartwithObservable(String start) {
        return Observable.fromCallable(new Callable<List<Idiom>>() {
            @Override
            public List<Idiom> call() throws Exception {
                return getIdiomsStartwith(start);
            }
        });
    }

    public List<Idiom> getIdiomsEndwith(String end) {
        // Filter results WHERE "title" = 'My Title'
        String selection = IdiomContract.IdiomTable.COLUMN_NAME_TEXT + " like ?";

        // Where clause arguments
        String[] selectionArgs = {"%" + end};

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
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
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
    public Observable<List<Idiom>> getIdiomsEndwithObservable(String end) {
        return Observable.fromCallable(new Callable<List<Idiom>>() {
            @Override
            public List<Idiom> call() throws Exception {
                return getIdiomsEndwith(end);
            }
        });
    }

    public List<Idiom> getIdiomsContainKeyword(String keyword) {
        // Filter results WHERE "title" = 'My Title'
        String selection = IdiomContract.IdiomTable.COLUMN_NAME_TEXT + " like ?";

        // Where clause arguments
        String[] selectionArgs = {"%" + keyword + "%"};

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
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
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
    public Observable<List<Idiom>> getIdiomsContainKeywordObservable(String keyword) {
        return Observable.fromCallable(new Callable<List<Idiom>>() {
            @Override
            public List<Idiom> call() throws Exception {
                return getIdiomsContainKeyword(keyword);
            }
        });
    }

    public List<Idiom> getIdiomsContainKeywords(List<String> keywords) {
        List<Idiom> idioms = new ArrayList<>();
        if (keywords == null || keywords.size() == 0) {
            return idioms;
        }

        // build Where clause and arguments
        String selection = "";
        String[] selectionArgs = new String[keywords.size()];
        int i = 0;
        for (String kw : keywords) {
            if (!selection.isEmpty()) {
                selection += " and ";
            }
            selection += IdiomContract.IdiomTable.COLUMN_NAME_TEXT + " like ?";
            selectionArgs[i++] = "%" + kw + "%";
        }

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

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String text = getString(cursor, IdiomContract.IdiomTable.COLUMN_NAME_TEXT);
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
    public Observable<List<Idiom>> getIdiomsContainKeywordsObservable(List<String> keywords) {
        return Observable.fromCallable(new Callable<List<Idiom>>() {
            @Override
            public List<Idiom> call() throws Exception {
                return getIdiomsContainKeywords(keywords);
            }
        });
    }

    // implement CFItemDbHelper methods

    @Override
    public CFItem getCFItem(String content) {
        return getIdiom(content);
    }

    @Override
    public Observable<CFItem> getCFItemObservable(String content) {
        return Observable.fromCallable(new Callable<CFItem>() {
            @Override
            public CFItem call() throws Exception {
                return getCFItem(content);
            }
        });
    }

    @Override
    public List<CFItem> getCFItemsStartwith(String start) {
        return new ArrayList<>(getIdiomsStartwith(start));
    }

    @Override
    public Observable<List<CFItem>> getCFItemsStartwithObservable(String start) {
        return Observable.fromCallable(new Callable<List<CFItem>>() {
            @Override
            public List<CFItem> call() throws Exception {
                return getCFItemsStartwith(start);
            }
        });
    }

    @Override
    public List<CFItem> getCFItemsEndwith(String end) {
        return new ArrayList<>(getIdiomsEndwith(end));
    }

    @Override
    public Observable<List<CFItem>> getCFItemsEndwithObservable(String end) {
        return Observable.fromCallable(new Callable<List<CFItem>>() {
            @Override
            public List<CFItem> call() throws Exception {
                return getCFItemsEndwith(end);
            }
        });
    }

    @Override
    public List<CFItem> getCFItemsContainKeyword(String keyword) {
        return new ArrayList<>(getIdiomsContainKeyword(keyword));
    }

    @Override
    public Observable<List<CFItem>> getCFItemsContainKeywordObservable(String keyword) {
        return Observable.fromCallable(new Callable<List<CFItem>>() {
            @Override
            public List<CFItem> call() throws Exception {
                return getCFItemsContainKeyword(keyword);
            }
        });
    }

    @Override
    public List<CFItem> getCFItemsContainKeywords(List<String> keywords) {
        return new ArrayList<>(getIdiomsContainKeywords(keywords));
    }

    @Override
    public Observable<List<CFItem>> getCFItemsContainKeywordsObservable(List<String> keywords) {
        return Observable.fromCallable(new Callable<List<CFItem>>() {
            @Override
            public List<CFItem> call() throws Exception {
                return getCFItemsContainKeywords(keywords);
            }
        });
    }

}
