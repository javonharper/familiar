package com.javonharper.familiar;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FamiliarApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/MuseoSansRounded-700.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
