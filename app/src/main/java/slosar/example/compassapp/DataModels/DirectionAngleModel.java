package slosar.example.compassapp.DataModels;

/**
 * Created by Rafal on 2016-04-13.
 */
public class DirectionAngleModel {
    private float directionAngleOld;
    private float directionAngleNew;

    public DirectionAngleModel(float directionAngleOld, float directionAngleNew) {
        this.directionAngleOld = directionAngleOld;
        this.directionAngleNew = directionAngleNew;
    }

    public float getDirectionAngleOld() {
        return directionAngleOld;
    }

    public void setDirectionAngleOld(float directionAngleOld) {
        this.directionAngleOld = directionAngleOld;
    }

    public float getDirectionAngleNew() {
        return directionAngleNew;
    }

    public void setDirectionAngleNew(float directionAngleNew) {
        this.directionAngleNew = directionAngleNew;
    }
}
