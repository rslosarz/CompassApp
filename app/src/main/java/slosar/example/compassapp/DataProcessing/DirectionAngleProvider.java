package slosar.example.compassapp.DataProcessing;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import slosar.example.compassapp.GoogleApi.GoogleApiManager;

/**
 * Created by Rafal on 2016-04-12.
 */
class DirectionAngleProvider implements IActivityStateSensitive {

    private ICompassDataManager mDataProvider;
    private GoogleApiManager mGoogleApiManager;

    private float mDirectionAngle;
    private Location mDestinationLocation;
    private Location mCurrentLocation;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            calculateBearing();
        }
    };

    public DirectionAngleProvider(Context context, ICompassDataManager dataProvider) {
        mDataProvider = dataProvider;
        mGoogleApiManager = new GoogleApiManager(context);
        mGoogleApiManager.registerLocationListener(mLocationListener);

        mDestinationLocation = new Location("");
        mDestinationLocation.setLatitude(52.4320811d);
        mDestinationLocation.setLongitude(16.5429719d);
    }

    private void calculateBearing() {
        if (mDestinationLocation != null) {
            float bearing = mCurrentLocation.bearingTo(mDestinationLocation);
            bearing = getFilteredValue(mDirectionAngle, bearing);

            mDataProvider.setDirectionAngle(mDirectionAngle, bearing);
            mDirectionAngle = bearing;
        }
    }

    public void setNewCoordinates(float latitude, float longitude) {
        mDestinationLocation = new Location("");
        mDestinationLocation.setLatitude(latitude);
        mDestinationLocation.setLongitude(longitude);
    }

    private float getFilteredValue(float oldValue, float newValue) {
        float bias = 0.3f;
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
        mGoogleApiManager.resume();
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