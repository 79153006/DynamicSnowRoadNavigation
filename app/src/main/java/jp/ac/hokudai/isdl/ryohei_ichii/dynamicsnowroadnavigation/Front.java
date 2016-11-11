package jp.ac.hokudai.isdl.ryohei_ichii.dynamicsnowroadnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Front extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        ImageButton btnrun = (ImageButton)this.findViewById(R.id.button_run);
        ImageButton btnmap = (ImageButton)this.findViewById(R.id.button_map);
        ImageButton btninfo = (ImageButton)this.findViewById(R.id.button_info);
        ImageButton btnpref = (ImageButton)this.findViewById(R.id.button_preference);

        btnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this,Run.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this,Map.class);
                startActivity(intent);
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this,Info.class);
                startActivity(intent);
            }
        });
        btnpref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Front.this,Pref.class);
                startActivity(intent);
            }
        });
    }
}
