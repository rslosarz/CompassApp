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
 * Created by Rafal on 2016-04-15.
 */
public class GoogleApiManager implements IGoogleApiManager {

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 100; // 0.1 sec
    private static int FATEST_INTERVAL = 100; // 0.1 sec
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

    @Override
    public void registerLocationListener(LocationListener locationListener) {
        mLocationListener = locationListener;
    }

    @Override
    public void start() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

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

    @Override
    public void pause() {
        stopLocationUpdates();
    }

    @Override
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() throws GooglePlayNotAvailableException {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);

        if (resultCode != ConnectionResult.SUCCESS)
            throw new GooglePlayNotAvailableException();
        else
            return true;
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API).build();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

        return mLocationRequest;
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
    }
}
