package slosar.example.compassapp.DataProcessing;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import slosar.example.compassapp.CompassDisplay.ICompassDataConsumer;
import slosar.example.compassapp.Events.MainActivityStateChanged;
import slosar.example.compassapp.Events.TargetLocationChanged;
import slosar.example.compassapp.Events.UserLocationChanged;

/**
 * Data manager - runs and manages objects provides north angle and direction angle
 */
public class CompassDataManager implements ICompassDataManager {

    private ICompassDataConsumer mCompassDataConsumer;
    private NorthAngleProvider mNorthAngleProvider;
    private DirectionAngleProvider mDirectionAngleProvider;


    public CompassDataManager(Context context, ICompassDataConsumer compassDataConsumer) {
        mCompassDataConsumer = compassDataConsumer;
        mNorthAngleProvider = new NorthAngleProvider(context, this);
        mDirectionAngleProvider = new DirectionAngleProvider(context, this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void setNorthAngle(float northAngleOld, float northAngleNew) {
        mCompassDataConsumer.onNewNorthData(northAngleOld, northAngleNew);
    }

    @Override
    public void setDirectionAngle(float directionAngleOld, float directionAngleNew) {
        mCompassDataConsumer.onNewDirectionData(directionAngleOld, directionAngleNew);
    }

    @Override
    public void setUserLocation(float latitude, float longitude) {
        EventBus.getDefault().postSticky(new UserLocationChanged(latitude, longitude));
    }

    /**
     * EventBus subscribe method - reacts on change of main activity state
     * @param stateChanged - event object
     */
    @Subscribe
    public void onMainActivityStateChanged(MainActivityStateChanged stateChanged) {
        if (stateChanged.isStart()) {
            mNorthAngleProvider.start();
            mDirectionAngleProvider.start();
        } else if (stateChanged.isResume()) {
            mNorthAngleProvider.resume();
            mDirectionAngleProvider.resume();
        } else if (stateChanged.isPause()) {
            mNorthAngleProvider.pause();
            mDirectionAngleProvider.pause();
        } else if (stateChanged.isStop()) {
            mNorthAngleProvider.stop();
            mDirectionAngleProvider.stop();
        }
    }

    /**
     * EventBus subscribe method - reacts on target location changed
     * @param targetLocationChanged - event object
     */
    @Subscribe
    public void onTargetLocationChanged(TargetLocationChanged targetLocationChanged) {
        mDirectionAngleProvider.setNewCoordinates(targetLocationChanged.mTargetLocation);
    }
}
