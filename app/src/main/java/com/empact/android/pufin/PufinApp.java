package com.empact.android.pufin;

import android.app.Application;

import io.tpa.tpalib.CrashHandling;
import io.tpa.tpalib.TPA;
import io.tpa.tpalib.TpaConfiguration;
import io.tpa.tpalib.feedback.FeedbackInvocation;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class PufinApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());  //for logs - make log in any part like : Timber.d("Hello world")
        } else {  // production app
            if (BuildConfig.TPA_ENABLED) {
                TpaConfiguration config = new TpaConfiguration.Builder(BuildConfig.tpa_uuid, "https://bridgeit.tpa.io/")
                        .setCrashHandling(CrashHandling.ALWAYS_SEND)
                        .setFeedbackInvocation(FeedbackInvocation.EVENT_SHAKE)
                        .build();

                TPA.initialize(this, config);
            }
        }


    }

}