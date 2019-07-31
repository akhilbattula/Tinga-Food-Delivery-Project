package com.sytiqhub.tinga.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.Places;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener2;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.fragments.LocationDialogFragment;
import com.sytiqhub.tinga.fragments.OrderFragment;
import com.sytiqhub.tinga.fragments.OrdersTabFragment;
import com.sytiqhub.tinga.fragments.ProfileFragment;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.AppController;
import com.sytiqhub.tinga.others.ConnectivityReceiver;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, OrderFragment.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextMessage;
    FirebaseAuth mAuth;
    TextView tv_address, tv_location;
    private FirebaseAuth.AuthStateListener mAuthListener;
    PreferenceManager prefs;
    TingaManager tingaManager;
    ProgressDialog progress;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

    ImageView img_nointernet;
    FrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img_nointernet = findViewById(R.id.img_no_internet);
        framelayout = findViewById(R.id.fragment_container);
        checkConnection();

        progress = new ProgressDialog(HomeActivity.this);

        String str = getIntent().getExtras().getString("tag", "home");

        if (str.equalsIgnoreCase("home")) {
            loadFragment(new OrderFragment());
        } else if (str.equalsIgnoreCase("profile")) {
            loadFragment(new ProfileFragment());
        } else if (str.equalsIgnoreCase("orders")) {
            loadFragment(new OrdersTabFragment());
        }
        prefs = new PreferenceManager(HomeActivity.this);

        tingaManager = new TingaManager();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    System.out.println("User logged in");
                } else {
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(i);
                    finish();
                }
            }
        };

        checkFirstRun();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        tv_location = findViewById(R.id.location_address);
        tv_location.setPaintFlags(tv_location.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationDialog();

            }
        });
    }


    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new OrderFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_myorders:
                    fragment = new OrdersTabFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_location:

                FragmentActivity activity = (FragmentActivity)(HomeActivity.this);
                FragmentManager fm = activity.getSupportFragmentManager();
                LocationDialogFragment alertDialog = new LocationDialogFragment(HomeActivity.this, new LocationDialogFragment.OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(String address) {
                        tv_location.setText(address);
                    }
                });
                alertDialog.show(fm, "fragment_alert");

                break;
           /* case R.id.navigation_signout:
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Provider List: ",user.getProviders().get(0));
                String provider = user.getProviders().get(0);
                tingaManager.Logout(HomeActivity.this,prefs.getLoginID(),provider);
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void locationDialog() {

        FragmentActivity activity = (FragmentActivity)(HomeActivity.this);
        FragmentManager fm = activity.getSupportFragmentManager();
        LocationDialogFragment alertDialog = new LocationDialogFragment(HomeActivity.this, new LocationDialogFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(String address) {
                tv_location.setText(address);
            }
        });
        alertDialog.show(fm, "location_dialog");


    }


    public void getLatLong() {

        progress.setMessage("Fetching your locaton, Please Wait...!");
        progress.show();
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            prefs.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                            prefs.setLongitude(String.valueOf(mLastLocation.getLongitude()));

                            prefs.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                            prefs.setLongitude(String.valueOf(mLastLocation.getLongitude()));
                            Log.v("akhilll", prefs.getLatitude());
                            Log.v("akhilll", prefs.getLongitude());
                            Geocoder gcd = new Geocoder(HomeActivity.this,
                                    Locale.getDefault());

                            List<Address> addresses;
                            try {
                                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude(), 1);
                                if (addresses.size() > 0) {
                                    String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    prefs.setLocation(address);
                                    prefs.setLocationDetails(addresses.get(0).getLocality(), addresses.get(0).getAdminArea(), addresses.get(0).getCountryName(), addresses.get(0).getPostalCode());
                                    //Toast.makeText(HomeActivity.this, "Current location has been set to " + address, Toast.LENGTH_SHORT).show();
                                    tv_location.setText(address);
                                }
                                Log.v("akhilll", prefs.getLocation());
                                progress.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }

                        } else {
                            Log.w("akhilll", "getLastLocation:exception", task.getException());
                            //showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage() + "", Toast.LENGTH_SHORT).show();
    }


    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getLatLong();
                }
            });

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();


        mAuth.addAuthStateListener(mAuthListener);
       //Toast.makeText(HomeActivity.this, "Current location has been set to "+address, Toast.LENGTH_SHORT).show();
        tv_location.setText(prefs.getLocation());

        //mGoogleApiClient.connect();
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

                MainActivity.displayLocationSettingsRequest(HomeActivity.this);

                checkFirstRun();

            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
       // mGoogleApiClient.disconnect();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Exit")
                .setMessage("Do you want exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .commit();
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alert.show();
    }

    @Override
    public void onListFragmentInteraction(RestaurantBean item) {

        if(item.getStatus().equalsIgnoreCase("Open")){
            Intent i = new Intent(HomeActivity.this,FoodItemsActivity.class);
            PreferenceManager prefs = new PreferenceManager(HomeActivity.this);
            prefs.setRestaurantID(item.getId());
            prefs.setRestaurantName(item.getName());
            prefs.setRestaurantImage(item.getImage_path());
            prefs.setRestaurantAddress(item.getAddress());
            prefs.setRestaurantCuisine(item.getCuisine());
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(i);
        }else{
            Toast.makeText(this, "This restaurant currently not accepting orders online.", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            img_nointernet.setVisibility(View.GONE);
            framelayout.setVisibility(View.VISIBLE);
            showSnack(true);
        }else{
            img_nointernet.setVisibility(View.VISIBLE);
            framelayout.setVisibility(View.GONE);
            showSnack(false);
        }
    }

    private void checkConnection() {

        if(ConnectivityReceiver.isConnected()){
            img_nointernet.setVisibility(View.GONE);
            framelayout.setVisibility(View.VISIBLE);
            //showSnack(true);
        }else{
            img_nointernet.setVisibility(View.VISIBLE);
            framelayout.setVisibility(View.GONE);
            showSnack(false);
        }
    }

    public void showSnack(boolean isConnected){

        if(isConnected){
            Snackbar.make(findViewById(R.id.parentlayout), "You're back online...", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }else{
            Snackbar.make(findViewById(R.id.parentlayout), "Internet connection lost...", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }
    }



}
