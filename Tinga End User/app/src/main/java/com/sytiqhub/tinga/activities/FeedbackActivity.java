package com.sytiqhub.tinga.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.manager.TingaManager;
import com.sytiqhub.tinga.others.ConnectivityReceiver;

public class FeedbackActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener{

    TextView restaurant_name,delivery_name;
    ImageView delivery_image;
    RatingBar rating_delivery,rating_food;
    Button submit;
    int order_id;
    String d_id;
    String name,r_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        checkConnection();
        final TingaManager tingaManager = new TingaManager();

        restaurant_name = findViewById(R.id.restaurant_name);
        delivery_name = findViewById(R.id.delivery_name);
        delivery_image = findViewById(R.id.delivery_image);
        rating_delivery = findViewById(R.id.rating_delivery);
        rating_food = findViewById(R.id.rating_food);

        submit = findViewById(R.id.submit_button);

        order_id = getIntent().getIntExtra("order_id", 0);
        Log.e("akhillll", String.valueOf(order_id));

        name = getIntent().getExtras().get("name").toString();
        Log.e("akhillll", String.valueOf(name));

        r_id = getIntent().getExtras().get("restaurant_id").toString();
        Log.e("akhillll", String.valueOf(r_id));

        d_id = getIntent().getExtras().get("delivery_id").toString();
        Log.e("akhillll", String.valueOf(d_id));

        delivery_name.setText(name+" has delivered your order");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected()){
                    float food_rating = rating_food.getRating();
                    float delivery_rating = rating_delivery.getRating();

                    tingaManager.UpdateRating(FeedbackActivity.this,order_id, Integer.parseInt(d_id),r_id,food_rating,delivery_rating);
                }else{
                    showSnack(false);
                }

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
