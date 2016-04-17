package slosar.example.compassapp.CompassDisplay;

import android.content.Context;

import slosar.example.compassapp.DataProcessing.CompassDataManager;

/**
 * Created by Rafal on 2016-04-12.
 */
class CompassPresenter implements ICompassDataConsumer {

    private final ICompassView view;


    /**
     * @param context - context for CompassDataManager
     * @param view    - ICompassView - MainActivity
     * @return CompassPresenter object
     * @desc constructor
     */
    public CompassPresenter(Context context, ICompassView view) {
        this.view = view;
        new CompassDataManager(context, this);
    }

    /**
     * @desc sends north angle data to view.
     * @param northAngleOld - animation start angle
     * @param northAngleNew   - animation end angle
     * @return void
     */
    @Override
    public void onNewNorthData(float northAngleOld, float northAngleNew) {
        view.showCompassAngle(northAngleOld, northAngleNew);
    }

    /**
     * @desc sends direction angle data to view.
     * @param directionAngleOld - animation start angle
     * @param directionAngleNew   - animation end angle
     * @return void
     */
    @Override
    public void onNewDirectionData(float directionAngleOld, float directionAngleNew) {
        view.showDirectionAngle(directionAngleOld, directionAngleNew);
    }
}
