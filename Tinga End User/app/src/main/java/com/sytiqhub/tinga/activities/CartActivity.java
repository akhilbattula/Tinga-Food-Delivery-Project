package com.sytiqhub.tinga.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.CartItemsAdapter;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.fragments.AddressSelectionFragment;
import com.sytiqhub.tinga.manager.DatabaseHandler;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    RecyclerView recycler_items;
    TextView txt_restaurant_name, txt_restaurant_address;
    ImageView img_restaurant_image;
    RadioGroup radioGroup;
    EditText et_address;
    TextView delivery_lat_long, cart_total_price,extra_text;
    LinearLayout ll_select_location;
    CartItemsAdapter cartItemsAdapter;
    Button btn_placeorder, btn_discard;
    List<OrderFoodBean> list = new ArrayList<>();
    ProgressDialog progressBar;
    String uid;
    RadioButton radioButton;
    DatabaseHandler db;
    ImageView image;
    Double latitude,longitude;
    ProgressDialog progress;
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkConnection();

        progress = new ProgressDialog(CartActivity.this);
        db = new DatabaseHandler(CartActivity.this);
        final PreferenceManager prefs = new PreferenceManager(CartActivity.this);
        final TingaManager tinga = new TingaManager();
        recycler_items = findViewById(R.id.cart_items);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        extra_text = findViewById(R.id.extra_text);
        txt_restaurant_name = findViewById(R.id.restaurant_name);
        txt_restaurant_address = findViewById(R.id.restaurant_address);
        img_restaurant_image = findViewById(R.id.restaurant_picture);
        delivery_lat_long = findViewById(R.id.tv_delivery_location);
        radioGroup = findViewById(R.id.payment_group);
        et_address = findViewById(R.id.cart_address);

        et_address.setText(prefs.getLocation());
        et_address.setTextColor(Color.parseColor("#000000"));
        delivery_lat_long.setText(prefs.getLatitude()+", "+prefs.getLongitude());
        extra_text.setPaintFlags(delivery_lat_long.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ll_select_location = findViewById(R.id.select_delivery_location);

        ll_select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity activity = (FragmentActivity)(CartActivity.this);
                FragmentManager fm = activity.getSupportFragmentManager();
                AddressSelectionFragment alertDialog = new AddressSelectionFragment(CartActivity.this, new AddressSelectionFragment.OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(String address) {
                        et_address.setText(address);
                        delivery_lat_long.setText(prefs.getLatitude()+", "+prefs.getLongitude());
                    }
                });
                alertDialog.show(fm, "address_selection");

            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Placing order, Please wait...");
        progressBar.setCancelable(false);

        cart_total_price = findViewById(R.id.cart_total_price);


        btn_placeorder = findViewById(R.id.btn_place_order);
        btn_discard = findViewById(R.id.btn_discard_cart);

        cartItemsAdapter = new CartItemsAdapter(db.getAllContent());
        recycler_items.setLayoutManager(new LinearLayoutManager(this));
        recycler_items.setAdapter(cartItemsAdapter);

        cart_total_price.setText(db.getTotalPrice() + "/-");

        txt_restaurant_name.setText(prefs.getRestaurantName());
        txt_restaurant_address.setText(prefs.getRestaurantAddress());
        Picasso.get().load(Uri.parse(prefs.getRestaurantImage())).into(img_restaurant_image);
//      Picasso.get().load(prefs.getRestaurantImage()).into(img_restaurant_image);

        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected()){
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedId);

                    if (et_address.getText().toString().length() <= 0) {
                        et_address.setError("Please enter delivery address");
                    } else if (selectedId == 0) {
                        Toast.makeText(CartActivity.this, "Please select payment mode...", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.show();
                        Date date = new Date();
                        String strDateFormat = "yyyy-MM-dd";
                        String strTimeFormat = "HH:MM:SS";
                        DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.ENGLISH);
                        DateFormat timeFormat = new SimpleDateFormat(strTimeFormat, Locale.ENGLISH);
                        String formattedDate = dateFormat.format(date);
                        String formattedTime = timeFormat.format(date);
                        String address = et_address.getText().toString();
                        radioButton = findViewById(selectedId);

                        Log.v("food_items json", String.valueOf(db.getTotalPrice()));
                        Log.v("akhilll time", formattedDate);
                        Log.v("akhilll date", formattedTime);

                        final OrderBean orderBean = new OrderBean();
                        orderBean.setRestaurant_id(prefs.getRestaurantID());
                        orderBean.setRestaurant_name(prefs.getRestaurantName());
                        orderBean.setRestaurant_address(prefs.getRestaurantAddress());
                        orderBean.setOrder_date(formattedDate);
                        orderBean.setOrder_time(formattedTime);
                        orderBean.setTotal_price(Integer.parseInt(cart_total_price.getText().toString().split("/")[0]));
                        orderBean.setAddress(address);
                        orderBean.setPayment_mode("Cash On Delivery");
                        orderBean.setLat(prefs.getLatitude());
                        orderBean.setLon(prefs.getLongitude());
                        orderBean.setStatus("Pending");

                        List<OrderFoodBean> orderFoodBeans = db.getAllContent();
                        String food_items = new Gson().toJson(orderFoodBeans);
                        Log.v("food_items json", food_items);
                        Log.v("order bean", orderBean.toString());
                        Log.v("uid", uid);

                        tinga.CheckItemStatusAndPlaceOrder(CartActivity.this, uid, orderBean, food_items, new TingaManager.PlaceOrderCallBack() {
                            @Override
                            public void onSuccess(final int orderid) {

                                /*DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(orderid));

                                TrackingBean bean = new TrackingBean();
                                bean.setOrderid(String.valueOf(orderid));
                                bean.setDelivery("Pending");
                                bean.setDelivery_name("None");
                                bean.setFood_status("Pending");
                                bean.setRestaurant("Pending");
                                bean.setStatus("Pending");

                                mdatabase.setValue(bean);*/

                                /*

                                mdatabase.child("orderid").setValue(orderid);
                                mdatabase.child("status").setValue("Pending");
                                mdatabase.child("restaurant").setValue("Pending");
                                mdatabase.child("food_status").setValue("Pending");
                                mdatabase.child("delivery").setValue("Pending");
                                mdatabase.child("delivery_name").setValue("None");

                                */

                                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }

                                db.reset();

                                Intent i = new Intent(CartActivity.this, OrderStatusActivity.class);
                                i.putExtra("order_id", orderid);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                startActivity(i);


                            }

                            @Override
                            public void onFail(String msg) {
                                Log.d("CheckItem error", msg);
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                                Toast.makeText(CartActivity.this, "Unable to place order. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Log.d("akhillll", orderBean.getOrder_date());
                    }
                }else{
                    showSnack(false);
                }
            }
        });


        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                alert.setTitle("Discard")
                        .setMessage("Do you want to discard cart?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.reset();
                                FoodItemsActivity.cart_count=0;

                                Intent i = new Intent(CartActivity.this, HomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("tag", "home");
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
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK){

            delivery_lat_long.setText(data.getStringExtra("latitude")+", "+data.getStringExtra("longitude"));

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
