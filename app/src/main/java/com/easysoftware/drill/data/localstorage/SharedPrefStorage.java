package com.easysoftware.drill.data.localstorage;

import android.content.Context;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class SharedPrefStorage implements LocalStorage {
    public static final String SHAREDPREF_NAME = "local_storage";
    private Context mContext;

    public SharedPrefStorage(Context context) {
        mContext = context;
    }

    @Override
    public void write(String key, String message) {
        mContext.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(key, message).apply();
    }

    @Override
    public Observable<String> read(String key) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return mContext.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
                        .getString(key, "");
            }
        });
    }
}
