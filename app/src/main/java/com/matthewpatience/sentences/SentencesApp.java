package com.matthewpatience.sentences;

import android.app.Application;
import android.content.Context;

import com.matthewpatience.sentences.util.CastManager;
import com.matthewpatience.sentences.util.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mpatience on 15-09-13.
 */
public class SentencesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(Constants.FONT_ROBOTO_REGULAR)
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        CastManager.init(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
