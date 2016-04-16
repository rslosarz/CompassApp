package slosar.example.compassapp.Events;

/**
 * Created by Rafal on 2016-04-15.
 */
public class MainActivityStateChanged {
    private final int mState;


    public MainActivityStateChanged(int state) {
        mState = state;
    }

    public static int getStart() {
        return 1;
    }

    public static int getResume() {
        return 2;
    }

    public static int getPause() {
        return 3;
    }

    public static int getStop() {
        return 4;
    }

    public boolean isStart() {
        return mState == 1;
    }

    public boolean isResume() {
        return mState == 2;
    }

    public boolean isPause() {
        return mState == 3;
    }

    public boolean isStop() {
        return mState == 4;
    }

}
