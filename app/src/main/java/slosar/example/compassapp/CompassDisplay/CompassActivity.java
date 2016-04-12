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

    private ICompassPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);

        presenter = new CompassPresenter(this);
    }

    @OnClick(R.id.bt_latitude)
    public void setNewLatitude() {
        try {
            double latitude = getCoordinate(mEtLatitude);
            presenter.setNewLatitude(latitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @OnClick(R.id.bt_longitude)
    public void setNewLongitude() {
        try {
            double longitude = getCoordinate(mEtLongitude);
            presenter.setNewLongitude(longitude);
        } catch (WrongCoordinatesException e) {
            handleException(e);
        }
    }

    @Override
    public void setCompassAngle(double currentAngle, double newAngle) {
        mIvCompass.startAnimation(getRotationObject(currentAngle, newAngle));
    }

    @Override
    public void setDirectionAngle(double currentAngle, double newAngle) {
        mIvDirection.startAnimation(getRotationObject(currentAngle, newAngle));
    }

    private RotateAnimation getRotationObject(double beginAngle, double endAngle) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                -(float) beginAngle,
                -(float) endAngle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);

        return rotateAnimation;
    }

    private double getCoordinate(EditText editText) throws WrongCoordinatesException {
        String text = editText.getText().toString();
        if (text.isEmpty())
            throw new WrongCoordinatesException(getResources().getString(R.string.empty_coordinate_exception));
        try {
            return Double.parseDouble(mEtLongitude.getText().toString());
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
