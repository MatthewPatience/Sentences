package com.matthewpatience.sentences.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.cast.CastDevice;
import com.matthewpatience.sentences.GameActivity;
import com.matthewpatience.sentences.R;
import com.matthewpatience.sentences.util.CastManager;

import java.util.ArrayList;

/**
 * Created by mpatience on 15-09-19.
 */
public class LobbyFragment extends Fragment implements CastManager.StatusListener, View.OnClickListener {

    private ListView lvPlayers;
    private Button btnStart;

    private ArrayAdapter<String> playerAdapter;
    private ArrayList<String> players;

    public LobbyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lobby, container, false);

        lvPlayers = (ListView) rootView.findViewById(R.id.lvPlayers);
        btnStart = (Button) rootView.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        players = new ArrayList<>();
        playerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, players);
        lvPlayers.setAdapter(playerAdapter);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            CastManager.getInstance().addStatusListener(this);
        } else {
            CastManager.getInstance().removeStatusListener(this);
        }

    }

    @Override
    public void onApplicationStatusChanged(int status) {

    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStart:
                startGame();
                break;
        }

    }

    private void startGame() {

        Intent intent = new Intent(getActivity(), GameActivity.class);
        getActivity().startActivity(intent);

    }

}
