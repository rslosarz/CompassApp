package slosar.example.compassapp.CompassDisplay;

/**
 * Created by Rafal on 2016-04-15.
 */
public interface ICompassDataConsumer {
    void onNewNorthData(float northAngleOld, float northAngleNew);

    void onNewDirectionData(float directionAngleOld, float directionAngleNew);
}
