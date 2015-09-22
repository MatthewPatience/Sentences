package com.matthewpatience.sentences.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.matthewpatience.sentences.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    public static final int NAV_NEW_GAME = 0;
    public static final int NAV_LEADERBOARDS = 1;
    public static final int NAV_ACHIEVEMENTS = 2;

    private Button btnStartNewGame;
    private Button btnLeaderboards;
    private Button btnAchievements;

    private OnNavigationSelectedListener navListener;

    public void setOnNavigationSelectedListener(OnNavigationSelectedListener listener) {
        this.navListener = listener;
    }

    public interface OnNavigationSelectedListener {
        void onNavigationSelected(int navItem);
    }

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        btnStartNewGame = (Button) rootView.findViewById(R.id.btnStartNewGame);
        btnStartNewGame.setOnClickListener(this);
        btnLeaderboards = (Button) rootView.findViewById(R.id.btnLeaderboards);
        btnLeaderboards.setOnClickListener(this);
        btnAchievements = (Button) rootView.findViewById(R.id.btnAchievements);
        btnAchievements.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStartNewGame:
                if (navListener != null) navListener.onNavigationSelected(NAV_NEW_GAME);
                break;
            case R.id.btnLeaderboards:
                if (navListener != null) navListener.onNavigationSelected(NAV_LEADERBOARDS);
                break;
            case R.id.btnAchievements:
                if (navListener != null) navListener.onNavigationSelected(NAV_ACHIEVEMENTS);
                break;
        }

    }

}
