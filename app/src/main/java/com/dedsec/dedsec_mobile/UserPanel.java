package com.dedsec.dedsec_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserPanel extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_panel);

        // Aplicar EdgeToEdge para gestionar los insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        geo_service_view();
        recycler_view();
        media_service();
        crud_sqlite();
    }

    private void geo_service_view(){
        button = findViewById(R.id.btn_ingress_map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(UserPanel.this, GeoService.class);
                startActivity(activity);
            }
        });
    }

    private void recycler_view(){
        button = findViewById(R.id.btn_ingress_recycler);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(UserPanel.this, Recycler_View.class);
                startActivity(activity);
            }
        });
    }

    private void media_service(){
        button = findViewById(R.id.btn_ingress_media_service);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(UserPanel.this, VideoService.class);
                startActivity(activity);
            }
        });
    }

    private  void crud_sqlite(){
        button = findViewById(R.id.btn_sqlite_service);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent activity = new Intent(UserPanel.this, CrudSQLite.class);
                startActivity(activity);
            }
        });
    }
}
