package slosar.example.compassapp.DataProcessing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import slosar.example.compassapp.Events.ExceptionEvent;
import slosar.example.compassapp.Exceptions.NorthAngleCalculationException;

/**
 * Created by Rafal on 2016-04-12.
 */
class NorthAngleProvider implements IActivityStateSensitive {

    private ICompassDataManager mDataProvider;
    private SensorManager mSensorManager;
    private float mNorthAngle;
    private final Subscriber northAngleSubscriber = new Subscriber<Float>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            try {
                throw new NorthAngleCalculationException(e.getMessage());
            } catch (NorthAngleCalculationException ex) {
                ex.printStackTrace();
                EventBus.getDefault().postSticky(new ExceptionEvent(ex));
            }
        }

        @Override
        public void onNext(Float aFloat) {
            mDataProvider.setNorthAngle(mNorthAngle, aFloat);
            mNorthAngle = aFloat;
        }
    };
    private float[] mAccAxisValues;
    private float[] mMagAxisValues;
    private final Observable northAngleCalculationObservable = Observable.create(new Observable.OnSubscribe<Subscriber>() {
        @Override
        public void call(Subscriber subscriber) {
            try {
                subscriber.onNext(northAngleCalculation(mAccAxisValues, mMagAxisValues));
            } catch (NorthAngleCalculationException e) {
                e.printStackTrace();
                EventBus.getDefault().postSticky(new ExceptionEvent(e));
            }
            subscriber.onCompleted();
        }
    });
    private final SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mAccAxisValues = event.values.clone();
                if (mMagAxisValues != null)
                    runNorthAngleCalculations();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private final SensorEventListener mMagnetometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mMagAxisValues = event.values.clone();
                if (mAccAxisValues != null)
                    runNorthAngleCalculations();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    public NorthAngleProvider(Context context, ICompassDataManager dataProvider) {
        mDataProvider = dataProvider;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * @return void
     * @desc method calling RxJava subscription.
     */
    private void runNorthAngleCalculations() {
        northAngleCalculationObservable.subscribe(northAngleSubscriber);
    }

    /**
     * @param accAxisValues - acceleration axis measurments (X,Y,Z)
     * @param magAxisValues - magnetometer axis measurments (X,Y,Z)
     * @return void
     * @desc method calculating north angle with magnetometer and accelerometer data, avoiding drifts. Gets rotation matrix (assuming that acceleration measures gravity only), and gets 3 angles of orientation.
     */
    private float northAngleCalculation(float[] accAxisValues, float[] magAxisValues) throws NorthAngleCalculationException {
        float rotationMatrix[] = new float[9];
        float inclinationMatrix[] = new float[9];
        float orientationVector[] = new float[3];

        boolean success = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix,
                accAxisValues, magAxisValues);
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationVector);
            float rawNorthAngle = (float) Math.toDegrees((double) orientationVector[0]);
            return getFilteredValue(mNorthAngle, rawNorthAngle);
        } else
            throw new NorthAngleCalculationException("To strong magnetic field!");
    }

    /**
     * @param oldValue - current value
     * @param newValue - calculated value
     * @return filtrated value
     * @desc filtration method - adds part of diff between old and new value
     */
    private float getFilteredValue(float oldValue, float newValue) {
        float bias = 0.3f;
        float diff = newValue - oldValue;
        diff = ensureDegreeFormat(diff);
        oldValue += bias * diff;
        return ensureDegreeFormat(oldValue);
    }

    /**
     * @param angle - calculated angle
     * @return value from <-180, 180> range
     * @desc ensures proper degree format from <-180, 180> range
     */
    private float ensureDegreeFormat(float angle) {
        while (angle >= 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    /**
     * @return void
     * @desc method called when main activity runs OnStart method, starts GoogleApiClient
     */
    @Override
    public void start() {

    }

    /**
     * @return void
     * @desc method called when main activity runs OnResume method, starts GoogleApiClient
     */
    @Override
    public void resume() {
        mSensorManager.registerListener(mAccelerometerListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mMagnetometerListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * @return void
     * @desc method called when main activity runs OnPause method, starts GoogleApiClient
     */
    @Override
    public void pause() {
        mSensorManager.unregisterListener(mAccelerometerListener);
        mSensorManager.unregisterListener(mMagnetometerListener);
    }

    /**
     * @return void
     * @desc method called when main activity runs OnStop method, starts GoogleApiClient
     */
    @Override
    public void stop() {

    }
}
