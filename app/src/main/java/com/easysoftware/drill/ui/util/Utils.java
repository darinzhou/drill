package com.easysoftware.drill.ui.util;

import android.util.Pair;

public class Utils {
    public static final char[] PUNCTUATIONS = {',', ';', '?', '!', '.', '，', '；', '？', '！', '。', ':', '：', '、'};
    public static Pair<String, String> splitTextAndEndingPunctuation(String s) {
        int len = s.length();
        char endChar = s.charAt(len-1);
        String punctuation = "";
        for (char p : PUNCTUATIONS) {
            if (p == endChar) {
                s = s.substring(0, len-1);
                punctuation = "" + p;
                break;
            }
        }
        return new Pair<>(s, punctuation);
    }
}
