package slosar.example.compassapp.DataProcessing;

/**
 * CompassDataManager interface
 */
public interface ICompassDataManager {
    void setNorthAngle(float northAngleOld, float northAngleNew);

    void setDirectionAngle(float destinationAngleOld, float destinationAngleNew);

    void setUserLocation(float latitude, float longitude);
}
