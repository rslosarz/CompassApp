package slosar.example.compassapp.CompassDisplay;

import android.content.Context;

import slosar.example.compassapp.DataProcessing.CompassDataManager;

/**
 * CompassActivity presenter
 */
class CompassPresenter implements ICompassDataConsumer {

    private final ICompassView view;


    /**
     * @param context - context for CompassDataManager
     * @param view    - ICompassView - MainActivity
     */
    public CompassPresenter(Context context, ICompassView view) {
        this.view = view;
        new CompassDataManager(context, this);
    }

    /**
     * sends north angle data to view.
     * @param northAngleOld - animation start angle
     * @param northAngleNew   - animation end angle
     */
    @Override
    public void onNewNorthData(float northAngleOld, float northAngleNew) {
        view.showCompassAngle(northAngleOld, northAngleNew);
    }

    /**
     * sends direction angle data to view.
     * @param directionAngleOld - animation start angle
     * @param directionAngleNew   - animation end angle
     */
    @Override
    public void onNewDirectionData(float directionAngleOld, float directionAngleNew) {
        view.showDirectionAngle(directionAngleOld, directionAngleNew);
    }
}
