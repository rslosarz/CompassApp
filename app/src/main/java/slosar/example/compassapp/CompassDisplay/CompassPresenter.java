package slosar.example.compassapp.CompassDisplay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import slosar.example.compassapp.DataModels.NorthAngleModel;

/**
 * Created by Rafal on 2016-04-12.
 */
class CompassPresenter implements ICompassPresenter {

    private final ICompassView view;

    public CompassPresenter(ICompassView view) {
        this.view = view;

        EventBus.getDefault().register(this);
    }

    @Override
    public void setNewCoordinates(float latitude, float longitude) {

    }

    @Subscribe
    public void onNewCompassData(NorthAngleModel northAngleModel) {
        view.setCompassAngle(northAngleModel.getCompassNorthAngleOld(), northAngleModel.getCompassNorthAngleNew());
    }
}
