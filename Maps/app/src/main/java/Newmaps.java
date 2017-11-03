package com.example.jain.maps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Newmaps extends FragmentActivity implements LocationListener {

    private static final int REQUEST_LOCATION = 1;
    GoogleMap googleMap;
    Geocoder geocoder1;
    double latitude,longitude;
    LatLng latlng;
    GoogleApiClient googleApiClient;
    EditText search;
    Button go;

    //....For Location Enable Pop Up Dialog Box....

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                 Show the dialog by calling startResolutionForResult(),
                                 and check the result in onActivityResult().
                                status.startResolutionForResult(Newmaps.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                             //    Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                     //    The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Getting Google Play availability status
      // enableLoc();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
            View locationButton = ((View)fm.getView().findViewById(1).getParent()).findViewById(2);
             RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
             rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
             rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
             rlp.setMargins(0, 0, 30, 30);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider,20000, 0, this);

            search= (EditText) findViewById(R.id.search);
            go= (Button) findViewById(R.id.go);
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.animate();
                    Geocoder geocoder = new Geocoder(getBaseContext());
                    List<Address> addresses = null;

                    try {
                        String find=search.getText().toString();

                        // Getting a maximum of 3 Address that matches the input
                        // text
                        addresses = geocoder.getFromLocationName(find, 3);
                        if (addresses != null && !addresses.equals(""))
                            search(addresses);

                    } catch (Exception e) {

                    }
                }
            });



            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    //get city or place name from latlng
                    geocoder1 = new Geocoder(getApplicationContext(), Locale.getDefault());
                    String State = null;
                    String City = null;
                    String Country = null;
                    try {

                        List<Address> Addresses = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (Addresses != null && Addresses.size() > 0) {

                            City = Addresses.get(0).getAddressLine(0);
                            State = Addresses.get(0).getAddressLine(1);
                            Country = Addresses.get(0).getAddressLine(2);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Clears the previously touched position
                    googleMap.clear();

                    TextView tvLocation = (TextView) findViewById(R.id.tv_location);


                    // Animating to the touched position
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

                    // Placing a marker on the touched position
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(City + "," + State + "," + Country));

                    tvLocation.setText("Latitude:" + latitude + ", Longitude:" + longitude + "\n" + City + "," + State + ",\n" + Country);

                }
            });

        }

    }

    private void search(List<Address> addresses) {
        Address address = (Address) addresses.get(0);
        double home_long = address.getLongitude();
        double home_lat = address.getLatitude();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

       MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        googleMap.clear();
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
       /* locationTv.setText("Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude());
*/
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        String city = null,state = null,country = null;
        geocoder1=new Geocoder(getApplicationContext(), Locale.getDefault());
        try{
            List<Address> addresses=geocoder1.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses!=null&&addresses.size()>0){
                city = addresses.get(0).getAddressLine(0);
                state = addresses.get(0).getAddressLine(1);
                country = addresses.get(0).getAddressLine(2);
                //googleMap.addMarker(new MarkerOptions().position(latLng).title(city + ","+state +"," + country));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Showing the current location in Google Map
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));

        // Zoom in the Google Map
       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.clear();
        // googleMap.addMarker(new MarkerOptions().position(latLng).title("Samyak"));

        googleMap.addMarker(new MarkerOptions().position(latLng).title(city + ","+state +"," + country));
        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" + latitude + ", Longitude:" + longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

}
