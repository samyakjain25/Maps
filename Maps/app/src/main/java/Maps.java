/*
package com.example.jain.maps;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements LocationListener
{
    private GoogleMap googleMap;
    Geocoder geocoder;
    private boolean LocationAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            initializeMap();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void initializeMap()
    {
        if(googleMap==null)
        {
            double latitude=17.385044;
            double longitude=78.486671;
            LatLng latLng=new LatLng(latitude,longitude);
          */
/*  android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            SupportMapFragment supportMapFragment=(SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
            googleMap=supportMapFragment.getMap();*//*

            googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            //MarkerOptions markerOptions=new MarkerOptions();
            //markerOptions.position(latLng);
           // markerOptions.title("Hello World");
            //googleMap.addMarker(markerOptions.position(latLng).title("Samyak"));


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
                      //      System.out.println(latitude + "," + longitude + "," + "=x=x=");

                            String city = addresses.get(0).getAddressLine(0);
                            String state = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);


                            markerOptions.title(city + ",\n" + state + ",\n" + country);
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

            LocationAvailable=false;
            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if(bestProvider!=null) {
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    onLocationChanged(location);
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
        initializeMap();
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
}*/
