package com.sytiqhub.tinga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.auth.ProfileFillActivity;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth mAuth;
    TingaManager tingaManager;
    PreferenceManager prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);
        mAuth = FirebaseAuth.getInstance();
        prefs = new PreferenceManager(SplashActivity.this);
        tingaManager = new TingaManager();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MobileAds.initialize(this, "YOUR_ADMOB_APP_ID");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(ConnectivityReceiver.isConnected()){
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        if (prefs.getProfileStatus() != null) {
                            if (prefs.getProfileStatus().equalsIgnoreCase("half")) {

                                Log.d("mainactivity uid", currentUser.getUid());
                                prefs.setUID(currentUser.getUid());
                                Intent i = new Intent(SplashActivity.this, ProfileFillActivity.class);
                                i.putExtra("uid", currentUser.getUid());
                                i.putExtra("type","login");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);
                                finish();

                            } else if (prefs.getProfileStatus().equalsIgnoreCase("full")) {

                                Log.d("mainactivity uid", currentUser.getUid());
                                prefs.setUID(currentUser.getUid());
                                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                i.putExtra("uid", currentUser.getUid());
                                i.putExtra("tag","home");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);
                                finish();

                            }
                        }
                    }else{
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(i);
                        finish();
                    }
                }else{
                    showSnack(false);
                }


            }
        }, SPLASH_TIME_OUT);
    }

    private void checkConnection() {

        if (ConnectivityReceiver.isConnected()) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(i);
            finish();
        } else {
            showSnack(false);
        }
    }

    public void showSnack(boolean isConnected) {

        if (isConnected) {
            Snackbar.make(findViewById(R.id.parentlayout), "You're back online...", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        } else {
            Snackbar.make(findViewById(R.id.parentlayout), "Internet connection lost...", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }


    }


}




