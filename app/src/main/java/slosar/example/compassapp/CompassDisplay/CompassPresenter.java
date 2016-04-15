package slosar.example.compassapp.CompassDisplay;

import android.content.Context;

import slosar.example.compassapp.DataProcessing.CompassDataManager;
import slosar.example.compassapp.DataProcessing.ICompassDataManager;

/**
 * Created by Rafal on 2016-04-12.
 */
class CompassPresenter implements ICompassPresenter, ICompassDataConsumer {

    private final ICompassView view;
    private final ICompassDataManager mDataProvider;

    public CompassPresenter(Context context, ICompassView view) {
        this.view = view;
        mDataProvider = new CompassDataManager(context, this);
    }

    @Override
    public void setNewDirectionCoordinates(float latitude, float longitude) {
        mDataProvider.setNewDirectionCoordinates(latitude, longitude);
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
