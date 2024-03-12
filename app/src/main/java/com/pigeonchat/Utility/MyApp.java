package com.pigeonchat.Utility;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        // Applies Material theme to app
        DynamicColors.applyToActivitiesIfAvailable(this);

        EmojiManager.install(new GoogleEmojiProvider());
    }
}
