package slosar.example.compassapp.CompassDisplay;

import android.content.Context;

import slosar.example.compassapp.DataProcessing.CompassDataManager;

/**
 * Created by Rafal on 2016-04-12.
 */
class CompassPresenter implements ICompassDataConsumer {

    private final ICompassView view;

    public CompassPresenter(Context context, ICompassView view) {
        this.view = view;
        new CompassDataManager(context, this);
    }

    @Override
    public void onNewNorthData(float northAngleOld, float northAngleNew) {
        view.showCompassAngle(northAngleOld, northAngleNew);
    }

    @Override
    public void onNewDirectionData(float directionAngleOld, float directionAngleNew) {
        view.showDirectionAngle(directionAngleOld, directionAngleNew);
    }
}
