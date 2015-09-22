package com.matthewpatience.sentences.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.matthewpatience.sentences.R;
import com.matthewpatience.sentences.util.CastManager;

/**
 * Created by mpatience on 15-09-14.
 */
public class ConnectFragment extends Fragment implements Cast.MessageReceivedCallback, CastManager.StatusListener {

    private MediaRouteButton btnMediaRoute;

    private ProgressDialog loadingDialog;

    public OnConnectListener connectListener;

    public void setOnConnectListener(OnConnectListener listener) {

        this.connectListener = listener;

    }

    public interface OnConnectListener {
        void onConnect();
    }

    public ConnectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);

        btnMediaRoute = (MediaRouteButton) rootView.findViewById(R.id.btnMediaRoute);
        btnMediaRoute.setRouteSelector(CastManager.getInstance().getMediaRouteSelector());

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

        switch (status) {
            case CastManager.STATUS_CONNECTING:
                loadingDialog = ProgressDialog.show(getActivity(), null, "Connecting...", true, false);
                break;
            case CastManager.STATUS_CONNECTED:
                Log.d("TEST", "CONNECTED");
                break;
            case CastManager.STATUS_APPLICATION_LAUNCHED:
                Log.d("TEST", "APP LAUNCHED");
                loadingDialog.dismiss();
                joinLobby();
                break;
        }

    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String s, String s1) {

    }

    private void joinLobby() {

        if (connectListener != null) {
            connectListener.onConnect();
        }

    }

}
