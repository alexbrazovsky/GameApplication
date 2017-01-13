package com.example.abrazovsky.myapplication.gameMenu;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.abrazovsky.myapplication.R;
import com.example.abrazovsky.myapplication.database.DatabaseHandler;
import com.example.abrazovsky.myapplication.database.Task;
import com.example.abrazovsky.myapplication.mailSender.GMailSender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("Style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Style", "Can't find style. Error: ", e);
        }
        DatabaseHandler db = new DatabaseHandler(this);
        List<Task> tasks = db.getAllTasks();
        int count_completed_tasks = db.getTasksCount();
        db.close();
        addNewMarker(map,53.900772,30.331154,"Могилев");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(53.900772,30.331154))
                .zoom(15)
                .tilt(30)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (count_completed_tasks>0) {
            for (Task task : tasks) {
                if (task.getChecked() != 0) {
                    addNewMarker(map, task.getLat(), task.getLon(), task.getName());
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.no_markers_on_map), Toast.LENGTH_LONG).show();
        }
    }
    public void addNewMarker (GoogleMap map, double lat, double lon, String title){
        LatLng point = new LatLng(lat, lon);
        map.addMarker(new MarkerOptions().position(point).title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("google_map_marker",111,180))));
    }
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}