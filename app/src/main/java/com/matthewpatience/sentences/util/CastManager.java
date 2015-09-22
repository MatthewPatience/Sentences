package com.matthewpatience.sentences.util;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mpatience on 15-09-18.
 */
public class CastManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int STATUS_CONNECTING = 0;
    public static final int STATUS_CONNECTED = 1;
    public static final int STATUS_DISCONNECTED = 2;
    public static final int STATUS_APPLICATION_LAUNCHED = 3;
    public static final int STATUS_APPLICATION_DISCONNECTED = 4;
    public static final int STATUS_VOLUME_CHANGED = 5;

    private MediaRouter mediaRouter;
    private MediaRouteSelector mediaRouteSelector;
    private GoogleApiClient castApiClient;

    private CastDevice castDevice;
    private String routeId;
    private String sessionId;

    private Context context;
    private ArrayList<StatusListener> statusListeners;

    private boolean waitingForReconnect = false;
    private boolean applicationLaunched = false;

    public interface StatusListener {
        void onApplicationStatusChanged(int status);
        void onMessageReceived(CastDevice castDevice, String namespace, String message);
    }

    public static CastManager instance;

    private CastManager(Context context) {

        statusListeners = new ArrayList<>();
        initMediaRouter(context);

    }

    public static void init(Application context) {

        if (instance == null) {
            instance = new CastManager(context);
            instance.context = context;
        }

    }

    public static CastManager getInstance() {

        return instance;
    }

    private MediaRouteSelector initMediaRouter(Context context) {

        if (mediaRouter == null) {
            mediaRouter = MediaRouter.getInstance(context);
        }

        mediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(Constants.CHROMECAST_APP_ID))
                .build();

        return mediaRouteSelector;
    }

    private void connectCastApiClient() throws IllegalStateException {

        if (castDevice == null) {
            throw new IllegalStateException("Must select a cast device before attempting to connect.");
        }

        Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                .builder(castDevice, castListener);

        castApiClient = new GoogleApiClient.Builder(context)
                .addApi(Cast.API, apiOptionsBuilder.build())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        castApiClient.connect();

    }

    public void addStatusListener(StatusListener listener) {

        statusListeners.add(listener);

    }

    public void removeStatusListener(StatusListener listener) {

        statusListeners.remove(listener);

    }

    public void removeAllStatusListeners() {

        statusListeners.clear();

    }

    public CastDevice getCastDevice() {

        return castDevice;
    }

    public MediaRouteSelector getMediaRouteSelector() {

        return mediaRouteSelector;
    }

    public boolean isApplicationLaunched() {

        return applicationLaunched;
    }

    public boolean isConnected() {

        return castApiClient.isConnected();
    }

    public String getApplicationStatus() {

        return Cast.CastApi.getApplicationStatus(castApiClient);
    }

    public void disconnect() {

        if (castApiClient != null) {
            castApiClient.disconnect();
        }

    }

    public void onStart() {

        mediaRouter.addCallback(mediaRouteSelector, routerCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);

    }

    public void onStop() {

        mediaRouter.removeCallback(routerCallback);

    }

    private void sendMessage(String message) {

        if (castApiClient != null && messageReceivedCallback != null) {
            try {
                Cast.CastApi.sendMessage(castApiClient, Constants.CHROMECAST_NAMESPACE, message)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status result) {
                                        if (!result.isSuccess()) {
                                            Log.e(this.getClass().getSimpleName(), "Sending message failed");
                                        }
                                    }
                                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private MediaRouter.Callback routerCallback = new MediaRouter.Callback() {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {

            castDevice = CastDevice.getFromBundle(info.getExtras());
            routeId = info.getId();
            for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                StatusListener listener = itr.next();
                listener.onApplicationStatusChanged(STATUS_CONNECTING);
            }
            connectCastApiClient();

        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {

            castDevice = null;

        }
    };

    private Cast.Listener castListener = new Cast.Listener() {

        public void onApplicationStatusChanged() {

        }

        public void onApplicationMetadataChanged(ApplicationMetadata applicationMetadata) {

        }

        public void onApplicationDisconnected(int statusCode) {

            applicationLaunched = false;
            for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                StatusListener listener = itr.next();
                listener.onApplicationStatusChanged(STATUS_APPLICATION_DISCONNECTED);
            }

        }

        public void onActiveInputStateChanged(int activeInputState) {

        }

        public void onStandbyStateChanged(int standbyState) {

        }

        public void onVolumeChanged() {
            if (castApiClient != null) {
                for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                    StatusListener listener = itr.next();
                    listener.onApplicationStatusChanged(STATUS_VOLUME_CHANGED);
                }
            }
        }

    };

    @Override
    public void onConnected(Bundle bundle) {

        if (waitingForReconnect) {
            waitingForReconnect = false;
            connectCastApiClient();
        } else {

            if (castApiClient != null) {
                for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                    StatusListener listener = itr.next();
                    listener.onApplicationStatusChanged(STATUS_CONNECTED);
                }
            }

            try {
                Cast.CastApi.launchApplication(castApiClient, Constants.CHROMECAST_APP_ID, false)
                        .setResultCallback(
                                new ResultCallback<Cast.ApplicationConnectionResult>() {
                                    @Override
                                    public void onResult(Cast.ApplicationConnectionResult result) {
                                        handleLaunchResponse(result);
                                    }
                                });

            } catch (Exception e) {
                applicationLaunched = false;
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        waitingForReconnect = true;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (castApiClient != null) {
            for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                StatusListener listener = itr.next();
                listener.onApplicationStatusChanged(STATUS_DISCONNECTED);
            }
        }
        applicationLaunched = false;

    }

    private void handleLaunchResponse(Cast.ApplicationConnectionResult result) {

        Status status = result.getStatus();
        if (status.isSuccess()) {
            ApplicationMetadata applicationMetadata =
                    result.getApplicationMetadata();
            sessionId = result.getSessionId();
            applicationLaunched = result.getWasLaunched();

            try {
                Cast.CastApi.setMessageReceivedCallbacks(castApiClient,
                        Constants.CHROMECAST_NAMESPACE,
                        messageReceivedCallback);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (castApiClient != null) {
                for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                    StatusListener listener = itr.next();
                    listener.onApplicationStatusChanged(STATUS_APPLICATION_LAUNCHED);
                }
            }
        } else {
            applicationLaunched = false;
        }

    }

    private Cast.MessageReceivedCallback messageReceivedCallback = new Cast.MessageReceivedCallback() {

        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace, String message) {

            for (Iterator<StatusListener> itr = statusListeners.iterator(); itr.hasNext();) {
                StatusListener listener = itr.next();
                listener.onMessageReceived(castDevice, namespace, message);
            }

        }

    };

}
