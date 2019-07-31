package com.sytiqhub.tinga.others;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.activities.FoodItemsActivity;
import com.sytiqhub.tinga.activities.HomeActivity;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.fragments.LocationDialogFragment;
import com.sytiqhub.tinga.fragments.OrderFragment;
import com.sytiqhub.tinga.fragments.OrdersTabFragment;
import com.sytiqhub.tinga.fragments.ProfileFragment;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.List;
import java.util.Locale;

public class ROugh {

/*
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
        private GoogleApiClient mGoogleApiClient;
        private int PLACE_PICKER_REQUEST = 1;
        private FusedLocationProviderClient mFusedLocationClient;
        protected Location mLastLocation;
        ConnectivityReceiver receiver;
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


            progress = new ProgressDialog(com.sytiqhub.tinga.activities.HomeActivity.this);

            String str = getIntent().getExtras().getString("tag", "home");

            if (str.equalsIgnoreCase("home")) {
                loadFragment(new OrderFragment());
            } else if (str.equalsIgnoreCase("profile")) {
                loadFragment(new ProfileFragment());
            } else if (str.equalsIgnoreCase("orders")) {
                loadFragment(new OrdersTabFragment());
            }
            prefs = new PreferenceManager(com.sytiqhub.tinga.activities.HomeActivity.this);

            tingaManager = new TingaManager();
            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        System.out.println("User logged in");
                    } else {
                        Intent i = new Intent(com.sytiqhub.tinga.activities.HomeActivity.this, MainActivity.class);
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

                    AlertDialog.Builder adb = new AlertDialog.Builder(this);

                    adb.setTitle("Select your Location");
                    adb.setMessage("Please update your location with below options");
                    adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getLatLong(dialog);

                        }
                    });
                    adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FragmentActivity activity = (FragmentActivity)(com.sytiqhub.tinga.activities.HomeActivity.this);
                            FragmentManager fm = activity.getSupportFragmentManager();
                            LocationDialogFragment alertDialog = new LocationDialogFragment(com.sytiqhub.tinga.activities.HomeActivity.this, new LocationDialogFragment.OnListFragmentInteractionListener() {
                                @Override
                                public void onListFragmentInteraction(String address) {
                                    tv_location.setText(address);
                                }
                            });
                            alertDialog.show(fm, "fragment_alert");
                            //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = adb.create();
                    alert.show();

                    break;
           */
/* case R.id.navigation_signout:
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Provider List: ",user.getProviders().get(0));
                String provider = user.getProviders().get(0);
                tingaManager.Logout(HomeActivity.this,prefs.getLoginID(),provider);
                break;*//*

                default:
                    return super.onOptionsItemSelected(item);
            }
            return true;
        }

        public void locationDialog() {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setTitle("Select your Location");
            adb.setMessage("Please update your location with below options");
            adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getLatLong(dialog);

                }
            });

            adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FragmentActivity activity = (FragmentActivity)(com.sytiqhub.tinga.activities.HomeActivity.this);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    LocationDialogFragment alertDialog = new LocationDialogFragment(com.sytiqhub.tinga.activities.HomeActivity.this, new LocationDialogFragment.OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(String address) {
                            tv_location.setText(address);
                        }
                    });
                    alertDialog.show(fm, "fragment_alert");
                    //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = adb.create();
            alert.show();

        }

        public void getLatLong(final DialogInterface dialog) {
            progress.setMessage("Fetching your locaton, Please Wait...!");
            progress.show();
            if (ActivityCompat.checkSelfPermission(com.sytiqhub.tinga.activities.HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(com.sytiqhub.tinga.activities.HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                Log.v("akhilll", prefs.getLatitude());
                                Log.v("akhilll", prefs.getLongitude());
                                Geocoder gcd = new Geocoder(com.sytiqhub.tinga.activities.HomeActivity.this,
                                        Locale.getDefault());

                                List<Address> addresses;
                                try {
                                    addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude(), 1);
                                    if (addresses.size() > 0) {
                                        String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        prefs.setLocation(address);
                                        prefs.setLocationDetails(addresses.get(0).getLocality(), addresses.get(0).getAdminArea(), addresses.get(0).getCountryName(), addresses.get(0).getPostalCode());
                                        Toast.makeText(com.sytiqhub.tinga.activities.HomeActivity.this, "Current location has been set to " + address, Toast.LENGTH_SHORT).show();
                                        tv_location.setText(address);
                                    }
                                    Log.v("akhilll", prefs.getLocation());
                                    progress.dismiss();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progress.dismiss();
                                    dialog.dismiss();
                                }

                            } else {
                                Log.w("akhilll", "getLastLocation:exception", task.getException());
                                //showSnackbar(getString(R.string.no_location_detected));
                            }
                        }
                    });

        }

        public void getLatLong() {

            progress.setMessage("Fetching your locaton, Please Wait...!");
            progress.show();
            if (ActivityCompat.checkSelfPermission(com.sytiqhub.tinga.activities.HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(com.sytiqhub.tinga.activities.HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                Geocoder gcd = new Geocoder(com.sytiqhub.tinga.activities.HomeActivity.this,
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

                    MainActivity.displayLocationSettingsRequest(com.sytiqhub.tinga.activities.HomeActivity.this);

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

            Intent i = new Intent(com.sytiqhub.tinga.activities.HomeActivity.this, FoodItemsActivity.class);
            PreferenceManager prefs = new PreferenceManager(com.sytiqhub.tinga.activities.HomeActivity.this);
            prefs.setRestaurantID(item.getId());
            prefs.setRestaurantName(item.getName());
            prefs.setRestaurantImage(item.getImage_path());
            prefs.setRestaurantAddress(item.getAddress());
            prefs.setRestaurantCuisine(item.getCuisine());
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(i);

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
*/


/*


veglayout.setVisibility(View.VISIBLE);
                    nonveglayout.setVisibility(View.VISIBLE);
                    egglayout.setVisibility(View.VISIBLE);
                    //  go_to_cart.setVisibility(View.VISIBLE);

                    List<FoodBean> veglist = new ArrayList<>();
                    List<FoodBean> nonveglist = new ArrayList<>();
                    List<FoodBean> egglist = new ArrayList<>();
                    Log.d("list",String.valueOf(ITEMS.size()));

                    for(int i = 0;i<ITEMS.size();i++){
                        if(ITEMS.get(i).getTypetag().equalsIgnoreCase("VEG")){
                            veglist.add(ITEMS.get(i));
                        }else if(ITEMS.get(i).getTypetag().equalsIgnoreCase("NON-VEG")){
                            nonveglist.add(ITEMS.get(i));
                        }else{
                            egglist.add(ITEMS.get(i));
                        }
                    }

                    Log.d("veg list",String.valueOf(veglist.size()));
                    Log.d("nonveg list",String.valueOf(nonveglist.size()));
                    Log.d("egg list",String.valueOf(egglist.size()));

                    if(veglist.size()<=0){
                        veglayout.setVisibility(View.GONE);
                    }else{
                        veglayout.setVisibility(View.VISIBLE);
                        //veg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL,false));
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        //llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        veg.setLayoutManager(llm);

                        // veg.setLayoutManager(new LinearLayoutManager(FoodItemsActivity.this));
                        adapter = new FoodListAdapter(getApplicationContext(),veglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);
                            }
                        });
                        veg.setAdapter(adapter);
                    }

                    if(nonveglist.size()<=0){
                        nonveglayout.setVisibility(View.GONE);
                    }else{
                        nonveglayout.setVisibility(View.VISIBLE);
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        nonveg.setLayoutManager(llm);
                        //nonveg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL, false));
                        adapter2 = new FoodListAdapter(getApplicationContext(),nonveglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);
                            }
                        });
                        nonveg.setAdapter(adapter2);
                    }

                    if(egglist.size()<=0){
                        egglayout.setVisibility(View.GONE);
                    }else{
                        egglayout.setVisibility(View.VISIBLE);
                        //egg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL,false));
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        egg.setLayoutManager(llm);
                        //egg.setLayoutManager(new LinearLayoutManager(FoodItemsActivity.this));
                        adapter3 = new FoodListAdapter(getApplicationContext(),egglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);
                            }
                        });
                        egg.setAdapter(adapter3);
                    }















    (findViewById(R.id.sign_out)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("Provider List: ",user.getProviders().get(0));
            String provider = user.getProviders().get(0);
            tingaManager.Logout(HomeActivity1.this,prefs.getLoginID(),provider);

        }
    });
*/
/* go_to_cart = findViewById(R.id.btn_go_to_cart);

        go_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(FoodItemsActivity.this,CartActivity.class);
                startActivity(i);

            }
        });*/
    //   fulllayout = findViewById(R.id.layout_full);


