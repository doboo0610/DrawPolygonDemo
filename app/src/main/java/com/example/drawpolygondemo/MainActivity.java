package com.example.drawpolygondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    //Initialize Variable
    GoogleMap gMap;
    CheckBox checkBox;
    SeekBar seekRed,seekGreen,seekBlue;
    Button btDraw,btClear;

    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    int red=0,green=0,blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Assign Variable
        checkBox = findViewById(R.id.check_box);
        seekRed = findViewById(R.id.seek_red);
        seekGreen = findViewById(R.id.seek_green);
        seekBlue = findViewById(R.id.seek_blue);
        btDraw = findViewById(R.id.bt_draw);
        btClear = findViewById(R.id.bt_clear);

        //Initialize SupportMapFragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Get Checkbox State
                if (b) {
                    if (polygon == null) return;
                    //Fill Polygon Color
                    polygon.setFillColor(Color.rgb(red, green, blue));
                } else {
                    //UnFill Polygon Color if unchecked
                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Draw Polyline on Map
                if(polygon != null) polygon.remove();
                //Create PolygonOptions
                PolygonOptions polygonOptions = new PolygonOptions() .addAll(latLngList)
                        .clickable(true);
                polygon = gMap.addPolygon(polygonOptions);
                //Set Polygon stroke Color
                polygon.setFillColor(Color.rgb(red,green,blue));
                if (checkBox.isChecked())
                    //Fill Polygon Color
                    polygon.setFillColor(Color.rgb(red,green,blue));
            }
            });

            btClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Clear All
                    if (polygon != null) polygon.remove();
                    for (Marker marker : markerList) marker.remove();
                    latLngList.clear();
                    markerList.clear();
                    checkBox.setChecked(false);
                    seekRed.setProgress(0);
                    seekGreen.setProgress(0);
                    seekBlue.setProgress(0);
                }
            });

            seekRed.setOnSeekBarChangeListener(this);
            seekGreen.setOnSeekBarChangeListener(this);
            seekBlue.setOnSeekBarChangeListener(this);

        }

        @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Create MarkerOptions
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                //Create Markers
                Marker marker = gMap.addMarker(markerOptions);
                //Add LanLng and Marker
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.seek_red:
                red = i;
                break;
            case R.id.seek_green:
                green = i;
            case R.id.seek_blue:
                blue = i;
                break;
        }
        if (polygon != null) {
            //Set Polygon stroke Color
            polygon.setFillColor(Color.rgb(red, green, blue));
            if (checkBox.isChecked())
                //Fill Polygon Color
                polygon.setFillColor(Color.rgb(red, green, blue));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}