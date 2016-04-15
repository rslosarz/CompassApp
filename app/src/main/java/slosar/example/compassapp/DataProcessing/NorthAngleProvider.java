package slosar.example.compassapp.DataProcessing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.Observable;
import rx.Subscriber;

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
            subscriber.onNext(northAngleCalculation(mAccAxisValues, mMagAxisValues));
            subscriber.onCompleted();
        }
    });
    private final SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mAccAxisValues = event.values.clone();
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

    private void runNorthAngleCalculations() {
        northAngleCalculationObservable.subscribe(northAngleSubscriber);
    }

    private float northAngleCalculation(float[] accAxisValues, float[] magAxisValues) {
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
            return 0;
    }

    private float getFilteredValue(float oldValue, float newValue) {
        float bias = 0.7f;
        float diff = newValue - oldValue;
        diff = ensureDegreeFormat(diff);
        oldValue += bias * diff;
        return ensureDegreeFormat(oldValue);
    }

    private float ensureDegreeFormat(float angle) {
        while (angle >= 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {
        mSensorManager.registerListener(mAccelerometerListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mMagnetometerListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void pause() {
        mSensorManager.unregisterListener(mAccelerometerListener);
        mSensorManager.unregisterListener(mMagnetometerListener);
    }

    @Override
    public void stop() {

    }
}
