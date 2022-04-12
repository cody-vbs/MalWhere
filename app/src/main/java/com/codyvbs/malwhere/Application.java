package com.codyvbs.malwhere;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CaocConfig.Builder.create()
                .trackActivities(true)
                .showErrorDetails(true)
                .apply();
    }

    public static boolean activityVisible; // Variable that will check the
    // current activity state

    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }
}
