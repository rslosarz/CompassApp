package slosar.example.compassapp.DataProcessing;

/**
 * Created by Rafal on 2016-04-15.
 */
public interface IActivityStateSensitive {
    void start();

    void resume();

    void pause();

    void stop();
}
