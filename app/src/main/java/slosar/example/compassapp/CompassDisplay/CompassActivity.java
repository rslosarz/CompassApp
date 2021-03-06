package slosar.example.compassapp.CompassDisplay;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slosar.example.compassapp.DestinationInput.LocationInputActivity;
import slosar.example.compassapp.Events.ExceptionEvent;
import slosar.example.compassapp.Events.MainActivityStateChanged;
import slosar.example.compassapp.Exceptions.GooglePlayNotAvailableException;
import slosar.example.compassapp.Exceptions.NorthAngleCalculationException;
import slosar.example.compassapp.Exceptions.UserLocationUnavailableException;
import slosar.example.compassapp.R;

/**
 * CompassActivity - main activity
 */
public class CompassActivity extends AppCompatActivity implements ICompassView {

    private static final String ACTUAL_DIRECTION_ANGLE_TAG = "actual_direction_angle";

    @Bind(R.id.iv_compass)
    ImageView mIvCompass;
    @Bind(R.id.iv_direction)
    ImageView mIvDirection;

    private float mActualDirectionAngle = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);

        checkSystemServices();

        EventBus.getDefault().register(this);
        new CompassPresenter(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().postSticky(new MainActivityStateChanged(MainActivityStateChanged.getStart()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().postSticky(new MainActivityStateChanged(MainActivityStateChanged.getResume()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().postSticky(new MainActivityStateChanged(MainActivityStateChanged.getPause()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().postSticky(new MainActivityStateChanged(MainActivityStateChanged.getStop()));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putFloat(ACTUAL_DIRECTION_ANGLE_TAG, mActualDirectionAngle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mActualDirectionAngle = savedInstanceState.getFloat(ACTUAL_DIRECTION_ANGLE_TAG);
    }

    /**
     * Button set coordinate OnClickListener. Runs LocationInputActivity
     */
    @OnClick(R.id.bt_set_coordinate)
    public void setNewLatitude() {
        Intent intent = new Intent(this, LocationInputActivity.class);
        startActivity(intent);
    }

    /**
     * runs North Angle rotation animation. Rotates compass background and arrow.
     * @param currentAngle, newAngle - animation start and stop angle
     */
    @Override
    public void showCompassAngle(float currentAngle, float newAngle) {
        mIvCompass.startAnimation(getRotationObject(currentAngle, newAngle));
        mIvDirection.startAnimation(getRotationObject(currentAngle + mActualDirectionAngle, newAngle + mActualDirectionAngle));
    }

    /**
     * runs Direction Angle rotation animation. Rotates arrow, and stores current angle.
     * @param currentAngle - animation start angle
     * @param newAngle     - animation end angle
     */
    @Override
    public void showDirectionAngle(float currentAngle, float newAngle) {
        mIvDirection.startAnimation(getRotationObject(-currentAngle, -newAngle));
        mActualDirectionAngle = -newAngle;
    }

    /**
     * checks if required services are available.
     */
    private void checkSystemServices() {
        if (!gpsLocationEnabled()) {
            showGpsSettingsDialog();
        }
    }

    /**
     * checks if location or network services are turned on.
     * @return boolean - if one of required services is turned on.
     */
    private boolean gpsLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps, network;

        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps || network;
    }

    /**
     * shows alert dialog - no GPS service available. Can redirect to settings menu.
     */
    private void showGpsSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getResources().getString(R.string.gps_dialog_title));
        alertDialog.setMessage(getResources().getString(R.string.gps_dialog_message));

        alertDialog.setPositiveButton(getResources().getString(R.string.yes_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.no_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    /**
     * returns RotateAnimation object.
     * @param beginAngle - animation start angle
     * @param endAngle   - animation end angle
     */
    private RotateAnimation getRotationObject(float beginAngle, float endAngle) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                -beginAngle,
                -endAngle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);

        return rotateAnimation;
    }

    /**
     * EventBus subscribe method for exception events.
     * @param exceptionEvent - exception event object, stores exception
     */
    @Subscribe
    public void onException(ExceptionEvent exceptionEvent) {
        handleException(exceptionEvent.e);
    }

    /**
     * exception handle method - displays toast
     * @param e - exception
     */
    private void handleException(Exception e) {
        if (e instanceof NorthAngleCalculationException) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof GooglePlayNotAvailableException) {
            Toast.makeText(this, getResources().getString(R.string.google_api_not_available), Toast.LENGTH_SHORT).show();
        } else if (e instanceof UserLocationUnavailableException) {
            Toast.makeText(this, getResources().getString(R.string.user_location_unavailable_exception), Toast.LENGTH_SHORT).show();
        }
    }
}
