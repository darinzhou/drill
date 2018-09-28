package com.easysoftware.drill.data.localstorage;

import io.reactivex.Observable;

public interface LocalStorage {
    void write(String key, String message);
    int read(String key, int defaultValue);
    Observable<Integer> readObservable(String key, int defaultValue);
    String read(String key, String defaultValue);
    Observable<String> readObservable(String key, String defaultValue);
}
