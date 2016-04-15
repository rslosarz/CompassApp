package slosar.example.compassapp.DataProcessing;

/**
 * Created by Rafal on 2016-04-12.
 */
public interface ICompassDataManager {
    void setNorthAngle(float northAngleOld, float northAngleNew);

    void setDirectionAngle(float destinationAngleOld, float destinationAngleNew);

    void setNewDirectionCoordinates(float latitude, float longitude);
}
