package slosar.example.compassapp.CompassDisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slosar.example.compassapp.R;

public class CompassActivity extends AppCompatActivity implements ICompassView {

    @Bind(R.id.iv_compass)
    ImageView mIvCompass;
    @Bind(R.id.iv_direction)
    ImageView mIvDirection;

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

    }

    @OnClick(R.id.bt_longitude)
    public void setNewLongitude() {

    }
}
