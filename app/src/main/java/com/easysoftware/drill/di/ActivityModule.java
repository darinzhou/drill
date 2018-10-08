package com.easysoftware.drill.di;

import android.content.Context;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.cflib.asset.IdiomLibAssetLoader;
import com.easysoftware.drill.data.cflib.database.IdiomLibDbLoader;
import com.easysoftware.drill.data.cflib.database.PoemLibDbLoader;
import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.database.PoemDbHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

//    @Provides
//    @PerActivity
//    IdiomLibLoader provideIdiomLibLoader(Context context) {
//        return new IdiomLibAssetLoader(context);
//    }

//    @Provides
//    @PerActivity
//    PoemLibLoader providePoemLibLoader(Context context, LocalStorage sharedPrefStorage) {
//        return new PoemLibAssetLoader(context, sharedPrefStorage);
//    }

    @Provides
    @PerActivity
    IdiomLibLoader provideIdiomLibLoader(IdiomDbHelper idiomDbHelper) {
        return new IdiomLibDbLoader(idiomDbHelper);
    }

    @Provides
    @PerActivity
    PoemLibLoader providePoemLibLoader(PoemDbHelper poemDbHelper, LocalStorage sharedPrefStorage) {
        return new PoemLibDbLoader(poemDbHelper, sharedPrefStorage);
    }
}
