package com.sytiqhub.tinga.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.CartItemsAdapter;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;
import com.sytiqhub.tinga.services.MyJobService;

import java.util.List;

public class OrderStatusActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    DatabaseReference mdatabase;
    int order_id;
    ProgressBar progress;
    TextView tv_tracking_status,restaurant_name,restaurant_address,cart_total_price,delivery_address,order_date;
    RecyclerView cart_items;
    CartItemsAdapter cartItemsAdapter;
    Button tracking_delivery;
    ImageView img;
    FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TingaManager tingaManager = new TingaManager();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(OrderStatusActivity.this));
        order_id = getIntent().getIntExtra("order_id", 0);
        //Toast.makeText(this, String.valueOf(order_id), Toast.LENGTH_SHORT).show();
        checkConnection();

        progress = findViewById(R.id.progress);
        tv_tracking_status = findViewById(R.id.tv_tracking_status);
        restaurant_name = findViewById(R.id.restaurant_name);
        restaurant_address = findViewById(R.id.restaurant_address);
        cart_total_price = findViewById(R.id.cart_total_price);
        delivery_address = findViewById(R.id.delivery_address);
        cart_items = findViewById(R.id.cart_items);
        tracking_delivery = findViewById(R.id.tracking_delivery);
        order_date = findViewById(R.id.order_date);
        img = findViewById(R.id.restaurant_picture);

        tv_tracking_status.setText("Your order is pending with restaurant...");

        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("food_status").getValue(String.class).equals("Accepted_Restaurant")){

                    tv_tracking_status.setText("Your order is accepted by the restaurant and is being prepared...");

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Rejected_Restaurant")){

                    tv_tracking_status.setText("Your order is rejected by the restaurant. Please order with other restaurant...");

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Accepted_Delivery")){

                    //"+dataSnapshot.child("delivery_name").getValue()+"
                    tv_tracking_status.setText("Our executive is on the way to collect your order...");

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Rejected_Delivery")){

                    tv_tracking_status.setText("Our executive have rejected your order. Please order with some other restaurant...");

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Ready_Restaurant")){

                    tv_tracking_status.setText("Your order is ready for delivery...");

                    //tracking_delivery.setVisibility(View.GONE);

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Collected_Delivery")){

                    tv_tracking_status.setText("Your order is collected and is on the way to you...");

                    //tracking_delivery.setVisibility(View.VISIBLE);

                }else if(dataSnapshot.child("food_status").getValue(String.class).equals("Delivered")){

                    tv_tracking_status.setText("Your Order is delivered on "+dataSnapshot.child("delivery_time").getValue(String.class));
                    //tracking_delivery.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mdatabase.child(String.valueOf(order_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equals("food_status")){

                    if(dataSnapshot.getValue(String.class).equals("Accepted_Restaurant")){

                        tv_tracking_status.setText("Your order is accepted by the restaurant and is being prepared...");
                        addNotification("Tinga Notification","Your order is accepted by the restaurant and is being prepared...",order_id);

                    }else if(dataSnapshot.getValue(String.class).equals("Rejected_Restaurant")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                tv_tracking_status.setText("Your order is rejected by the restaurant. Please order with other restaurant...");
                                addNotification("Tinga Notification","Your order is rejected by the restaurant due to '"+dataSnapshot.child("order_feedback").getValue(String.class)+"'. Please order with other restaurant...",order_id);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else if(dataSnapshot.getValue(String.class).equals("Ready_Restaurant")){

                        tv_tracking_status.setText("Your order is ready for delivery...");
                        addNotification("Tinga Notification","Your order is ready for delivery...",order_id);

                        tracking_delivery.setVisibility(View.VISIBLE);

                    }else if(dataSnapshot.getValue(String.class).equals("Accepted_Delivery")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tv_tracking_status.setText("Our executive "+dataSnapshot.child("delivery_name").getValue(String.class)+" is on the way to collect your order...");
                                addNotification("Tinga Notification","Our executive "+dataSnapshot.child("delivery_name").getValue(String.class)+" is on the way to collect your order...",order_id);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot.getValue(String.class).equals("Collected_Delivery")){

                        tv_tracking_status.setText("Your order is collected and is on the way to you...");
                        tracking_delivery.setVisibility(View.VISIBLE);
                        addNotification("Tinga Notification","Your order is collected and is on the way to you...",order_id);

                    }else if(dataSnapshot.getValue(String.class).equals("Delivered")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tv_tracking_status.setText("Your Order is delivered on "+dataSnapshot.child("delivery_time").getValue(String.class));
                                tracking_delivery.setVisibility(View.GONE);
                                addNotification("Tinga Notification","Your Order is delivered on "+dataSnapshot.child("delivery_time").getValue(String.class),order_id);
                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tracking_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tingaManager.getOrderDetails(OrderStatusActivity.this, order_id, new TingaManager.OrderDetailsCallBack() {
            @Override
            public void onSuccess(OrderBean orderbean, List<OrderFoodBean> detailsMovies) {

                cartItemsAdapter = new CartItemsAdapter(detailsMovies);
                cart_items.setLayoutManager(new LinearLayoutManager(OrderStatusActivity.this));
                cart_items.setAdapter(cartItemsAdapter);

                cart_total_price.setText(orderbean.getTotal_price()+"/-");
                order_date.setText("Order Date: "+orderbean.getOrder_date()+" "+orderbean.getOrder_time());

                restaurant_name.setText(orderbean.getRestaurant_name());
                restaurant_address.setText(orderbean.getRestaurant_address());

                Picasso.get().load(Uri.parse(orderbean.getRestaurant_image())).into(img);

                delivery_address.setText(orderbean.getAddress());

            }

            @Override
            public void onFail(String msg) {

            }
        });


        Log.e("akhillll", String.valueOf(order_id));


    }

    private void addNotification(String title,String content,int order_id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent notificationIntent = new Intent(this, OrderStatusActivity.class);
        notificationIntent.putExtra("order_id",order_id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scheduleJob();

        Intent i = new Intent(OrderStatusActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("tag","orders");
        overridePendingTransition(R.anim.enter, R.anim.exit);
        startActivity(i);
        finish();

    }

    public void scheduleJob(){

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putInt("order_id", order_id);
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobService.class)
                // uniquely identifies the job
                .setTag("tinga-order-status"+order_id)
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 60))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatcher.cancel("tinga-order-status"+order_id);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dispatcher.cancel("tinga-order-status"+order_id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduleJob();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduleJob();

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
