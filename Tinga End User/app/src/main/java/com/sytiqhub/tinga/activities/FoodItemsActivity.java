package com.sytiqhub.tinga.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.CategoryAdapter;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.beans.CategoryBean;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;
import com.sytiqhub.tinga.others.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodItemsActivity extends AppCompatActivity implements OnListFragmentInteractionListener, ConnectivityReceiver.ConnectivityReceiverListener{

    ImageView action_image;
    RecyclerView main_recycler;
    public List<FoodBean> ITEMS = new ArrayList<FoodBean>();
    TextView text_err,tvr_name,tvr_cuisine;
    //SwipeRefreshLayout pullToRefresh;
    String r_id,r_name,r_cuisine;
    DatabaseHandler db;
    // Button go_to_cart;
    List<CategoryBean> lcat=new ArrayList<>();
    public static int cart_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        checkConnection();
        db = new DatabaseHandler(this);
        PreferenceManager prefs = new PreferenceManager(FoodItemsActivity.this);
        action_image = findViewById(R.id.actionbar_image);
        String r_image = prefs.getRestaurantImage();
        Picasso.get().load(r_image).into(action_image);

        //pullToRefresh = findViewById(R.id.pullToRefresh_fooditems);

        r_id = prefs.getRestaurantID();
        r_name = prefs.getRestaurantName();
        r_cuisine = prefs.getRestaurantCuisine();

        //recommended = findViewById(R.id.recycler_recommended);
        main_recycler = (RecyclerView) findViewById(R.id.main_recycler);
        main_recycler.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(main_recycler,false);
        text_err = findViewById(R.id.full_error);
        tvr_name = findViewById(R.id.restaurant_name);
        tvr_cuisine = findViewById(R.id.r_cuisine);

        tvr_name.setText(r_name);
        tvr_cuisine.setText(r_cuisine);

        db.reset();
        addContent(1);

       /* pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2);
            }
        });*/

        Log.d("akhillll",r_id);
        //NestedScrollView nestedSV = (NestedScrollView) findViewById(R.id.nested_sync_scrollview);

        getSupportActionBar().setTitle("");

    }

    public void addContent(int i){

        TingaManager tingaManager = new TingaManager();

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "ALL", i, new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {

                ITEMS.clear();

                ITEMS = detailsMovies;

                if(ITEMS.size()<=0){

                    main_recycler.setVisibility(View.GONE);
                    text_err.setVisibility(View.VISIBLE);

                }else{
                    lcat.clear();
                    main_recycler.setVisibility(View.VISIBLE);
                    text_err.setVisibility(View.GONE);

                    Set<String> categoryset = new HashSet<>();

                    Log.d("list size",String.valueOf(ITEMS.size()));

                    for(int i = 0;i<ITEMS.size();i++){

                        if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Tinga Specials")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Combos")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Family Pack")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Rice and Biryani")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Starters")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Main Course")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Soups")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Breads")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else if(ITEMS.get(i).getSubtype_tag().equalsIgnoreCase("Chinese")){
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }else{
                            categoryset.add(ITEMS.get(i).getSubtype_tag());
                        }
                    }

                    Log.d("categoryset size",String.valueOf(categoryset.size()));

                    List<String> categorylist_temp = new ArrayList<String>(categoryset);
                    List<String> categorylist = new ArrayList<String>();

                    if(categorylist_temp.contains("Tinga Specials")){
                        categorylist.add("Tinga Specials");
                    }
                    if(categorylist_temp.contains("Combos")){
                        categorylist.add("Combos");
                    }
                    if(categorylist_temp.contains("Family Pack")){
                        categorylist.add("Family Pack");
                    }
                    if(categorylist_temp.contains("Rice and Biryani")){
                        categorylist.add("Rice and Biryani");
                    }
                    if(categorylist_temp.contains("Starters")){
                        categorylist.add("Starters");
                    }
                    if(categorylist_temp.contains("Main Course")){
                        categorylist.add("Main Course");
                    }
                    if(categorylist_temp.contains("Soups")){
                        categorylist.add("Soups");
                    }
                    if(categorylist_temp.contains("Breads")){
                        categorylist.add("Breads");
                    }
                    if(categorylist_temp.contains("Chinese")){
                        categorylist.add("Chinese");
                    }

                    Log.d("categorylist size",String.valueOf(categorylist.size()));

                    for(int i=0;i< categorylist.size();i++) {  //2
                        List<FoodBean> lfb = new ArrayList<>();

                        for (int j = 0; j < ITEMS.size(); j++) {  //3
                            if (ITEMS.get(j).getSubtype_tag().equalsIgnoreCase(categorylist.get(i)) &&
                                    ITEMS.get(j).getStatus().equalsIgnoreCase("Available")) {
                                lfb.add(ITEMS.get(j));
                            }
                        }
                        for (int j = 0; j < ITEMS.size(); j++) {  //3
                            if (ITEMS.get(j).getSubtype_tag().equalsIgnoreCase(categorylist.get(i)) &&
                                    !(ITEMS.get(j).getStatus().equalsIgnoreCase("Available"))) {
                                lfb.add(ITEMS.get(j));
                            }
                        }

                        Log.d("lfb size",String.valueOf(lfb.size()));
                        if(lfb.size()>0){
                            Log.d("lfb size",String.valueOf(lfb.size()));
                            lcat.add(new CategoryBean(categorylist.get(i), lfb));
                        }
                    }


                    main_recycler.setLayoutManager(new LinearLayoutManager(FoodItemsActivity.this));
                    CategoryAdapter ca=new CategoryAdapter(FoodItemsActivity.this,lcat);
                    main_recycler.setAdapter(ca);

                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }

        });
        /*if (pullToRefresh.isRefreshing()) {
            pullToRefresh.setRefreshing(false);
        }*/

    }


    @Override
    public void onListFragmentInteraction(FoodBean item) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);

        menuItem.setIcon(Converter.convertLayoutToImage(FoodItemsActivity.this,cart_count,R.drawable.ic_shopping_cart_white_40dp));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        final DatabaseHandler db = new DatabaseHandler(this);
        switch (item.getItemId())
        {
            case R.id.cart_action:
                if(db.getOrderedFoodCount()<1){
                    Toast.makeText(this, "Please add items to cart...", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(FoodItemsActivity.this,CartActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(i);
                }
                break;
            case android.R.id.home:{
                if(db.getOrderedFoodCount()<1){
                    super.onBackPressed();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Discard")
                            .setMessage("Do you want to discard cart?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    db.reset();
                                    cart_count=0;
                                    invalidateOptionsMenu();
                                    Intent i = new Intent(FoodItemsActivity.this,HomeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra("tag","home");
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    startActivity(i);
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
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        final DatabaseHandler db = new DatabaseHandler(FoodItemsActivity.this);
        if(db.getOrderedFoodCount()<1){
            super.onBackPressed();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard")
                    .setMessage("Do you want to discard cart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            db.reset();
                            cart_count=0;
                            invalidateOptionsMenu();
                            Intent i = new Intent(FoodItemsActivity.this,HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("tag","home");
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            startActivity(i);
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            showSnack(true);
        } else {
            showSnack(false);
        }
    }

    private void checkConnection() {

        if (ConnectivityReceiver.isConnected()) {
            //showSnack(true);
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
