package jp.ac.hokudai.isdl.ryohei_ichii.dynamicsnowroadnavigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.hardware.SensorManager.getOrientation;

public class Run extends FragmentActivity implements OnMapReadyCallback, LocationListener, SensorEventListener {

    private GoogleMap mMap;  //GoogleMapインスタンス
    private LocationManager locationManager;  //ロケーションマネージャ
    private SensorManager mSensorManager;   // センサマネージャ
    private Sensor mAccelerometer;  // 加速度センサ
    private Sensor mMagneticField;  // 磁気センサ
    private float degreeDir;  // 方位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagneticField = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //permissionチェック
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, "位置情報の利用許可されていないため、開始できません", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            //GPS情報取得関連
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 100, this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
            toast.show();
            //MainActivityに戻す
            finish();
        }

        //Map取得関連
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //ボタン操作
        ImageButton zoomtele = (ImageButton) this.findViewById(R.id.zoomtele);
        ImageButton zoomwide = (ImageButton) this.findViewById(R.id.zoomwide);
        ImageButton getcurrentposition = (ImageButton) this.findViewById(R.id.returncurrent);

        //ズームボタンを押したとき
        zoomtele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getCameraPosition().zoom < mMap.getMaxZoomLevel()) {
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom + 1));
                }else{
                    toastMassage("最大ズームです");
                }
            }
        });
        //ワイドボタンを押したとき
        zoomwide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getCameraPosition().zoom>mMap.getMinZoomLevel()) {
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 1));
                }else{
                    toastMassage("最小ズームです");
                }
            }
        });
        //現在地ボタンを押したとき
        getcurrentposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //実装中
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // リスナーの登録解除
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // リスナーの登録
        this.mSensorManager.registerListener(
                this, this.mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        this.mSensorManager.registerListener(
                this, this.mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }
    /*---ここからGPS関連---*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onLocationChanged(Location location) {
        CameraPosition.Builder cb = new CameraPosition.Builder(mMap.getCameraPosition());
        //カメラの回転
        cb.bearing(degreeDir);
        //現在地座標の取得
        cb.target(new LatLng(location.getLatitude(),location.getLongitude()));
        cb.tilt(50);
        //カメラの移動
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cb.build()));
        mMap.moveCamera(CameraUpdateFactory.scrollBy(0,-displayInfo()/4));
    }

    //画面の縦ピクセルを取得
    public int displayInfo(){;
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.y;
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
    /*---ここまでGPS関連---*/

    /*---ここからコンパス関連---*/
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // 加速度センサの値
    private float[] mAccelerometerValue = new float[3];
    // 磁気センサの値
    private float[] mMagneticFieldValue = new float[3];
    // 磁気センサの更新がすんだか
    private boolean mValidMagneticFiled = false;

    // センサーの値が変更された
    public void onSensorChanged(SensorEvent event) {

        // センサーごとの処理
        switch (event.sensor.getType()) {
            // 加速度センサー
            case Sensor.TYPE_ACCELEROMETER:
                this.mAccelerometerValue = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                this.mMagneticFieldValue = event.values.clone();
                this.mValidMagneticFiled = true;
                break;
        }

        // 値が更新された角度を出す準備ができた
        if (this.mValidMagneticFiled) {
            // 方位を出すための変換行列
            float[] rotate = new float[16]; // 傾斜行列
            float[] inclination = new float[16];    // 回転行列

            SensorManager.getRotationMatrix(rotate, inclination,this.mAccelerometerValue,this.mMagneticFieldValue);

            // 方向を求める
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotate, orientation);

            // デグリー角に変換する
            degreeDir = (float)Math.toDegrees(orientation[0]);
        }
    }

    public void toastMassage(String string){
        Toast toast = Toast.makeText(this,string,Toast.LENGTH_LONG);
        toast.show();
    }

}
