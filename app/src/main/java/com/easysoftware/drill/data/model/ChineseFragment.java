package com.easysoftware.drill.data.model;

import com.easysoftware.drill.data.cflib.CFLibraryLoader;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import io.reactivex.Observable;

public class ChineseFragment {
    private final String mText;
    private final int mLength;

    public ChineseFragment(@Nonnull String text) {
        mText = text;
        mLength = mText.length();
    }

    public ChineseFragment(@Nonnull ChineseFragment cf) {
        mText = cf.toString();
        mLength = mText.length();
    }

    @Override
    public String toString() {
        return mText;
    }

    public char getFirstChar() {
        return charAt(0);
    }

    public char getLastChar() {
        return charAt(mLength-1);
    }

    public int length() {
        return mLength;
    }

    public char charAt(int index) {
        return mText.charAt(index);
    }

    public boolean contains(char c) {
        return mText.contains(Character.toString(c));
    }

    public boolean contains(String s) {
        return mText.contains(s);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof ChineseFragment) {
            ChineseFragment cf = (ChineseFragment) o;
            return toString().equals(cf.toString());
        } else if (o instanceof String) {
            String s = (String) o;
            return toString().equals(s);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mText.hashCode();
        return result;
    }

    public static Observable<List<ChineseFragment>> loadLibraryObservable(final CFLibraryLoader loader) {
        return Observable.fromCallable(new Callable<List<ChineseFragment>>() {
            @Override
            public List<ChineseFragment> call() throws Exception {
                return loader.load();
            }
        });
    }

}
