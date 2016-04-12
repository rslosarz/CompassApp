package slosar.example.compassapp.DataProcessing;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import slosar.example.compassapp.DataModels.NorthAngleModel;

/**
 * Created by Rafal on 2016-04-12.
 */
public class CompassDataProvider implements ICompassDataProvider {

    private NorthAngleProvider mNorthAngleProvider;
    private DirectionAngleProvider mDirectionAngleProvider;

    public CompassDataProvider(Context context) {
        mNorthAngleProvider = new NorthAngleProvider(context, this);
        mDirectionAngleProvider = new DirectionAngleProvider(context, this);
    }

    @Override
    public void setNorthAngle(float northAngleOld, float northAngleNew) {
        NorthAngleModel mNorthAngleModel = new NorthAngleModel(northAngleOld, northAngleNew);
        EventBus.getDefault().postSticky(mNorthAngleModel);
    }

    @Override
    public void dismiss() {
        mNorthAngleProvider.dismiss();
        mDirectionAngleProvider.dismiss();
    }
}
