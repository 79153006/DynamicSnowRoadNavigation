package jp.ac.hokudai.isdl.ryohei_ichii.dynamicsnowroadnavigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Run extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //permissionチェック
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, "位置情報の利用許可されていないため、開始できません", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            //GPS情報取得関連
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 100, this);
        } catch (Exception e){
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
            toast.show();
            //MainActivityに戻す
            finish();
        }

        //Map取得関連
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
