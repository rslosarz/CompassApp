package slosar.example.compassapp.Events;

import com.google.android.gms.maps.model.LatLng;

/**
 * EventBus event object - target location
 */
public class TargetLocationChanged {
    public final LatLng mTargetLocation;


    public TargetLocationChanged(LatLng targetLocation) {
        mTargetLocation = targetLocation;
    }
}
