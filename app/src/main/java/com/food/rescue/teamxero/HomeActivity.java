package com.food.rescue.teamxero;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationProvider.LocationCallback {

    private static final String TAG = "HomeActivity";
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private MenuItem mSearchMenuItem;
    private List<Provider> mProviderList;
    private LocationProvider mLocationProvider;

    //com.google.android.gms_9.6.83_(876-133155058)-9683876_minAPI19(x86)(320dpi)_apkmirror.com
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        mLocationProvider = new LocationProvider(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        // Add a marker in Sydney and move the camera
        //37.3775641,-121.9314487
        LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
        LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
        LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
        LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
        LatLng PERTH = new LatLng(-31.952854, 115.857342);



//        mMap.addMarker(new MarkerOptions()
//                .position(SYDNEY)
//                .title("Sydney")
//                .snippet("Population: 4,627,300")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cast_dark))
//                .infoWindowAnchor(0.5f, 0.5f));
//        mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .title("Melbourne")
//                .snippet("Population: 4,137,400")
//                .draggable(true));
//        mMap.addMarker(new MarkerOptions()
//                .position(PERTH)
//                .title("Perth")
//                .snippet("Population: 1,738,800"));
//        mMap.addMarker(new MarkerOptions()
//                .position(ADELAIDE)
//                .title("Adelaide")
//                .snippet("Population: 1,213,000"));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 0.5f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_place_pick:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try{
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

                }catch (Exception ex){
                    Log.d(TAG, "Exception in Place picker "+ ex);
                    Toast.makeText(this, "Error fetching place, please retry", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.refresh_view:
                // Search new items
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == Activity.RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, this);
                Log.d(TAG, "Lat long is "+ place.getLatLng());
                updateCurrentLocation(place.getLatLng());

            }
        }
    }

    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        updateCurrentLocation(latLng);
    }

    private void updateCurrentLocation(LatLng latLng){
        Log.d(TAG, "Current location is "+ latLng.latitude + "  " + latLng.longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        mMap.animateCamera(cameraUpdate);
    }

    private class FetchProducts extends AsyncTask<String, Void, List<Provider>> {

        @Override
        protected List<Provider> doInBackground(String... params) {
            // Make call to rest api
            return null;
        }

        @Override
        protected void onPostExecute(List<Provider> providers) {
            super.onPostExecute(providers);
            mProviderList = providers;
            //hideProgressDialog();
            if(mProviderList.size() > 0){
                // populate the MAP
            }else {
                String toastMsg = "Sorry! no results, try another location";

            }
        }
    }

}
