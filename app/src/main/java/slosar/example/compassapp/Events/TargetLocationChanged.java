package slosar.example.compassapp.Events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Rafal on 2016-04-15.
 */
public class TargetLocationChanged {
    public final LatLng mTargetLocation;

    public TargetLocationChanged(LatLng targetLocation) {
        mTargetLocation = targetLocation;
    }
}
