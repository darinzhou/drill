package com.easysoftware.drill.di;

import android.app.Application;
import android.content.Context;

import com.easysoftware.drill.data.cflib.IdiomLibLoader;
import com.easysoftware.drill.data.cflib.PoemLibLoader;
import com.easysoftware.drill.data.cflib.asset.IdiomLibAssetLoader;
import com.easysoftware.drill.data.cflib.asset.PoemLibAssetLoader;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.localstorage.SharedPrefStorage;
import com.easysoftware.drill.data.poem.PoemDbHelper;
import com.easysoftware.drill.data.poem.PoemDbHelperFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApp;

    public ApplicationModule(final Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    LocalStorage provideLocalStorage(Context context) {
        return new SharedPrefStorage(context);
    }

    @Provides
    @Singleton
    @Named("forceToOverwriteDb")
    boolean provideForceToOverwriteDb() {
        return true;
    }

    @Provides
    @Singleton
    PoemDbHelper providePoemDbHelper(Context context, LocalStorage localStorage,
                                          @Named("forceToOverwriteDb") boolean forceToOverwrite) {
        return PoemDbHelperFactory.getPoemDbHelper(context, localStorage, forceToOverwrite);
    }
}
