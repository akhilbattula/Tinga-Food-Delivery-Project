package com.sytiqhub.tingadelivery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sytiqhub.tingadelivery.bean.RestaurantBean;
import com.sytiqhub.tingadelivery.fragments.FoodItemsTabFragment;
import com.sytiqhub.tingadelivery.fragments.OrdersTabFragment;
import com.sytiqhub.tingadelivery.fragments.ProfileFragment;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    PreferenceManager preferenceManager;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final PreferenceManager prefs = new PreferenceManager(this);

        if(!prefs.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), LinearLayout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(intent);
            finish();
        }

        preferenceManager = new PreferenceManager(MainActivity.this);
        Log.d("akhillll",preferenceManager.getRestaurantDetails().getId());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PreferenceManager pref = new PreferenceManager(MainActivity.this);
            Log.d("akhillll",pref.getRestaurantDetails().getId());

            switch (position) {
                case 0:
                    OrdersTabFragment tab1 = new OrdersTabFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("rid",pref.getRestaurantDetails().getId());
                    tab1.setArguments(bundle1);
                    invalidateOptionsMenu();
                    return tab1;
                case 1:
                    FoodItemsTabFragment tab2 = new FoodItemsTabFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("rid",pref.getRestaurantDetails().getId());
                    tab2.setArguments(bundle2);
                    invalidateOptionsMenu();
                    return tab2;
                case 2:
                    ProfileFragment tab3 = new ProfileFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("rid",pref.getRestaurantDetails().getId());
                    tab3.setArguments(bundle3);
                    invalidateOptionsMenu();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/

    SwitchCompat switchAB;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem alertMenuItem = menu.findItem(R.id.myswitch);
        RelativeLayout rootview = (RelativeLayout) alertMenuItem.getActionView();
        final PreferenceManager preferenceManager = new PreferenceManager(MainActivity.this);

        int currentTab = tabLayout.getSelectedTabPosition();
        final MenuItem signoutMenuItem = menu.findItem(R.id.logout);
        final MenuItem addMenuItem = menu.findItem(R.id.add_item);

        addMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent  = new Intent(MainActivity.this, AddFoodItemActivity.class);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
                return true;
            }
        });

        signoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                preferenceManager.setLoggedIn(MainActivity.this,false);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
                finish();
                return true;

            }
        });

        final TingaManager tingaManager = new TingaManager();
        switchAB = (SwitchCompat) rootview.findViewById(R.id.switchForActionBar);

        tingaManager.getRestaurantDetails(MainActivity.this, preferenceManager.getRestaurantDetails().getId(), 1, new TingaManager.RestaurantCallBack() {
            @Override
            public void onSuccess(RestaurantBean restaurantBean) {

                if(restaurantBean.getStatus().equals("Open")){
                    switchAB.setChecked(true);
                }else{
                    switchAB.setChecked(false);
                }

            }

            @Override
            public void onFail(String msg) {

            }
        });


        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    tingaManager.ChangeRestaurantStatus(MainActivity.this, preferenceManager.getRestaurantDetails().getId(), "Open", new TingaManager.RestaurantStatusCallBack() {
                        @Override
                        public void onSuccess(String food_id) {

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                } else {
                    tingaManager.ChangeRestaurantStatus(MainActivity.this, preferenceManager.getRestaurantDetails().getId(), "Closed", new TingaManager.RestaurantStatusCallBack() {
                        @Override
                        public void onSuccess(String food_id) {

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
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

}
