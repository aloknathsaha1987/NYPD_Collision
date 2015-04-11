package com.aloknath.nypd_collision.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.aloknath.nypd_collision.Objects.CollisionDetail;
import com.aloknath.nypd_collision.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 4/11/2015.
 */
public class CollisionMapActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private List<CollisionDetail> collisionDetails = MainActivity.collisions;

    private GoogleMap mMap;
    private LocationClient mLocationClient;

    private double latitude;
    private double longitude;
    private boolean mShowMap;
    private static final float DEFAULTZOOM = 15;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private ArrayList<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isOnline()) {
            if (servicesOK()) {
                setContentView(R.layout.map_display_layout);

                if (initMap()) {

                    LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        mLocationClient = new LocationClient(CollisionMapActivity.this, CollisionMapActivity.this, CollisionMapActivity.this);
                        mLocationClient.connect();
                        mShowMap = true;
                    }else{
                        Toast.makeText(this, "Location Manager Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if(mMap == null){
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker marker) {

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.windowlayout, null);

                    // Getting the position from the marker
                    LatLng latLng = marker.getPosition();
                    // Getting the Snippet passed
                    String snippet = marker.getSnippet();

                    String[] values = snippet.split(":");

                    TextView personKilled = (TextView) v.findViewById(R.id.no_person_killed);

                    TextView motoristInjured = (TextView) v.findViewById(R.id.number_of_motorist_injured);

                    TextView date = (TextView)v.findViewById(R.id.date);

                    TextView pedistriansInjured = (TextView)v.findViewById(R.id.number_of_pedestrians_injured);

                    personKilled.setText("Number of Person Killed: " + values[0]);
                    motoristInjured.setText("Number of Motorists Injured: " + values[1]);
                    date.setText("Date: " + values[2]);
                    pedistriansInjured.setText("Pedistrians Injured: " + values[3]);


                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });

        }
        return (mMap != null);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location mLocation = mLocationClient.getLastLocation();
        if(mLocation == null){
            Toast.makeText(this, "My Location is not available", Toast.LENGTH_SHORT).show();
        }else {

            try {
                displayLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this,"Disconnected from the location services", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Location" + location.getLatitude() + "," + location.getLongitude();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection the location services Failed", Toast.LENGTH_SHORT).show();
    }

    protected void displayLocation() throws IOException {

        if(collisionDetails.size()>0){
            Log.i("Collisions Array Size: ", String.valueOf(collisionDetails.size()));

            for(CollisionDetail collisionDetail: collisionDetails){
                com.aloknath.nypd_collision.Objects.Location collisionLocation = collisionDetail.getLocation();
                if(collisionLocation.getLatitude().isEmpty() || collisionLocation.getLongitude().isEmpty()){
                    // Do Nothing
                }else{
                    //Set Markers
                    latitude = Double.parseDouble(collisionLocation.getLatitude());
                    longitude = Double.parseDouble(collisionLocation.getLongitude());
                    setMarker(latitude, longitude, collisionDetail);
                }
            }

            // Go to the location of the last Marker

            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULTZOOM);
            mMap.animateCamera(cameraUpdate);
        }

    }

    private void setMarker(double lat, double lng, CollisionDetail collisionDetail) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(lat, lng))
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_CYAN
                ))
                .title("Collision")
                .snippet(collisionDetail.toString());


        Marker marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
        Log.i("Snippet: " , collisionDetail.toString());
        markers.add(marker);

    }

    @Override
    public void onBackPressed()
    {
        removeEverything();
        finish();
    }

    private void removeEverything() {

        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }
}
