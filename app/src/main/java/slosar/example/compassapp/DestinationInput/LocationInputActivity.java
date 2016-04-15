package slosar.example.compassapp.DestinationInput;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slosar.example.compassapp.Events.TargetLocationChanged;
import slosar.example.compassapp.R;

public class LocationInputActivity extends FragmentActivity {

    @Bind(R.id.tv_latitude)
    TextView mTvLatitude;
    @Bind(R.id.tv_longitude)
    TextView mTvLongitude;
    private GoogleMap mMap;
    private Marker mUserMarker;
    private Marker mTargetMarker;
    private LatLng selected;
    private LatLng mUserLatLng;
    private GoogleMap.OnMapClickListener mOnMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            onMapClickHandler(latLng);
        }
    };

    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setOnMapClickListener(mOnMapClickListener);
            mUserMarker = mMap.addMarker(new MarkerOptions().position(mUserLatLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserLatLng, 15));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mOnMapReadyCallback);

        EventBus.getDefault().register(this);
        UserLocationChanged userLocationChanged = EventBus.getDefault().getStickyEvent(UserLocationChanged.class);
        mUserLatLng = userLocationChanged.getPosition();
    }

    @OnClick(R.id.bt_set_coordinate)
    public void onBtSetCoordinatesClick() {
        EventBus.getDefault().postSticky(new TargetLocationChanged(selected));
        finish();
    }


    @Subscribe
    public void onUserLocationChanged(UserLocationChanged userLocationChanged) {
        if (mUserMarker != null)
            mUserMarker.remove();
        mUserMarker = mMap.addMarker(new MarkerOptions().position(userLocationChanged.getPosition()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocationChanged.getPosition(), 12));
    }

    private void onMapClickHandler(LatLng latLng) {
        selected = latLng;
        String latitudeString = String.format("%.5f", latLng.latitude);
        String longitudeString = String.format("%.5f", latLng.longitude);
        mTvLatitude.setText(latitudeString);
        mTvLongitude.setText(longitudeString);

        if (mTargetMarker == null) {
            mTargetMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            mTargetMarker.remove();
            mTargetMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }
}