package slosar.example.compassapp.DataProcessing;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import slosar.example.compassapp.GoogleApi.GoogleApiManager;
import slosar.example.compassapp.GoogleApi.IGoogleApiManager;

/**
 * Created by Rafal on 2016-04-12.
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

    private void calculateBearing() {
        if (mDestinationLocation != null) {
            float bearing = mCurrentLocation.bearingTo(mDestinationLocation);
            bearing = getFilteredValue(mDirectionAngle, bearing);

            mDataProvider.setDirectionAngle(mDirectionAngle, bearing);
            mDirectionAngle = bearing;
        }
    }

    public void setNewCoordinates(LatLng targetLocation) {
        if (mDestinationLocation == null) {
            mGoogleApiManager.resume();
        }
        mDestinationLocation = new Location("");
        mDestinationLocation.setLatitude(targetLocation.latitude);
        mDestinationLocation.setLongitude(targetLocation.longitude);
    }

    private float getFilteredValue(float oldValue, float newValue) {
        float bias = 0.8f;
        float diff = newValue - oldValue;
        diff = ensureDegreeFormat(diff);
        oldValue += bias * diff;
        return ensureDegreeFormat(oldValue);
    }

    private float ensureDegreeFormat(float angle) {
        while (angle >= 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    @Override
    public void start() {
        mGoogleApiManager.start();
    }

    @Override
    public void resume() {
        if (mDestinationLocation != null) {
            mGoogleApiManager.resume();
        }
    }

    @Override
    public void pause() {
        mGoogleApiManager.pause();
    }

    @Override
    public void stop() {
        mGoogleApiManager.stop();
    }
}