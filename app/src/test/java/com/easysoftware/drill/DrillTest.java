package com.easysoftware.drill;

import com.easysoftware.drill.data.cflib.CFLibraryLoader;
import com.easysoftware.drill.data.cflib.asset.CFLibAssetUtil;
import com.easysoftware.drill.data.cflib.asset.IdiomLibAssetLoader;
import com.easysoftware.drill.data.cflib.asset.PoemLibAssetLoader;
import com.easysoftware.drill.data.model.CFRecognitionItem;
import com.easysoftware.drill.data.model.ChineseFragment;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static junit.framework.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DrillTest {
    private static final String  ASSET_BASE_PATH = "../app/src/main/assets/";
    private static final String IDIOM_FILE_NAME = "idioms.txt";
    private static final String POEM_FILE_NAME = "poem.txt";

    private static final String  IDIOM_FILE_PATH = ASSET_BASE_PATH + IDIOM_FILE_NAME;
    private static final int  IDIOM_COUNT = 8519;
    private static final String  FIRST_IDIOM = "坚定不移";
    private static final String  LAST_IDIOM = "见机而作";

    private static final String  POEM_FILE_PATH = ASSET_BASE_PATH + POEM_FILE_NAME;
    private static final int  POEM_COUNT = 13703;
    private static final String  FIRST_POEM = "更上一层楼";
    private static final String  LAST_POEM = "龙门翠黛眉相对";


    @Test
    public void test_rx_operators() {
        Observable<Integer> observable = Observable.range(1, 5);

        Observable<String> observableFin = observable.switchMap(
                (Function<Integer, ObservableSource<String>>) (Integer itemFromSource) ->
                        Observable.just("first one "+itemFromSource,"second one "+itemFromSource, "last one "+itemFromSource)
                                .delay(10, TimeUnit.NANOSECONDS));
        observableFin.subscribe(s -> System.out.println(""+s));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private void testChineseFragmentLoadLibrary(CFLibraryLoader cfLoader, int cfCount, String cfFirst, String cfLast) throws FileNotFoundException {
        List<ChineseFragment> cfList = ChineseFragment.loadLibraryObservable(cfLoader)
                .test()
                .assertComplete()
                .values()
                .get(0);
        assertTrue(cfList.size() == cfCount);
        assertTrue(cfList.get(0).equals(cfFirst));
        assertTrue(cfList.get(cfList.size()-1).equals(cfLast));
    }

    @Test
    public void test_load_idioms() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(IDIOM_FILE_PATH);
        IdiomLibAssetLoader cfLoader = new IdiomLibAssetLoader(is);
        testChineseFragmentLoadLibrary(cfLoader, IDIOM_COUNT, FIRST_IDIOM, LAST_IDIOM);
    }

    @Test
    public void test_load_poem() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(POEM_FILE_PATH);
        PoemLibAssetLoader cfLoader = new PoemLibAssetLoader(is);
        testChineseFragmentLoadLibrary(cfLoader, POEM_COUNT, FIRST_POEM, LAST_POEM);
    }

    private void testCFRecognition(CFRecognitionItem cfri, final int cfriLength) {
        String sCF = cfri.getChineseFragment().toString();
        System.out.println("ChineseFragment: " + sCF);

        String sConf = cfri.getConfusion().toString();
        System.out.println("Confusion: " + sConf);

        String s = cfri.getObfuscation();
        assertTrue(s.length() == cfriLength);
        int ci = 0;
        System.out.println("Obfuscation: ");
        System.out.println(s.substring(0,3));
        System.out.println(s.substring(3,6));
        System.out.println(s.substring(6,9));
        if (cfriLength > 9 ) {
            System.out.println(s.substring(9, 12));
        }
        for (int i=0; i<sCF.length(); ++i) {
            assertTrue(s.contains(sCF.substring(i,i+1)));
        }

        int tc = 0;
        for (int i=0; i<sConf.length(); ++i) {
            if (s.contains(sConf.substring(i,i+1))) {
                tc++;
            }
        }
        assertTrue(sConf.length() > tc);
    }

    @Test
    public void test_poem7_recognition_raw() throws IOException {
        FileInputStream is = new FileInputStream(POEM_FILE_PATH);
        List<ChineseFragment> cfList = CFLibAssetUtil.load(is);

        String cf = "升堂坐阶新雨足";
        String conf = "更闻桑田变成海";
        int cfriLength = 12;
        CFRecognitionItem cfri = new CFRecognitionItem.Builder(cfList, cf.length(), cfriLength)
                .build(new ChineseFragment(cf), new ChineseFragment(conf));
        testCFRecognition(cfri, cfriLength);
    }

    private void testCFRecognition(CFLibraryLoader cfLoader, final int cfLength, final int cfriLength) throws FileNotFoundException {
        List<CFRecognitionItem> cfriList = ChineseFragment.loadLibraryObservable(cfLoader)
                .switchMap(cfList -> new CFRecognitionItem.Builder(cfList, cfLength, cfriLength).buildObservable())
                .test()
                .assertComplete()
                .values();

        assertTrue(cfriList.size() == 1);
        CFRecognitionItem cfri = cfriList.get(0);
        testCFRecognition(cfri, cfriLength);
    }

    @Test
    public void test_idiom_recognition() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(IDIOM_FILE_PATH);
        IdiomLibAssetLoader cfLoader = new IdiomLibAssetLoader(is);
        testCFRecognition(cfLoader, 4, 9);
    }

    @Test
    public void test_poem5_recognition() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(POEM_FILE_PATH);
        PoemLibAssetLoader cfLoader = new PoemLibAssetLoader(is);
        testCFRecognition(cfLoader, 5, 9);
    }

    @Test
    public void test_poem7_recognition() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(POEM_FILE_PATH);
        PoemLibAssetLoader cfLoader = new PoemLibAssetLoader(is);
        testCFRecognition(cfLoader, 7, 12);
    }

}