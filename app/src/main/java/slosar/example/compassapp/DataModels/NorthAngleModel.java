package slosar.example.compassapp.DataModels;

/**
 * Created by Rafal on 2016-04-13.
 */
public class NorthAngleModel {
    private final float compassNorthAngleOld;
    private final float compassNorthAngleNew;

    public NorthAngleModel(float northAngleOld, float northAngleNew) {
        compassNorthAngleOld = northAngleOld;
        compassNorthAngleNew = northAngleNew;
    }

    public float getCompassNorthAngleOld() {
        return compassNorthAngleOld;
    }

    public float getCompassNorthAngleNew() {
        return compassNorthAngleNew;
    }
}
