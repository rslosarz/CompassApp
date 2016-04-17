package slosar.example.compassapp.DataProcessing;

/**
 * Interface of activity state methods
 */
interface IActivityStateSensitive {
    void start();

    void resume();

    void pause();

    void stop();
}
