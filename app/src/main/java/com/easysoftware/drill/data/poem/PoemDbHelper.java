package com.easysoftware.drill.data.poem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class PoemDbHelper extends SQLiteOpenHelper {

    public PoemDbHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getDbPath(Context context, String dbName) {
        return context.getDatabasePath(dbName).getAbsolutePath();
    }

    public static boolean dbExists(Context context, String dbName) {
        return new File(getDbPath(context, dbName)).exists();
    }

    public static void deleteDb(Context context, String dbName) {
        File file = new File(getDbPath(context, dbName));
        if (file.exists()) {
            file.delete();
        }
    }

    public static void copyDb(Context context, String dbName) throws IOException {
        String outFileName = getDbPath(context, dbName);

        try (
            OutputStream myOutput = new FileOutputStream(outFileName);
            InputStream myInput = context.getAssets().open(dbName);
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
        }
    }

    public String getPoem(String title, String subTitle, String author) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PoemContract.PoemSentence.COLUMN_NAME_SN,
                PoemContract.PoemSentence.COLUMN_NAME_SENTENCE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PoemContract.PoemSentence.COLUMN_NAME_TITLE + " = ? AND " +
                PoemContract.PoemSentence.COLUMN_NAME_SUBTITLE + " = ? AND " +
                PoemContract.PoemSentence.COLUMN_NAME_AUTHOR + " = ?";

        // Where clause arguments
        String[] selectionArgs = {title, subTitle, author};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PoemContract.PoemSentence.COLUMN_NAME_SUBTITLE + " ASC";

        // search
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PoemContract.PoemSentence.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        String content = "";
        while(cursor.moveToNext()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(PoemContract.PoemSentence.COLUMN_NAME_SENTENCE));
            if (content.length() > 0) {
                content += "\n";
            }
            try {
                String s = new String(blob, "UTF-8");
                if (s.charAt(s.length()-1) == 0) {
                    s = s.substring(0, s.length()-1);
                }
                content += s;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return content;
    }

    public Observable<String> getPoemObservable(String title, String subTitle, String author) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getPoem(title, subTitle, author);
            }
        });
    }
}
