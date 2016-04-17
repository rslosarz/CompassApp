package slosar.example.compassapp.GoogleApi;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import slosar.example.compassapp.Events.ExceptionEvent;
import slosar.example.compassapp.Exceptions.GooglePlayNotAvailableException;

/**
 * Manager of GoogleApiClient.
 */
public class GoogleApiManager implements IGoogleApiManager {

    private static int UPDATE_INTERVAL = 100; // 0.1 sec
    private static int FASTEST_INTERVAL = 100; // 0.1 sec
    private static int DISPLACEMENT = 0; // 0 meters

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            startLocationUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {
            start();
        }
    };
    private GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    };


    /**
     * object constructor
     * @param context instance - application context for handling GoogleApiClient
     */
    public GoogleApiManager(Context context) {
        mContext = context;
        try {
            checkPlayServices();
        } catch (GooglePlayNotAvailableException e) {
            e.printStackTrace();
        }
        mGoogleApiClient = buildGoogleApiClient();
        mLocationRequest = createLocationRequest();
    }

    /**
     * registers location listener interface
     * @param locationListener instance
     */
    @Override
    public void registerLocationListener(LocationListener locationListener) {
        mLocationListener = locationListener;
    }

    /**
     * method called when main activity runs OnStart method, starts GoogleApiClient
     */
    @Override
    public void start() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * method called when main activity runs OnResume method, starts location updates
     */
    @Override
    public void resume() {
        try {
            checkPlayServices();
        } catch (GooglePlayNotAvailableException e) {
            e.printStackTrace();
            EventBus.getDefault().postSticky(new ExceptionEvent(e));
        }

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /**
     * method called when main activity runs OnPause method, stops location updates
     */
    @Override
    public void pause() {
        stopLocationUpdates();
    }

    /**
     * method called when main activity runs OnStop method, disconnects GoogleApiClient
     */
    @Override
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * checks if GooglePlayServices are available on device, throws custom exception
     */
    private void checkPlayServices() throws GooglePlayNotAvailableException {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);

        if (resultCode != ConnectionResult.SUCCESS)
            throw new GooglePlayNotAvailableException();
    }

    /**
     * builds GoogleApiClient object. Sets connection callbacks
     */
    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API).build();
    }

    /**
     * creates LocationRequest object - sets requests intervals and min displacement
     */
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

        return mLocationRequest;
    }

    /**
     * starts location requests from GoogleApiClient;
     */
    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
    }

    /**
     * stops location requests from GoogleApiClient;
     */
    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
    }
}
