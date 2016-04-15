package slosar.example.compassapp.GoogleApi;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Rafal on 2016-04-15.
 */
public interface IGoogleApiManager {
    void registerLocationListener(LocationListener locationListener);

    void start();

    void resume();

    void pause();

    void stop();
}
