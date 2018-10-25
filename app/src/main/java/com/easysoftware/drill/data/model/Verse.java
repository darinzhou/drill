package com.easysoftware.drill.data.model;

import android.util.Pair;

import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Verse implements CFItem {
    public static final String VERSE_FORMAT = "\n  【诗句】%s\n  【出处】";

    private String mText;
    private Poem mPoem;
    private int mPosition;
    private String mPrevious;
    private String mNext;
    private boolean mIsEnd;

    public Verse(String text, Poem poem) {
        mText = text;
        mPoem = poem;
        mPosition = -1;
        build();
    }

    private boolean build() {
        int scount = mPoem.getSentences().size();
        for (int i = 0; i < scount; ++i) {
            String s = mPoem.getSentences().get(i);
            Pair<String, String> pair = Utils.splitTextAndEndingPunctuation(s);
            if (mText.equals(pair.first)) {
                mPosition = i;
                if (i > 0) {
                    Pair<String, String> p = Utils.splitTextAndEndingPunctuation(mPoem.getSentences().get(i - 1));
                    mPrevious = p.first;
                }
                if (i < scount - 1) {
                    Pair<String, String> p = Utils.splitTextAndEndingPunctuation(mPoem.getSentences().get(i + 1));
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

    @Override
    public String getText() {
        return mText;
    }

    @Override
    public List<String> getFormattedTexts() {
        return Verse.getFormattedTexts(this);
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
            if (v.getText().equals(sentence)) {
                return v.getPoem();
            }
        }
        return null;
    }

    public static List<String> getFormattedTexts(Verse verse) {
        List<String> texts = new ArrayList<>();
        texts.add(String.format(VERSE_FORMAT, verse.getNext()));
        Poem poem = verse.getPoem();
        texts.add(poem.getTitleString());
        texts.add(poem.getAuthorString());
        texts.add(poem.getPrologue());
        texts.add(poem.getContent());

        return texts;
    }

}
