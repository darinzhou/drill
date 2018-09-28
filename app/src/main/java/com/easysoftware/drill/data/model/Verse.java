package com.easysoftware.drill.data.model;

public class Verse extends ChineseFragment {
    private String mPoemTitle;
    private String mPoemAuthor;
    private int mPoemSentenceNo;

    public Verse(String text, String poemTitle, String poemAuthor, int poemSentenceNo) {
        super(text);
        mPoemTitle = poemTitle;
        mPoemAuthor = poemAuthor;
        mPoemSentenceNo = poemSentenceNo;
    }

    public void setPoemTitle(String poemTitle) {
        mPoemTitle = poemTitle;
    }

    public void setPoemAuthor(String poemAuthor) {
        mPoemAuthor = poemAuthor;
    }

    public void setPoemSentenceNo(int poemSentenceNo) {
        mPoemSentenceNo = poemSentenceNo;
    }

    public String getPoemTitle() {
        return mPoemTitle;
    }

    public String getPoemAuthor() {
        return mPoemAuthor;
    }

    public int getPoemSentenceNo() {
        return mPoemSentenceNo;
    }
}
