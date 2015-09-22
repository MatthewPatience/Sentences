package com.matthewpatience.sentences;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.google.example.games.basegameutils.BaseGameActivity;
import com.matthewpatience.sentences.fragment.ConnectFragment;
import com.matthewpatience.sentences.fragment.LobbyFragment;
import com.matthewpatience.sentences.fragment.MainMenuFragment;
import com.matthewpatience.sentences.util.CastManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseGameActivity implements MainMenuFragment.OnNavigationSelectedListener, ConnectFragment.OnConnectListener {

    private MainMenuFragment mainMenuFragment;
    private ConnectFragment connectFragment;
    private LobbyFragment lobbyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuFragment = new MainMenuFragment();
        mainMenuFragment.setOnNavigationSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, mainMenuFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onNavigationSelected(int navItem) {

        switch (navItem) {
            case MainMenuFragment.NAV_NEW_GAME:
                startConnect();
                break;
            case MainMenuFragment.NAV_LEADERBOARDS:

                break;
            case MainMenuFragment.NAV_ACHIEVEMENTS:

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        CastManager.getInstance().onStart();

    }

    @Override
    protected void onStop() {

        CastManager.getInstance().onStop();

        super.onStop();
    }

    private void startConnect() {

        if (connectFragment == null) {
            connectFragment = new ConnectFragment();
            connectFragment.setOnConnectListener(this);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, connectFragment);
        ft.commit();

    }

    @Override
    public void onConnect() {

        if (lobbyFragment == null) {
            lobbyFragment = new LobbyFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, lobbyFragment);
        ft.commit();

    }
}
