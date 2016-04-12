package slosar.example.compassapp.CompassDisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slosar.example.compassapp.DataProcessing.CompassDataProvider;
import slosar.example.compassapp.DataProcessing.ICompassDataProvider;
import slosar.example.compassapp.Exceptions.WrongCoordinatesException;
import slosar.example.compassapp.R;

public class CompassActivity extends AppCompatActivity implements ICompassView {

    @Bind(R.id.iv_compass)
    private ImageView mIvCompass;
    @Bind(R.id.iv_direction)
    private ImageView mIvDirection;
    @Bind(R.id.et_latitude)
    private EditText mEtLatitude;
    @Bind(R.id.et_longitude)
    private EditText mEtLongitude;

    private ICompassDataProvider dataProvider;
    private ICompassPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);

        dataProvider = new CompassDataProvider(this);
        presenter = new CompassPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataProvider.dismiss();
    }

    @OnClick(R.id.bt_latitude)
    public void setNewLatitude() {
        try {
            float latitude = getCoordinate(mEtLatitude);
            float longitude = getCoordinate(mEtLongitude);
            presenter.setNewCoordinates(latitude, longitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @OnClick(R.id.bt_longitude)
    public void setNewLongitude() {
        try {
            float latitude = getCoordinate(mEtLatitude);
            float longitude = getCoordinate(mEtLongitude);
            presenter.setNewCoordinates(latitude, longitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @Override
    public void setCompassAngle(float currentAngle, float newAngle) {
        mIvCompass.startAnimation(getRotationObject(currentAngle, newAngle));
    }

    @Override
    public void setDirectionAngle(float currentAngle, float newAngle) {
        mIvDirection.startAnimation(getRotationObject(currentAngle, newAngle));
    }

    private RotateAnimation getRotationObject(float beginAngle, float endAngle) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                -beginAngle,
                -endAngle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(100);
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