       /* tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "VEG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {

                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                veg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                adapter = new FoodListAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                veg.setAdapter(adapter);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "NON-VEG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {
                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                adapter = new FoodListAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                nonveg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                nonveg.setAdapter(adapter);

            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "EGG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {
                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                adapter = new FoodListAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                egg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                egg.setAdapter(adapter);

            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });*/


/*


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sytiqhub.tinga", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
*/

/*
    int totalprice = 0;
    // code to get all contacts in a list view
    public int getTotalPrice() {
        List<OrderFoodBean> contactList = new ArrayList<OrderFoodBean>();
        // Select All Query
        String selectQuery = "SELECT  `total_price`,`quantity` FROM " + TABLE_ORDERS_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                totalprice = totalprice +(Integer.parseInt(cursor.getString(0)) * Integer.parseInt(cursor.getString(1)));
*/
/*
                OrderFoodBean order = new OrderFoodBean();
                order.setFoodId(cursor.getString(1));
                order.setQuantity(Integer.parseInt(cursor.getString(2)));
                order.setTotalPrice(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                contactList.add(order);
*//*

            } while (cursor.moveToNext());
        }
        // return contact list
        return totalprice;
    }
*/


/*
    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog
            Toast.makeText(HomeActivity.this, "Pick a location", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setTitle("Select your Location");
            adb.setMessage("Please update your location with below options");
            adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getLatLong(dialog);

                }
            });
            adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                    try {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        startActivityForResult(builder.build(HomeActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "Google Play Services is not available.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            AlertDialog alert = adb.create();
            alert.show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
*/


/*

package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.activities.FoodDescriptionActivity;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;

import java.util.List;

    public class FoodListAdapter extends RecyclerView.Adapter<com.sytiqhub.tinga.adapters.FoodListAdapter.ViewHolder> {

        private List<FoodBean> mValues;
        private final OnListFragmentInteractionListener mListener;
        private Context context;
        public FoodListAdapter(Context mcontext,List<FoodBean> items, OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
            context = mcontext;
        }
        private static int count=0;

        @NotNull
        @Override
        public com.sytiqhub.tinga.adapters.FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);
            return new com.sytiqhub.tinga.adapters.FoodListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final com.sytiqhub.tinga.adapters.FoodListAdapter.ViewHolder holder, final int position) {

            final DatabaseHandler db = new DatabaseHandler(context);
            if(!(mValues.size()<=0)){

                holder.mItem = mValues.get(position);
                holder.tv_name.setText(mValues.get(position).getName());
                holder.tv_price.setText("Price: "+mValues.get(position).getPrice()+"/-");

*/
/*
            if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                holder.tv_status.setTextColor(Color.parseColor("#006400"));
                holder.tv_status.setText("Available");
            }else{
                // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                holder.tv_status.setText("Not available");


            }
*//*


                if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                    holder.tv_status.setTextColor(Color.parseColor("#006400"));
                    holder.tv_status.setText("Available");
                }else{
                    // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                    holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                    holder.tv_status.setText("Currently not available");

                }

                //Picasso.get().load(mValues.get(position).getImage_path()).into(holder.image);

                if(mValues.get(position).getTypetag().equalsIgnoreCase("VEG")){
                    Picasso.get().load(R.drawable.veg).into(holder.type_image);
                }else if(mValues.get(position).getTypetag().equalsIgnoreCase("NON-VEG")){
                    Picasso.get().load(R.drawable.nonveg).into(holder.type_image);
                }else if(mValues.get(position).getTypetag().equalsIgnoreCase("EGG")){
                    Picasso.get().load(R.drawable.egg).into(holder.type_image);
                }

                holder.tv_count.setText(String.valueOf(count));

                holder.btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int c = Integer.parseInt(holder.tv_count.getText().toString());
                        if(c==6){
                            Toast.makeText(context, "Max quantity is 6...", Toast.LENGTH_SHORT).show();
                            //Snackbar.make((context.findViewById(R.id.layout)),"Max quantity is 6...",Snackbar.LENGTH_SHORT).show();
                        }else if(c<1){
                            c++;

                            holder.btn_remove.setVisibility(View.VISIBLE);
                            holder.tv_count.setVisibility(View.VISIBLE);
                            holder.tv_count.setText(String.valueOf(c));

                            OrderFoodBean orderFoodBean = new OrderFoodBean();
                            orderFoodBean.setFoodId(mValues.get(position).getId());
                            orderFoodBean.setQuantity(Integer.parseInt(holder.tv_count.getText().toString()));
                            orderFoodBean.setTotalPrice(Integer.parseInt(holder.tv_count.getText().toString())*Integer.parseInt(mValues.get(position).getPrice()));
                            //orderFoodBean.setTotalPrice(120);
                            orderFoodBean.setFoodName(holder.tv_name.getText().toString());
                            Log.d("akhilllll foodname",holder.tv_name.getText().toString());
                            //Log.d("akhilllll foodname",f_name);

                            //prefs.setOrderFood(orderFoodBean);
                            db.addOrderFood(orderFoodBean);
                            Toast.makeText(context, "Item added to cart...", Toast.LENGTH_SHORT).show();
                            //Snackbar.make((findViewById(R.id.layout)),"Item added to cart...",Snackbar.LENGTH_SHORT).show();

                        }else{
                            count++;
                            db.updateQuantity(mValues.get(position).getId(),count);
                            holder.tv_count.setText(String.valueOf(count));
                            Log.d("akhilll add",String.valueOf(count));
                        }
                    }
                });

                holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(count == 1){
                            count--;
                            db.deleteOrderFood(mValues.get(position).getId());
                            holder.btn_remove.setVisibility(View.GONE);
                            holder.tv_count.setVisibility(View.GONE);
                        }else if(count > 0){
                            count--;
                            holder.tv_count.setText(String.valueOf(count));
                            Log.d("akhilll remove",String.valueOf(count));
                        }

                    }
                });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {

                            mListener.onListFragmentInteraction(mValues.get(position));

                        }
                    }
                });


            }

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView tv_name;
            public final TextView tv_price;
            public final TextView tv_status;
            public final TextView tv_count;
            public final ImageButton btn_add;
            public final ImageButton btn_remove;
            public FoodBean mItem;
            public ImageView image,type_image;
            public LinearLayout layout;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                tv_name = view.findViewById(R.id.food_name);
                tv_price = view.findViewById(R.id.food_price);
                tv_status = view.findViewById(R.id.food_status);
                //image = view.findViewById(R.id.food_image);
                btn_add = view.findViewById(R.id.ib_add);
                btn_remove = view.findViewById(R.id.ib_remove);
                tv_count = view.findViewById(R.id.tv_count);
                type_image = view.findViewById(R.id.type_image);
                layout = view.findViewById(R.id.food_layout);

            }

        }
    }
*/


}
