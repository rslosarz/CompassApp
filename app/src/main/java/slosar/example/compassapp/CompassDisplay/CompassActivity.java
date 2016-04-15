package slosar.example.compassapp.CompassDisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slosar.example.compassapp.Events.MainActivityStateChanged;
import slosar.example.compassapp.Exceptions.WrongCoordinatesException;
import slosar.example.compassapp.R;

public class CompassActivity extends AppCompatActivity implements ICompassView {

    @Bind(R.id.iv_compass)
    ImageView mIvCompass;
    @Bind(R.id.iv_direction)
    ImageView mIvDirection;
    @Bind(R.id.et_latitude)
    EditText mEtLatitude;
    @Bind(R.id.et_longitude)
    EditText mEtLongitude;

    private ICompassPresenter mPresenter;
    private float mActualDirectionAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);

        mPresenter = new CompassPresenter(this, this);
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

    @OnClick(R.id.bt_latitude)
    public void setNewLatitude() {
        try {
            float latitude = getCoordinate(mEtLatitude);
            float longitude = getCoordinate(mEtLongitude);
            mPresenter.setNewDirectionCoordinates(latitude, longitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @OnClick(R.id.bt_longitude)
    public void setNewLongitude() {
        try {
            float latitude = getCoordinate(mEtLatitude);
            float longitude = getCoordinate(mEtLongitude);
            mPresenter.setNewDirectionCoordinates(latitude, longitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @Override
    public void showCompassAngle(float currentAngle, float newAngle) {
        mIvCompass.startAnimation(getRotationObject(currentAngle, newAngle));
        mIvDirection.startAnimation(getRotationObject(currentAngle + mActualDirectionAngle, newAngle + mActualDirectionAngle));
    }

    @Override
    public void showDirectionAngle(float currentAngle, float newAngle) {
        mIvDirection.startAnimation(getRotationObject(currentAngle, newAngle));
        mActualDirectionAngle = newAngle;
    }

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

    private float getCoordinate(EditText editText) throws WrongCoordinatesException {
        String text = editText.getText().toString();
        if (text.isEmpty())
            throw new WrongCoordinatesException(getResources().getString(R.string.empty_coordinate_exception));
        try {
            return Float.parseFloat(mEtLongitude.getText().toString());
        } catch (NumberFormatException e) {
            throw new WrongCoordinatesException(getResources().getString(R.string.wrong_coordinate_exception));
        }
    }

    private void handleException(Exception e) {
        if (e instanceof WrongCoordinatesException) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
