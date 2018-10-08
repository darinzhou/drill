package com.easysoftware.drill;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.easysoftware.drill.data.model.Poem;
import com.easysoftware.drill.data.database.PoemDbHelper;

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
    public void test_db() {
        PoemDbHelper dbHelper = PoemDbHelper.getInstance(
                InstrumentationRegistry.getTargetContext(), true);
        List<Poem> poems1 = dbHelper.getPoems("好雨知时节");

        List<String> keywords = new ArrayList<>();
        keywords.add("王维");
        keywords.add("明月");
//        keywords.add("故乡");
        List<Poem> poems2 = dbHelper.getPoems(keywords);
    }

}
