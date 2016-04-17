package slosar.example.compassapp.GoogleApi;

import com.google.android.gms.location.LocationListener;

/**
 * GoogleApiManager interface
 */
public interface IGoogleApiManager {
    void registerLocationListener(LocationListener locationListener);

    void start();

    void resume();

    void pause();

    void stop();
}
