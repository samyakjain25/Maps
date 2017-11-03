/*
package com.example.jain.maps;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.*;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jain.maps.Manifest;
import com.example.jain.maps.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.*;

public class MainActivity extends FragmentActivity implements LocationListener {

    private Geocoder geocoder;
    private GoogleMap googleMap;
    double latitude;
    double longitude;
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity act;
    private boolean LocationAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initilizeMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void initilizeMap() {

        if (googleMap == null) {
            //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            FragmentManager fragmentManager=getSupportFragmentManager();
            SupportMapFragment supportMapFragment=(SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

            googleMap=supportMapFragment.getMap();
            System.out.println("location....xxxx ==>  " + googleMap);




            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {


                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);


                    //get city or place name from latlng
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    System.out.println("cgdddhtfthtdty");
                    try {

                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            System.out.println(latitude + "," + longitude + "," + "=x=x=");

                            String city = addresses.get(0).getAddressLine(0);
                            String state = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);


                            markerOptions.title(city + "," + state + "," + country);
                            System.out.println("xxxxxxxxxx" + city + "," + state + "," + country);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Setting the title for the marker.
                    // This will be displayed on taping the marker


                    // Clears the previously touched position
                    googleMap.clear();

                    // Animating to the touched position
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    googleMap.addMarker(markerOptions);


                }
            });

//
            checkPermission();
            System.out.println(" location =>=>=>123456789 ");
            requestPermission();



            LocationAvailable=false;
            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if(bestProvider!=null) {
                Location location = locationManager.getLastKnownLocation(bestProvider);
                System.out.println(" Kutta kamina" + location);
                if (location != null) {
                    System.out.println(" xmen" + location);
                    onLocationChanged(location);
                    System.out.println(" iron man" + location);
                }
                locationManager.requestLocationUpdates(bestProvider, 20000, 0, (android.location.LocationListener) this);
            }
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry Unable to Load Google Map", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }


   @Override
    public void onMapClick(LatLng latLng) {

       Toast.makeText(getApplicationContext(),"Before Codes",Toast.LENGTH_SHORT).show();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("First Pit Stop")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        Toast.makeText(getApplicationContext(),"After Codes",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        // Placing a marker on the touched position
        // googleMap.addMarker(markerOptions);




    }

    private boolean checkPermission(){

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            LocationAvailable = true;
            return true;
        } else {
            LocationAvailable = false;
            return false;
        }
    }
    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}
*/
