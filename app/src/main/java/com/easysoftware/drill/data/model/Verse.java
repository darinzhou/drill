package com.easysoftware.drill.data.model;

import android.util.Pair;

import java.util.List;

public class Verse {
    private String mContent;
    private Poem mPoem;
    private int mPosition;
    private String mPrevious;
    private String mNext;
    private boolean mIsEnd;

    public Verse(String content, Poem poem) {
        mContent = content;
        mPoem = poem;
        mPosition = -1;
        build();
    }

    private boolean build() {
        int scount = mPoem.getSentences().size();
        for (int i = 0; i < scount; ++i) {
            String s = mPoem.getSentences().get(i);
            Pair<String, String> pair = Poem.splitWordsAndPunctuation(s);
            if (mContent.equals(pair.first)) {
                mPosition = i;
                if (i > 0) {
                    Pair<String, String> p = Poem.splitWordsAndPunctuation(mPoem.getSentences().get(i - 1));
                    mPrevious = p.first;
                }
                if (i < scount - 1) {
                    Pair<String, String> p = Poem.splitWordsAndPunctuation(mPoem.getSentences().get(i + 1));
                    mNext = p.first;
                }

                if ("。".equals(pair.second) || "！".equals(pair.second)) {
                    mIsEnd = true;
                }

                return true;
            }
        }

        return false;
    }

    public String getContent() {
        return mContent;
    }

    public Poem getPoem() {
        return mPoem;
    }

    public boolean isValid() {
        return getPosition() != -1;
    }
    public int getPosition() {
        return mPosition;
    }

    public String getPrevious() {
        return mPrevious;
    }

    public String getNext() {
        return mNext;
    }

    public boolean isEnd() {
        return mIsEnd;
    }

    public static Poem findPoemWithSentence(String sentence, List<Verse> verseList) {
        for (Verse v : verseList) {
            if (v.getContent().equals(sentence)) {
                return v.getPoem();
            }
        }
        return null;
    }
}
