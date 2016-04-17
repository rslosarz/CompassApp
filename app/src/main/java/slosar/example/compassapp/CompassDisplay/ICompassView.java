package slosar.example.compassapp.CompassDisplay;

/**
 * Interface of CompassActivity - View
 */
interface ICompassView {
    void showCompassAngle(float currentAngle, float newAngle);

    void showDirectionAngle(float currentAngle, float newAngle);
}
