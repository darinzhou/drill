package com.easysoftware.drill.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.ChineseFragment;
import com.easysoftware.drill.data.model.Verse;
import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class PoemDbHelper extends DbHelper implements CFItemDbHelper {
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

    private String getString(Cursor cursor, String columName) {
        byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(columName));
        return blobToString(blob);
    }

    private String getString(Cursor cursor, String columName, String s) {
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
    private Poem getPoem(int poemId) {
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
        if (title.isEmpty() || content.isEmpty()) {
            return null;
        }

        return new Poem(title, subtitle, author, period, prologue, content);
    }

    private List<Integer> getPoemIdsContainSentence(String sentence) {
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
    public List<Poem> getPoemsContainSentence(String sentence) {
        List<Integer> poemIds = getPoemIdsContainSentence(sentence);
        List<Poem> poems = new ArrayList<>();
        for (int i : poemIds) {
            Poem poem = getPoem(i);
            if (poem != null) {
                poems.add(poem);
            }
        }
        return poems;
    }
    public Observable<List<Poem>> getPoemsContainSentenceObservable(String sentence) {
        return Observable.fromCallable(new Callable<List<Poem>>() {
            @Override
            public List<Poem> call() throws Exception {
                return getPoemsContainSentence(sentence);
            }
        });
    }

    private List<Integer> getPoemIds(List<String> keywords) {
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
                PoemContract.PoemTable.TABLE_NAME);

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
    public List<Poem> getPoemsContainKeywords(List<String> keywords) {
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
    public Observable<List<Poem>> getPoemsContainKeywordsObservable(List<String> keywords) {
        return Observable.fromCallable(new Callable<List<Poem>>() {
            @Override
            public List<Poem> call() throws Exception {
                return getPoemsContainKeywords(keywords);
            }
        });
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
                    Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(s);
                    cfList.add(new ChineseFragment(pair.first));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return cfList;
    }

    public Verse getVerse(String verseText) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_SENTENCE + " like ?";

        // Where clause arguments
        String[] selectionArgs = {verseText + "%"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        Verse verse = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(
                        getString(cursor, PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                if (pair.first.equals(verseText)) {
                    verse = new Verse(pair.first, getPoem(poemId));
                    break;
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return verse;
    }
    public Observable<Verse> getVerseObservable(String verseText) {
        return Observable.fromCallable(new Callable<Verse>() {
            @Override
            public Verse call() throws Exception {
                return getVerse(verseText);
            }
        });
    }

    public List<Verse> getVersesStartwith(String start) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_SENTENCE + " like ?";

        // Where clause arguments
        String[] selectionArgs = {start + "%"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Verse> verses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                String sentence = getString(cursor, PoemContract.PoemTable.COLUMN_NAME_SENTENCE);
                Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(sentence);

                Poem poem = getPoem(poemId);
                verses.add(new Verse(pair.first, poem));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return verses;
    }
    public Observable<List<Verse>> getVersesStartwithObservable(String start) {
        return Observable.fromCallable(new Callable<List<Verse>>() {
            @Override
            public List<Verse> call() throws Exception {
                return getVersesStartwith(start);
            }
        });
    }

    public List<Verse> getVersesEndwith(String end) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_SENTENCE + " like ?";

        // Where clause arguments
        String[] selectionArgs = {"%" + end + "_"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Verse> verses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(
                        getString(cursor, PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                verses.add(new Verse(pair.first, getPoem(poemId)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return verses;
    }
    public Observable<List<Verse>> getVersesEndwithObservable(String end) {
        return Observable.fromCallable(new Callable<List<Verse>>() {
            @Override
            public List<Verse> call() throws Exception {
                return getVersesEndwith(end);
            }
        });
    }

    public List<Verse> getVersesContainKeyword(String keyword) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemTable.COLUMN_NAME_POEM_ID,
                PoemContract.PoemTable.COLUMN_NAME_SENTENCE
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemTable.COLUMN_NAME_SENTENCE + " like ?";

        // Where clause arguments
        String[] selectionArgs = {"%" + keyword + "%"};

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Verse> verses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int poemId = cursor.getInt(cursor.getColumnIndexOrThrow(PoemContract.PoemTable.COLUMN_NAME_POEM_ID));
                Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(
                        getString(cursor, PoemContract.PoemTable.COLUMN_NAME_SENTENCE));
                verses.add(new Verse(pair.first, getPoem(poemId)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return verses;
    }
    public Observable<List<Verse>> getVersesContainKeywordObservable(String keyword) {
        return Observable.fromCallable(new Callable<List<Verse>>() {
            @Override
            public List<Verse> call() throws Exception {
                return getVersesContainKeyword(keyword);
            }
        });
    }

    @Override
    public CFItem getCFItem(String content) {
        return getVerse(content);
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
        return new ArrayList<>(getVersesStartwith(start));
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
        return new ArrayList<>(getVersesEndwith(end));
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
        return new ArrayList<>(getVersesContainKeyword(keyword));
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
        List<Poem> poems = getPoemsContainKeywords(keywords);
        List<Verse> verses = new ArrayList<>();
        for (Poem p : poems) {
            String text = "";
            for (String s : p.getSentences()) {
                for (String kw : keywords) {
                    if (s.contains(kw)) {
                        text = s;
                        break;
                    }
                }
                if (!text.isEmpty()) {
                    break;
                }
            }
            verses.add(new Verse(text, p));
        }
        return new ArrayList<>(verses);
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
