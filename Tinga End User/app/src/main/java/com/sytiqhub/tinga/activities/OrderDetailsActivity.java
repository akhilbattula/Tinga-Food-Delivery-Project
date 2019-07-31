package com.sytiqhub.tinga.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.CartItemsAdapter;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    DatabaseReference mdatabase;
    int order_id;

    TextView tv_tracking_status, restaurant_name, restaurant_address, cart_total_price, delivery_address, order_date;
    RecyclerView cart_items;
    CartItemsAdapter cartItemsAdapter;
    Button tracking_delivery;
    ImageView img;
    LinearLayout ll_giverating,ll_viewrating;
    RatingBar rating_food,rating_delivery;
    CardView card_rating;
    Button btn_rating;
    String name,r_id;
    String d_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkConnection();

        TingaManager tingaManager = new TingaManager();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders");

        order_id = getIntent().getIntExtra("order_id", 0);
        Log.e("akhillll", String.valueOf(order_id));
        //Toast.makeText(this, String.valueOf(order_id), Toast.LENGTH_SHORT).show();

        tv_tracking_status = findViewById(R.id.tv_tracking_status);
        restaurant_name = findViewById(R.id.restaurant_name);
        restaurant_address = findViewById(R.id.restaurant_address);
        cart_total_price = findViewById(R.id.cart_total_price);
        delivery_address = findViewById(R.id.delivery_address);
        cart_items = findViewById(R.id.cart_items);
        tracking_delivery = findViewById(R.id.tracking_delivery);
        order_date = findViewById(R.id.order_date);
        img = findViewById(R.id.restaurant_picture);

        card_rating = findViewById(R.id.card_rating);
        ll_giverating = findViewById(R.id.ll_give_rating);
        ll_viewrating = findViewById(R.id.ll_view_rating);

        rating_food = findViewById(R.id.rating_food);
        rating_delivery = findViewById(R.id.rating_delivery);

        btn_rating = findViewById(R.id.rating_button);

        card_rating.setVisibility(View.GONE);

        //tv_tracking_status.setText("Your order is pending with restaurant...");

      /*  mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("food_status").getValue(String.class).equals("Delivered")) {

                    name = dataSnapshot.child("delivery_name").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        tingaManager.getOrderDetails(OrderDetailsActivity.this, order_id, new TingaManager.OrderDetailsCallBack() {
            @Override
            public void onSuccess(OrderBean orderbean, List<OrderFoodBean> detailsMovies) {

                cartItemsAdapter = new CartItemsAdapter(detailsMovies);
                cart_items.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
                cart_items.setAdapter(cartItemsAdapter);

                cart_total_price.setText(orderbean.getTotal_price() + "/-");
                order_date.setText("Order Date: " + orderbean.getOrder_date()+" "+orderbean.getOrder_time());

                restaurant_name.setText(orderbean.getRestaurant_name());
                restaurant_address.setText(orderbean.getRestaurant_address());

                Picasso.get().load(Uri.parse(orderbean.getRestaurant_image())).into(img);

                delivery_address.setText(orderbean.getAddress());

                r_id = orderbean.getRestaurant_id();
                d_id = orderbean.getDelivery_id();
                name = orderbean.getDelivery_name();
                if(orderbean.getStatus().equalsIgnoreCase("Completed")){
                    tv_tracking_status.setText("Your Order is delivered on " + orderbean.getDelivery_time());

                    card_rating.setVisibility(View.VISIBLE);
                    Log.d("akhillll", String.valueOf(orderbean.getFood_rating().length()));
                    if(orderbean.getFood_rating()==null ||orderbean.getFood_rating().length() == 4 ||orderbean.getFood_rating().equals("null")){
                        ll_giverating.setVisibility(View.VISIBLE);
                        ll_viewrating.setVisibility(View.GONE);
                    }else{
                        ll_giverating.setVisibility(View.GONE);
                        ll_viewrating.setVisibility(View.VISIBLE);

                        rating_food.setRating(Float.parseFloat(orderbean.getFood_rating()));
                        rating_delivery.setRating(Float.parseFloat(orderbean.getDelivery_rating()));
                    }

                }else if(orderbean.getStatus().equalsIgnoreCase("Rejected")){

                    tv_tracking_status.setText("Your Order had been rejected.");
                    card_rating.setVisibility(View.GONE);

                }else if(orderbean.getStatus().equalsIgnoreCase("Rejected")){

                    tv_tracking_status.setText("Your Order had been rejected.");
                    card_rating.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFail(String msg) {

            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetailsActivity.this, FeedbackActivity.class);
                i.putExtra("order_id",order_id);
                i.putExtra("name",name);
                i.putExtra("delivery_id",d_id);
                i.putExtra("restaurant_id",r_id);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(i);
                finish();
            }
        });
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