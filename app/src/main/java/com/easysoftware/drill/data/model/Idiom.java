package com.easysoftware.drill.data.model;

public class Idiom {
    private String mContent;
    private String mPinyin;
    private String mExplanation;
    private String mDerivation;
    private String mExample;

    public Idiom(String content, String pinyin, String explanation, String derivation, String example) {
        mContent = content;
        mPinyin = pinyin;
        mExplanation = explanation;
        mDerivation = derivation;
        mExample = example;
    }

    public String getContent() {
        return mContent;
    }

    public String getPinyin() {
        return mPinyin;
    }

    public String getExplanation() {
        return mExplanation;
    }

    public String getDerivation() {
        return mDerivation;
    }

    public String getExample() {
        return mExample;
    }
}
