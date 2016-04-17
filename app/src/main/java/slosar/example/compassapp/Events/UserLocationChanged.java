package slosar.example.compassapp.Events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Rafal on 2016-04-15.
 */
public class UserLocationChanged {
    private final float mLatitude, mLongitude;


    public UserLocationChanged(float latitude, float longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public LatLng getPosition() {
        return new LatLng(mLatitude, mLongitude);
    }
}
