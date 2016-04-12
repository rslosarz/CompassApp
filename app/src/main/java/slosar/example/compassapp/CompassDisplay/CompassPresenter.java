package slosar.example.compassapp.CompassDisplay;

/**
 * Created by Rafal on 2016-04-12.
 */
class CompassPresenter implements ICompassPresenter {

    private final ICompassView view;

    public CompassPresenter(ICompassView view) {
        this.view = view;
    }

    @Override
    public void setNewLatitude(double latitude) {

    }

    @Override
    public void setNewLongitude(double longitude) {

    }
}
