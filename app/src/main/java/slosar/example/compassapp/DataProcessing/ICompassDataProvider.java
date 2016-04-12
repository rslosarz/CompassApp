package slosar.example.compassapp.DataProcessing;

/**
 * Created by Rafal on 2016-04-12.
 */
public interface ICompassDataProvider {
    void setNorthAngle(float northAngleOld, float northAngleNew);

    void dismiss();
}
