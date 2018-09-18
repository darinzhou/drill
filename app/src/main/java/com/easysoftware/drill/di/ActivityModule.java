package com.easysoftware.drill.di;

import android.app.Application;
import android.content.Context;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.cflib.asset.IdiomLibAssetLoader;
import com.easysoftware.drill.data.cflib.asset.PoemLibAssetLoader;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.localstorage.SharedPrefStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @PerActivity
    IdiomLibLoader provideIdiomLibLoader(Context context) {
        return new IdiomLibAssetLoader(context);
    }

    @Provides
    @PerActivity
    PoemLibLoader providePoemLibLoader(Context context) {
        return new PoemLibAssetLoader(context);
    }
}
