package com.easysoftware.drill.di;

import android.app.Application;
import android.content.Context;

import com.easysoftware.drill.data.database.IdiomDbHelper;
import com.easysoftware.drill.data.localstorage.LocalStorage;
import com.easysoftware.drill.data.localstorage.SharedPrefStorage;
import com.easysoftware.drill.data.database.PoemDbHelper;

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

//    @Provides
//    @Singleton
//    PoemDbHelper providePoemDbHelper(Context context, LocalStorage localStorage,
//                                          @Named("forceToOverwriteDb") boolean forceToOverwrite) {
//        return PoemDbHelperFactory.getPoemDbHelper(context, localStorage, forceToOverwrite);
//    }

    @Provides
    @Singleton
    PoemDbHelper providePoemDbHelper(Context context,
                                     @Named("forceToOverwriteDb") boolean forceToOverwrite) {
        return PoemDbHelper.getInstance(context, forceToOverwrite);
    }

    @Provides
    @Singleton
    IdiomDbHelper provideIdiomDbHelper(Context context,
                                       @Named("forceToOverwriteDb") boolean forceToOverwrite) {
        return IdiomDbHelper.getInstance(context, forceToOverwrite);
    }
}
