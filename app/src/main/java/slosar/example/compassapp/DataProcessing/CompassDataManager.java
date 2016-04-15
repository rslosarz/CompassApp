package slosar.example.compassapp.DataProcessing;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import slosar.example.compassapp.CompassDisplay.ICompassDataConsumer;
import slosar.example.compassapp.Events.MainActivityStateChanged;

/**
 * Created by Rafal on 2016-04-12.
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
    public void setNewDirectionCoordinates(float latitude, float longitude) {
        mDirectionAngleProvider.setNewCoordinates(latitude, longitude);
    }

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
}
