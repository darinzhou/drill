package com.easysoftware.drill.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Pair;

import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.data.model.Poem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class PoemDbHelper extends DbHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "poems.db";

    private static PoemDbHelper sInstance;

    public PoemDbHelper(Context context) {
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
        sInstance = new PoemDbHelper(context);
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected String getString(Cursor cursor, String columName, String s) {
        if (TextUtils.isEmpty(s)) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(columName));
            s = blobToString(blob);
        }
        return s;
    }

    public Poem getPoem(String title, String subtitle, String author) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_PERIOD,
                PoemContract.PoemTable.COLUMN_NAME_PROLOGUE,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_TITLE + " = ? AND " +
                PoemContract.PoemTable.COLUMN_NAME_SUBTITLE + " = ? AND " +
                PoemContract.PoemTable.COLUMN_NAME_AUTHOR + " = ?";

        // Where clause arguments
        String[] selectionArgs = {title, subtitle, author};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PoemContract.PoemTable.COLUMN_NAME_SUBTITLE + " ASC, " +
                PoemContract.PoemTable.COLUMN_NAME_SN + " ASC";

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        String period = "";
        String prologue = "";
        List<String> content = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                period = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_PERIOD, period);
                prologue = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_PROLOGUE, prologue);

                byte[] blobSentence = cursor.getBlob(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                String s = blobToString(blobSentence);
                if (!s.isEmpty()) {
                    content.add(s);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return new Poem(title, subtitle, author, period, prologue, content);
    }
    public Observable<Poem> getPoemObservable(String title, String subtitle, String author) {
        return Observable.fromCallable(new Callable<Poem>() {
            @Override
            public Poem call() throws Exception {
                return getPoem(title, subtitle, author);
            }
        });
    }

    // get poem from poem id
    protected Poem getPoem(int poemId) {
        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_POEM_ID + " = ?";

        // Where clause arguments
        String[] selectionArgs = {String.valueOf(poemId)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PoemContract.PoemTable.COLUMN_NAME_SN + " ASC";

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        String title = "";
        String subtitle = "";
        String author = "";
        String period = "";
        String prologue = "";
        List<String> content = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                title = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_TITLE, title);
                subtitle = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_SUBTITLE, subtitle);
                author = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_AUTHOR, author);
                period = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_PERIOD, period);
                prologue = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_PROLOGUE, prologue);

                byte[] blobSentence = cursor.getBlob(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                String s = blobToString(blobSentence);
                if (!s.isEmpty()) {
                    content.add(s);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        // no valid poem found
        if (title.isEmpty() || author.isEmpty() || content.isEmpty()) {
            return null;
        }

        return new Poem(title, subtitle, author, period, prologue, content);
    }

    protected List<Integer> getPoemIds(String sentence) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_SENTENCE + " like ?";

        // Where clause arguments
        String[] selectionArgs = {sentence + "%"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Integer> poemIds = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                if (!poemIds.contains(poemId)) {
                    poemIds.add(poemId);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return poemIds;
    }

    public List<Poem> getPoems(String sentence) {
        List<Integer> poemIds = getPoemIds(sentence);
        List<Poem> poems = new ArrayList<>();
        for (int i : poemIds) {
            Poem poem = getPoem(i);
            if (poem != null) {
                poems.add(poem);
            }
        }
        return poems;
    }
    public Observable<List<Poem>> getPoemsObservable(String sentence) {
        return Observable.fromCallable(new Callable<List<Poem>>() {
            @Override
            public List<Poem> call() throws Exception {
                return getPoems(sentence);
            }
        });
    }

    protected List<Integer> getPoemIds(List<String> keywords) {
        List<Integer> poemIds = new ArrayList<>();
        if (keywords == null || keywords.size() == 0) {
            return poemIds;
        }

        // sql clause
        String sql = String.format("select %s from (select %s, %s, %s, group_concat(%s) as content from %s group by poemid)",
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_TITLE,
                PoemContract.PoemTable.COLUMN_NAME_AUTHOR,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE,
                PoemContract.PoemTable.TABLE_NAME,
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID);

        // selection and arguments
        StringBuilder selection = new StringBuilder();
        String[] selectionArgs = new String[3 * keywords.size()];
        int i = 0;
        for (String kw : keywords) {
            if (selection.length() > 0) {
                selection.append(" and ");
            }
            selection.append("(");
            selection.append(PoemContract.PoemTable.COLUMN_NAME_TITLE + " like ?");
            selection.append(" or ");
            selection.append(PoemContract.PoemTable.COLUMN_NAME_AUTHOR + " like ?");
            selection.append(" or ");
            selection.append("content like ?");
            selection.append(")");

            selectionArgs[i++] = "%" + kw + "%";
            selectionArgs[i++] = "%" + kw + "%";
            selectionArgs[i++] = "%" + kw + "%";
        }

        //final sql clause
        if (selection.length() > 0) {
            sql += " where " + selection.toString();
        }

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // parse
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                if (!poemIds.contains(poemId)) {
                    poemIds.add(poemId);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return poemIds;
    }

    public List<Poem> getPoems(List<String> keywords) {
        List<Integer> poemIds = getPoemIds(keywords);
        List<Poem> poems = new ArrayList<>();
        for (int i : poemIds) {
            Poem poem = getPoem(i);
            if (poem != null) {
                poems.add(poem);
            }
        }
        return poems;
    }

    public List<ChineseFragment> getChineseFragments(int level) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_LEVEL + " = ?";

        // Where clause arguments
        String[] selectionArgs = {""+level};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<ChineseFragment> cfList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                byte[] blobSentence = cursor.getBlob(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                String s = blobToString(blobSentence);
                if (!s.isEmpty()) {
                    Pair<String, String> pair = Poem.splitWordsAndPunctuation(s);
                    cfList.add(new ChineseFragment(pair.first));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return cfList;
    }
}
