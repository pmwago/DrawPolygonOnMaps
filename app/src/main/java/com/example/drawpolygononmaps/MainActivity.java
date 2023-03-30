package com.example.drawpolygononmaps;

import androidx.annotation.NonNull;
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
private CheckBox checkBox;
private SeekBar seekRed,seekGreen,seekBlue;
private Button btnDraw,btnClear;
GoogleMap map;

Polygon polygon=null;
List<LatLng> latLngList=new ArrayList<>();
List<Marker> markerList=new ArrayList<>();

int red=0,green=0,blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox=findViewById(R.id.check_box);
        seekRed=findViewById(R.id.seek_red);
        seekBlue=findViewById(R.id.seek_blue);
        seekGreen=findViewById(R.id.seek_green);
        btnClear=findViewById(R.id.btn_clear);
        btnDraw=findViewById(R.id.btn_draw);


        SupportMapFragment fragment=(SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        fragment.getMapAsync(this);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //get checkbox state
            if (isChecked){
                if (polygon==null) return;

                //fill polygon with selected  color
                polygon.setFillColor(Color.rgb(red,green,blue));
            }else{
                //Unfill polygon color if checkbox is unchecked
                polygon.setFillColor(Color.TRANSPARENT);
            }
        });
        btnDraw.setOnClickListener(v -> {
            //draw polyline on map
            if(polygon!=null)polygon.remove();

            PolygonOptions polygonOptions=new PolygonOptions().addAll(latLngList)
                    .clickable(true);
            polygon=map.addPolygon(polygonOptions);


            polygon.setStrokeColor(Color.rgb(red,green,blue));
            if (checkBox.isChecked())
                polygon.setFillColor(Color.rgb(red,green,blue));
        });
        btnClear.setOnClickListener(v -> {
            if (polygon!=null) polygon.remove();
            for (Marker marker : markerList) marker.remove();
            latLngList.clear();
            markerList.clear();
            checkBox.setChecked(false);
            seekRed.setProgress(0);
            seekGreen.setProgress(0);
            seekBlue.setProgress(0);

        });
        seekRed.setOnSeekBarChangeListener(this);
        seekGreen.setOnSeekBarChangeListener(this);
        seekBlue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map=googleMap;
        map.setOnMapClickListener(latLng -> {

            MarkerOptions markerOptions=new MarkerOptions().position(latLng);

            Marker marker=map.addMarker(markerOptions);

            latLngList.add(latLng);
            markerList.add(marker);
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seek_red:
                red=progress;
                break;
            case R.id.seek_green:
                green=progress;
                break;
            case R.id.seek_blue:
                blue=progress;
                break;
        }
        if (polygon!=null) {

            polygon.setStrokeColor(Color.rgb(red, green, blue));
            if (checkBox.isChecked())
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