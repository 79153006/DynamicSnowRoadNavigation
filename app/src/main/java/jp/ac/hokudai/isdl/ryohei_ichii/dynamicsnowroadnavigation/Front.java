package jp.ac.hokudai.isdl.ryohei_ichii.dynamicsnowroadnavigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Front extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        //permission関連
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }


        ImageButton btnrun = (ImageButton) this.findViewById(R.id.button_run);
        ImageButton btnmap = (ImageButton) this.findViewById(R.id.button_map);
        ImageButton btninfo = (ImageButton) this.findViewById(R.id.button_info);
        ImageButton btnpref = (ImageButton) this.findViewById(R.id.button_preference);

        btnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this, Run.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this, Map.class);
                startActivity(intent);
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this, Info.class);
                startActivity(intent);
            }
        });
        btnpref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this, Pref.class);
                startActivity(intent);
            }
        });
    }

    //permission確認
    public void checkPermission() {
        // GPS利用の許可がなかった場合に許可を求める
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Front.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
            }else{
                Toast toast = Toast.makeText(this, "Runモードの実行には位置情報の利用許可が必要です", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
