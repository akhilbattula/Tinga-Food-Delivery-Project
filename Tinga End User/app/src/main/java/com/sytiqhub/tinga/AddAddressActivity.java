package com.sytiqhub.tinga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.sytiqhub.tinga.activities.CartActivity;
import com.sytiqhub.tinga.activities.LocationActivity;
import com.sytiqhub.tinga.beans.AddressBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

public class AddAddressActivity extends AppCompatActivity {

    EditText et_title,et_address,et_latitude,et_longitude;
    Button btn_submit;
    LinearLayout ll_select_location;
    TextView location;
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        et_title = findViewById(R.id.et_title);
        et_address = findViewById(R.id.et_address);
        et_latitude = findViewById(R.id.et_lat);
        et_longitude = findViewById(R.id.et_long);
        ll_select_location = findViewById(R.id.select_delivery_location);
        location = findViewById(R.id.tv_delivery_location);
        btn_submit = findViewById(R.id.btn_add_address);

        final TingaManager tingaManager  = new TingaManager();
        final PreferenceManager pref = new PreferenceManager(this);

        ll_select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddAddressActivity.this, LocationActivity.class);
                startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = et_title.getText().toString();
                String address = et_address.getText().toString();
                String latitude = et_latitude.getText().toString();
                String longitude = et_longitude.getText().toString();

                if(title.isEmpty()){

                    et_title.setError("Please enter title...");

                }else if(address.isEmpty()){

                    et_address.setError("Please enter full address...");

                }else if(latitude.isEmpty()){

                    Toast.makeText(AddAddressActivity.this, "Please select a location on map...", Toast.LENGTH_SHORT).show();

                }else if(longitude.isEmpty()){

                    Toast.makeText(AddAddressActivity.this, "Please select a location on map...", Toast.LENGTH_SHORT).show();

                }else{

                    AddressBean addressBean = new AddressBean();
                    addressBean.setAddress(address);
                    addressBean.setTitle(title);
                    addressBean.setLocation_lat(latitude);
                    addressBean.setLocation_long(longitude);

                    tingaManager.AddAddress(AddAddressActivity.this, pref.getUID(), addressBean, new TingaManager.AddressCallBack() {
                        @Override
                        public void onSuccess(int id) {

                            Toast.makeText(AddAddressActivity.this, "Address added successfully...", Toast.LENGTH_SHORT).show();
                            et_title.setText("");
                            et_address.setText("");
                            et_latitude.setText("");
                            et_longitude.setText("");
                            location.setText("Click here to select location");

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK){

            et_latitude.setText(data.getStringExtra("latitude"));
            et_longitude.setText(data.getStringExtra("longitude"));

            location.setText(data.getStringExtra("latitude")+", "+data.getStringExtra("longitude"));

        }
    }

}
