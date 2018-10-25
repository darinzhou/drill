package com.easysoftware.drill.data.model;

import java.util.ArrayList;
import java.util.List;

public class Idiom implements CFItem {
    public static final String IDIOM_FORMAT = "【解释】\n%s\n\n【出处】\n%s\n\n【例子】\n%s";

    private String mText;
    private String mPinyin;
    private String mExplanation;
    private String mDerivation;
    private String mExample;

    public Idiom(String text, String pinyin, String explanation, String derivation, String example) {
        mText = text;
        mPinyin = pinyin;
        mExplanation = explanation;
        mDerivation = derivation;
        mExample = example;
    }

    @Override
    public String getText() {
        return mText;
    }

    @Override
    public List<String> getFormattedTexts() {
        return Idiom.getFormattedTexts(this);
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

    public static List<String> getFormattedTexts(Idiom idiom) {
        List<String> texts = new ArrayList<>();
        texts.add(idiom.getText());
        texts.add(idiom.getPinyin());

        String other = String.format(IDIOM_FORMAT, idiom.getExplanation(),
                idiom.getDerivation(), idiom.getExample());
        texts.add(other);

        return texts;
    }

}
