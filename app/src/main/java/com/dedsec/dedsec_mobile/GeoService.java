package com.dedsec.dedsec_mobile;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class GeoService extends AppCompatActivity {

    private double lat_ipcft_stgo = -33.4493141;
    private double long_ipcft_stgo = -70.6624069;

    private double lat_parque = -33.4625076;
    private double long_parque = -70.6600286;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_service);

        Configuration.getInstance().load(getApplicationContext(), getDefaultSharedPreferences(getApplicationContext()));

        MapView map = findViewById(R.id.map_service);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint point_ipcft = new GeoPoint(lat_ipcft_stgo, long_ipcft_stgo);
        map.getController().setZoom(16.0);
        map.getController().setCenter(point_ipcft);

        Marker marcador_ipcft = new Marker(map);
        marcador_ipcft.setPosition(point_ipcft);
        marcador_ipcft.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marcador_ipcft.setTitle("IP-CFT Santiago Centro");
        marcador_ipcft.setSnippet("Instituto Profesional");
        map.getOverlays().add(marcador_ipcft);

        GeoPoint point_parque = new GeoPoint(lat_parque, long_parque);
        Marker marcador_parque = new Marker(map);
        marcador_parque.setPosition(point_parque);
        marcador_parque.setAnchor(0.2f, 0.4f);
        marcador_parque.setInfoWindowAnchor(0.2f, 0.0f);
        marcador_parque.setTitle("Parque de pana");
        marcador_parque.setSnippet("Plataforma de lanzamiento al espacio");
        marcador_parque.setIcon(getResources().getDrawable(R.drawable.weed_pic));
        map.getOverlays().add(marcador_parque);

        Polyline line = new Polyline();
        line.addPoint(point_ipcft);
        line.addPoint(point_parque);
        line.setColor(0xFF0000FF);
        line.setWidth(5);
        map.getOverlayManager().add(line);

        Spinner spinner = findViewById(R.id.map_options);
        String[] mapTypes = {"Mapa Normal", "Mapa Transporte", "Mapa Topografico"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mapTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        map.setTileSource(TileSourceFactory.MAPNIK);
                        break;
                    case 1:
                        map.setTileSource(new XYTileSource(
                                "PublicTransport",
                                0, 18, 256, ".png", new String[]{
                                "https://tile.memomaps.de/tilegen/"
                        }
                        ));
                        break;
                    case 2:
                        map.setTileSource(new XYTileSource(
                                "USGS_Satellite",
                                0, 18, 256, ".png", new String[]{
                                        "https://a.tile.opentopomap.org/","https://b.tile.opentopomap.org/", "https://c.tile.opentopomap.org/"
                        }
                        ));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }
}