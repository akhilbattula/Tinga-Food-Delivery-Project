package com.sytiqhub.tingadelivery;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

public class SyncService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    private Handler handler;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    String latitude,longitude;

    public static final long DEFAULT_SYNC_INTERVAL = 20 * 1000;

    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            //create AsyncTask here

            TingaManager tingaManager = new TingaManager();
            PreferenceManager prefs = new PreferenceManager(SyncService.this);


            tingaManager.UpdateLocation(getApplicationContext(), Integer.parseInt(prefs.getDeliveryDetails().getId()),latitude,longitude);

            //Toast.makeText(SyncService.this, "Service triggered", Toast.LENGTH_SHORT).show();
            handler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };

    //Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(SyncService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SyncService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

        handler = new Handler();
        handler.post(runnableService);

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.i("*****", "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {
                latitude = String.valueOf(loc.getLatitude());
                longitude = String.valueOf(loc.getLongitude());

                TingaManager tingaManager = new TingaManager();
                PreferenceManager prefs = new PreferenceManager(SyncService.this);

                tingaManager.UpdateLocation(getApplicationContext(), Integer.parseInt(prefs.getDeliveryDetails().getId()),latitude,longitude);
/*
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
*/

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }

}

/*
public class SyncService extends Service {

    private Handler handler;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    String latitude,longitude;

    public static final long DEFAULT_SYNC_INTERVAL = 5 * 1000;

    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            //create AsyncTask here

            TingaManager tingaManager = new TingaManager();
            PreferenceManager prefs = new PreferenceManager(SyncService.this);
            tingaManager.UpdateLocation(SyncService.class,prefs.getDeliveryDetails().getId(),);

            Toast.makeText(SyncService.this, "Service triggered", Toast.LENGTH_SHORT).show();
            handler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        handler.post(runnableService);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnableService);
        stopSelf();
        super.onDestroy();
    }

    public void getLatLong() {

        if (ActivityCompat.checkSelfPermission(SyncService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
            .addOnCompleteListener(getApplicationContext(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();

                        latitude = String.valueOf(mLastLocation.getLatitude());
                        longitude = String.valueOf(mLastLocation.getLongitude());

                    } else {
                        Log.w("akhilll", "getLastLocation:exception", task.getException());
                        //showSnackbar(getString(R.string.no_location_detected));
                    }
                }
            });
    }


}*/
