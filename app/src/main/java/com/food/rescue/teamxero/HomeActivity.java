package com.food.rescue.teamxero;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.food.rescue.teamxero.pojo.SearchTerm;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMarkerClickListener, OnMapReadyCallback, LocationProvider.LocationCallback {

    private static final String TAG = "HomeActivity";
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private MenuItem mSearchMenuItem;
    private List<Provider> mProviderList;
    private LocationProvider mLocationProvider;
    private Marker mCurrentLocation;
    private FloatingActionButton notifyMe;
    private SeekBar radiusSeekbar;
    private TextView radiusText;
    private ProgressDialog mProgressDialog;
    private int radiusValue;

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
        notifyMe = (FloatingActionButton) findViewById(R.id.notify_me);
        notifyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng newLatlng = new LatLng(mCurrentLocation.getPosition().latitude, mCurrentLocation.getPosition().longitude);
                new NotifyEnable().execute(newLatlng);
                FragmentManager fragmentManager = getSupportFragmentManager();
                ConfirmFragment dialog = new ConfirmFragment();
                dialog.show(fragmentManager, "DialogWelcome");
            }
        });

        radiusSeekbar = (SeekBar) findViewById(R.id.seekbar);
        radiusText = (TextView) findViewById(R.id.radius);
        setupSeekbarListeners();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
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
                if(mCurrentLocation != null){
                    updateCurrentLocation(mCurrentLocation.getPosition(), radiusValue);
                }
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
                updateCurrentLocation(place.getLatLng(), radiusValue);

            }
        }
    }

    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        if(mCurrentLocation == null){
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);
            radiusValue = 1;
            updateCurrentLocation(latLng, radiusValue);
        }
    }

    private void updateCurrentLocation(LatLng latLng, int radius){
        Log.d(TAG, "Current location is "+ latLng.latitude + "  " + latLng.longitude);
        mMap.clear();
        showProgressDialog();
        if(mCurrentLocation != null){
            mCurrentLocation.remove();
        }
        mCurrentLocation = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(cameraUpdate);

        new FetchProducts().execute(new SearchTerm(latLng.latitude, latLng.longitude, radius));
    }

    private void updateMarkers(){
        for(Provider provider : mProviderList){
            LatLng latLng = new LatLng(provider.getLocation().getLatitude(), provider.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(provider.getFirstName() + " " + provider.getLastName())
                .snippet(provider.getDescription()));
            marker.setTag(provider);
        }
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Retrieve the data from the marker.
                Provider provider = (Provider) marker.getTag();
                if(provider != null){
                    Log.d(TAG, "Provider ID ="+ provider.getFirstName());
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    String message = provider.getId();
                    intent.putExtra("provider_id", message);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
//        Provider provider = (Provider) marker.getTag();
//        if(provider != null){
//            Log.d(TAG, "Provider ID ="+ provider.getFirstName());
//            Intent intent = new Intent(this, DetailsActivity.class);
//            String message = provider.getId();
//            intent.putExtra("provider_id", message);
//            startActivity(intent);
//        }

        return false;
    }

    private class FetchProducts extends AsyncTask<SearchTerm, Void, List<Provider>>{

        @Override
        protected List<Provider> doInBackground(SearchTerm... searchTerms) {
            // Make call to rest api
            return ProviderInfo.getsProviderInfo(getApplicationContext()).fetchProducts(searchTerms[0]);
        }

        @Override
        protected void onPostExecute(List<Provider> providers) {
            super.onPostExecute(providers);
            mProviderList = providers;
            updateMarkers();
            hideProgressDialog();
            if(mProviderList.size() > 0){
                // populate the MAP
            }else {
                String toastMsg = "Sorry! no results, try another location";

            }
        }
    }

    private class NotifyEnable extends AsyncTask<LatLng, Void, Boolean>{

        @Override
        protected Boolean doInBackground(LatLng... latLngs) {
            ProviderInfo.getsProviderInfo(getApplicationContext()).sendNotifyMeEnable(latLngs[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private void setupSeekbarListeners(){
        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radiusText.setText("Get places to eat in "+ progress + " miles");
                radiusValue = progress;
                if(mCurrentLocation != null){
                    updateCurrentLocation(mCurrentLocation.getPosition(), radiusValue);
                }
            }
        });
    }

    private void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Finding food...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }
}
