package com.easysoftware.drill.data.model;

import android.text.TextUtils;
import android.util.Pair;

import java.util.List;

import static com.easysoftware.drill.ui.util.Utils.splitTextAndEndingPunctuation;

public class Poem {

    private String mTitle;
    private String mSubtitle;
    private String mAuthor;
    private String mPrologue;
    private String mPeriod;
    private List<String> mSentences;

    public Poem(String title, String subtitle, String author, String period, String prologue, List<String> sentences) {
        mTitle = title;
        mAuthor = author;
        mSubtitle = subtitle;
        mPeriod = period;
        mPrologue = prologue;
        mSentences = sentences;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPrologue() {
        return mPrologue;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public List<String> getSentences() {
        return mSentences;
    }

    public int sentencePosition(String sentence) {
        for (int i = 0; i < mSentences.size(); ++i) {
            String s = mSentences.get(i);
            if (s.equals(sentence)) {
                return i;
            }

            Pair<String, String> pair = splitTextAndEndingPunctuation(s);
            if (pair.first.equals(sentence)) {
                return i;
            }
        }
        return -1;
    }

    public String getContent() {
        StringBuilder content = new StringBuilder();
        for (String s : mSentences) {
            content.append(s);

            // replace ascii punctuations with unicode ones
            int len = content.length();
            char p = content.charAt(len - 1);
            switch (p) {
                case ',':
                    p = '，';
                    content.setCharAt(len - 1, p);
                    break;
                case '?':
                    p = '？';
                    content.setCharAt(len - 1, p);
                    break;
                case '!':
                    p = '！';
                    content.setCharAt(len - 1, p);
                    break;
                case ':':
                    p = '：';
                    content.setCharAt(len - 1, p);
                    break;
            }

            content.append("\n");
        }
        if (content.length() > 0) {
            content.deleteCharAt(content.length() - 1);
        }

        return content.toString();
    }

    public String getTitleString() {
        if (TextUtils.isEmpty(mSubtitle)) {
            return mTitle;
        }
        return mTitle + "  " + mSubtitle;
    }

    public String getAuthorString() {
        if (TextUtils.isEmpty(mPeriod)) {
            return mAuthor;
        }
        return "（" + mPeriod + "）" + mAuthor;
    }
}
