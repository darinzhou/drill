package com.easysoftware.drill.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, int version) {
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

    protected String blobToString(byte[] blob) {
        String s = "";
        try {
            s = new String(blob, "UTF-8");
            if (s.charAt(s.length()-1) == 0) {
                s = s.substring(0, s.length()-1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

}
