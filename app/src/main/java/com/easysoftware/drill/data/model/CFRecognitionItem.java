package com.easysoftware.drill.data.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import io.reactivex.Observable;

public class CFRecognitionItem {

    private final ChineseFragment mChineseFragment;
    private final ChineseFragment mConfusion;
    private String mObfuscation;

    public CFRecognitionItem(final ChineseFragment cf, final ChineseFragment confusion, final String obfuscation) {
        mChineseFragment = cf;
        mConfusion = confusion;
        mObfuscation = obfuscation;
    }

    public ChineseFragment getChineseFragment() {
        return mChineseFragment;
    }

    public ChineseFragment getConfusion() {
        return mConfusion;
    }

    public String getObfuscation() {
        return mObfuscation;
    }

    public boolean checkResult(String result) {
        return mChineseFragment.equals(result);
    }

    //
    // builder class for CFRecognitionItem
    //

    public static class Builder {
        public static final int MIN_OBFUSCATION_LENGTH = 9;
        public static final int MAX_DUPLICATE_TIMES_TO_RESET_USED_SET = 16;

        private List<ChineseFragment> mCFList;
        private int mCFLength;
        private int mObfuscationLength;

        private HashSet<Integer> mUsed;

        public Builder(@Nonnull List<ChineseFragment> cfList, int cfLength, int obfuscationLength) {
            mCFList = cfList;
            mCFLength = cfLength;
            mObfuscationLength = obfuscationLength;
            mUsed = new HashSet<>();
        }

        private char removeOneCharFromConfusion(StringBuilder sb, final ChineseFragment chineseFragment) {
            Random rand = new Random();
            int idxToRemove = rand.nextInt(sb.length());
            char chToRemove = sb.charAt(idxToRemove);
            while (chineseFragment.contains(chToRemove)) {
                idxToRemove = rand.nextInt(sb.length());
                chToRemove = sb.charAt(idxToRemove);
            }
            sb.deleteCharAt(idxToRemove);
            return chToRemove;
        }

        public CFRecognitionItem build(@Nonnull final ChineseFragment chineseFragment,
                                       @Nonnull final ChineseFragment confusion) {
            if (mObfuscationLength < MIN_OBFUSCATION_LENGTH) {
                throw new IllegalArgumentException("Chinese fragment recognition item should be at least 9.");
            }
            if (mObfuscationLength <= chineseFragment.length()) {
                throw new IllegalArgumentException("Chinese fragment recognition item should be longer that Chinese fragment.");
            }
            if (chineseFragment.equals(confusion)) {
                throw new IllegalArgumentException("Chinese fragment and confusion should not be the same.");
            }
            if (chineseFragment.length() != confusion.length()) {
                throw new IllegalArgumentException("Chinese fragment and confusion should have the same length.");
            }

            // create random
            Random rand = new Random();

            // create obfuscation builder
            StringBuilder sb = new StringBuilder(confusion.toString());

            // chars should be removed from confusion
            List<Character> charsToRemove = new ArrayList<>();

            // at least remove 1 char from confusion
            char chToRemove = removeOneCharFromConfusion(sb, chineseFragment);
            charsToRemove.add(chToRemove);

            // remove more chars if cfri length doesn't match
            int ec = sb.length() - (mObfuscationLength - chineseFragment.length());
            for (int i=0; i<ec; ++i) {
                chToRemove = removeOneCharFromConfusion(sb, chineseFragment);
                charsToRemove.add(chToRemove);
            }

            // remove chars exist in both chineseFragment and confusion
            for (int i=0; i<chineseFragment.length(); ++i) {
                int idx = sb.indexOf(Character.toString(chineseFragment.toString().charAt(i)));
                if (idx != -1) {
                    sb.deleteCharAt(idx);
                }
            }

            // add target chineseFragment
            sb.append(chineseFragment.toString());

            // find extra chars
            int count = mObfuscationLength - sb.length();
            for (int i=0; i<count; ++i) {
                // find a random chineseFragment
                int r = rand.nextInt(mCFList.size());
                ChineseFragment cf = mCFList.get(r);
                while (chineseFragment.equals(cf) || confusion.equals(cf)) {
                    r = rand.nextInt(mCFList.size());
                    cf = mCFList.get(r);
                }

                // find a random char in the chineseFragment but not the char removed from confusion, and not in the result so far
                r = rand.nextInt(cf.length());
                char c = cf.charAt(r);
                while (sb.indexOf(Character.toString(c)) != -1 || c == chToRemove) {
                    r = rand.nextInt(cf.length());
                    c = cf.charAt(r);
                }

                // add
                sb.append(c);
            }

            // obfuscate
            StringBuilder ob = new StringBuilder();
            for (int i=0; i<mObfuscationLength; ++i) {
                int r = rand.nextInt(sb.length());
                ob.append(sb.charAt(r));
                sb.deleteCharAt(r);
            }

            return new CFRecognitionItem(chineseFragment, confusion, ob.toString());
        }

        public CFRecognitionItem build() {
            // create random
            Random rand = new Random();

            // find chineseFragment
            int duplicatedTimes = 0;
            int max = mCFList.size();
            Random random = new Random();
            int r1 = random.nextInt(max);
            ChineseFragment chineseFragment = mCFList.get(r1);
            while (chineseFragment.length() != mCFLength || mUsed.contains(r1)) {
                // if r1 was used, tehn count the duplicate times
                // to avoid infinite loop, we have to reset the count if deplicate too much
                if (chineseFragment.length() == mCFLength) {
                    duplicatedTimes++;
                    if (duplicatedTimes > MAX_DUPLICATE_TIMES_TO_RESET_USED_SET) {
                        mUsed.clear();
                        duplicatedTimes = 0;
                    }
                }
                r1 = random.nextInt(max);
                chineseFragment = mCFList.get(r1);
            }
            mUsed.add(r1);

            // find confusion
            int r2 = random.nextInt(max);
            while (r1 == r2) {
                r2 = random.nextInt(max);
            }
            ChineseFragment confusion = mCFList.get(r2);
            while (confusion.length() != mCFLength) {
                r2 = random.nextInt(max);
                confusion = mCFList.get(r2);
            }

            return build(chineseFragment, confusion);
        }

        public Observable<CFRecognitionItem> buildObservable() {
            return Observable.fromCallable(
                    new Callable<CFRecognitionItem>() {
                        @Override
                        public CFRecognitionItem call() throws Exception {
                            return build();
                        }
                    }
            );
        }

    }

}
