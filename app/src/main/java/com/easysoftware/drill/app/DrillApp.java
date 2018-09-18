package com.easysoftware.drill.app;

import android.app.Application;

import com.easysoftware.drill.di.ActivityComponent;
import com.easysoftware.drill.di.AppComponent;
import com.easysoftware.drill.di.ApplicationModule;
import com.easysoftware.drill.di.DaggerAppComponent;

public class DrillApp extends Application {

    private AppComponent mAppComponent;
    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        createAppComponent();
    }

    private void createAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public ActivityComponent createActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = mAppComponent.addSubComponent();
        }
        return mActivityComponent;
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    public void releaseActivityComponent() {
        mActivityComponent = null;
    }
}
