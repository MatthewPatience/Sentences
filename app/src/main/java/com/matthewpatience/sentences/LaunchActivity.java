package com.matthewpatience.sentences;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.BaseGameActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mpatience on 15-09-13.
 */
public class LaunchActivity extends BaseGameActivity {

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_launch);

        checkGooglePlayServices();


    }

    private boolean checkGooglePlayServices() {

        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
                this);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            default:
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck,
                        this, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                dialog.show();
        }

        return false;
    }

    @Override
    public void onSignInFailed() {

        startMainActivity();

    }

    @Override
    public void onSignInSucceeded() {

        startMainActivity();

    }

    private void startMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
