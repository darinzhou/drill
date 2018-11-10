package com.easysoftware.drill;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.model.CFItem;
import com.easysoftware.drill.data.model.Idiom;
import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.data.database.PoemDbHelper;
import com.easysoftware.drill.data.model.Verse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.easysoftware.drill", appContext.getPackageName());
    }

    @Test
    public void test_poem_db() {
        PoemDbHelper dbHelper = PoemDbHelper.getInstance(
                InstrumentationRegistry.getTargetContext(), true);
        List<Poem> poems1 = dbHelper.getPoemsContainSentence("好雨知时节");

        List<String> keywords = new ArrayList<>();
        keywords.add("王维");
        keywords.add("明月");
//        keywords.add("故乡");
        List<Poem> poems2 = dbHelper.getPoemsContainKeywords(keywords);

        List<Verse> verses1 = dbHelper.getVersesStartwith("明月");
        List<Verse> verses2 = dbHelper.getVersesEndwith("明月");
        List<Verse> verses3 = dbHelper.getVersesContainKeyword("明月");

        int i = 0;
    }

    @Test
    public void test_idiom_db() {
        IdiomDbHelper dbHelper = IdiomDbHelper.getInstance(
                InstrumentationRegistry.getTargetContext(), true);
        List<CFItem> idioms = dbHelper.getCFItemsContainKeyword("知己");
        List<String> keywords = new ArrayList<>();
        keywords.add("知己");
        keywords.add("知彼");
        List<CFItem> idms = dbHelper.getCFItemsContainKeywords(keywords);

        List<Idiom> idioms1 = dbHelper.getIdiomsStartwith("明");
        List<Idiom> idioms2 = dbHelper.getIdiomsEndwith("明");
        List<Idiom> idioms3 = dbHelper.getIdiomsContainKeyword("明");

        int i = 0;
    }

}
