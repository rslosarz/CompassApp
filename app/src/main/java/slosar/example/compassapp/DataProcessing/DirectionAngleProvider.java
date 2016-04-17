package slosar.example.compassapp.DataProcessing;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import slosar.example.compassapp.GoogleApi.GoogleApiManager;
import slosar.example.compassapp.GoogleApi.IGoogleApiManager;

/**
 * Direction angle provider - runs GPS service and provides bearing from user location to target location
 */
class DirectionAngleProvider implements IActivityStateSensitive {

    private ICompassDataManager mDataProvider;
    private IGoogleApiManager mGoogleApiManager;

    private float mDirectionAngle;
    private Location mDestinationLocation;
    private Location mCurrentLocation;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            mDataProvider.setUserLocation((float) mCurrentLocation.getLatitude(), (float) mCurrentLocation.getLongitude());
            calculateBearing();
        }
    };


    public DirectionAngleProvider(Context context, ICompassDataManager dataProvider) {
        mDataProvider = dataProvider;
        mGoogleApiManager = new GoogleApiManager(context);
        mGoogleApiManager.registerLocationListener(mLocationListener);
    }

    /**
     * method calculates bearing from user location to destination location
     */
    private void calculateBearing() {
        if (mDestinationLocation != null) {
            float bearing = mCurrentLocation.bearingTo(mDestinationLocation);
            bearing = getFilteredValue(mDirectionAngle, bearing);

            mDataProvider.setDirectionAngle(mDirectionAngle, bearing);
            mDirectionAngle = bearing;
        }
    }

    /**
     * method used by CompassDataManager for setting new destinationLocation coordinates
     * @param targetLocation - LatLng object of given destination location
     */
    public void setNewCoordinates(LatLng targetLocation) {
        if (mDestinationLocation == null) {
            mGoogleApiManager.resume();
        }
        mDestinationLocation = new Location("");
        mDestinationLocation.setLatitude(targetLocation.latitude);
        mDestinationLocation.setLongitude(targetLocation.longitude);
    }

    /**
     * filtration method - adds part of diff between old and new value
     * @param oldValue - current value
     * @param newValue - calculated value
     */
    private float getFilteredValue(float oldValue, float newValue) {
        float bias = 0.8f;
        float diff = newValue - oldValue;
        diff = ensureDegreeFormat(diff);
        oldValue += bias * diff;
        return ensureDegreeFormat(oldValue);
    }

    /**
     * ensures proper degree format from <-180, 180> range
     * @param angle - calculated angle
     * @return value from <-180, 180> range
     */
    private float ensureDegreeFormat(float angle) {
        while (angle >= 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }


    /**
     * method called when main activity runs OnStart method, starts GoogleApiClient
     */
    @Override
    public void start() {
        mGoogleApiManager.start();
    }

    /**
     * method called when main activity runs OnResume method, starts location updates
     */
    @Override
    public void resume() {
        if (mDestinationLocation != null) {
            mGoogleApiManager.resume();
        }
    }

    /**
     * method called when main activity runs OnPause method, stops location updates
     */
    @Override
    public void pause() {
        mGoogleApiManager.pause();
    }

    /**
     * method called when main activity runs OnStop method, stops location updates
     */
    @Override
    public void stop() {
        mGoogleApiManager.stop();
    }
}