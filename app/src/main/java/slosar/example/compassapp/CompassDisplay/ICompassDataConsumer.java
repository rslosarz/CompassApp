package slosar.example.compassapp.CompassDisplay;

/**
 * Interface of CompassPresenter - receiver of compass data
 */
public interface ICompassDataConsumer {
    void onNewNorthData(float northAngleOld, float northAngleNew);

    void onNewDirectionData(float directionAngleOld, float directionAngleNew);
}
