package com.sytiqhub.tingadelivery;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

public class AddFoodItemActivity extends AppCompatActivity {

    EditText name,desc,price;
    ImageButton img_food;
    Spinner sp_type,sp_subtype;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        getSupportActionBar().show();

        final TingaManager tingaManager = new TingaManager();
        final PreferenceManager prefs = new PreferenceManager(AddFoodItemActivity.this);

        name = findViewById(R.id.et_item_name);
        price = findViewById(R.id.et_price);
        desc = findViewById(R.id.et_desc);
        //img_food = findViewById(R.id.img_food);
        sp_type = findViewById(R.id.spinner_type);
        sp_subtype = findViewById(R.id.spinner_subtype);
        btn_submit = findViewById(R.id.btn_add_item);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food_name = name.getText().toString();
                String str_price = price.getText().toString();
                String str_desc = desc.getText().toString();
                String type = sp_type.getSelectedItem().toString();
                String subtype = sp_subtype.getSelectedItem().toString();

                if(food_name.isEmpty()){
                    name.setError("Enter food name");
                }else if(str_price.isEmpty()){
                    price.setError("Enter food price");
                }else if(str_desc.isEmpty()){
                    desc.setError("Enter food description");
                }else if(type.equalsIgnoreCase("-- Type --")){
                    TextView errorText = (TextView)sp_type.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//
                    //name.setError("Enter type");
                }else if(subtype.equalsIgnoreCase("-- Sub Type --")){

                    TextView errorText = (TextView)sp_subtype.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//
                    //name.setError("Enter sub type");
                }else{

                    tingaManager.addFoodItem(AddFoodItemActivity.this, food_name, str_desc, str_price, type, subtype, prefs.getRestaurantDetails().getId(), new TingaManager.AddFoodCallBack() {
                        @Override
                        public void onSuccess(String msg) {
                            name.setText("");
                            desc.setText("");
                            price.setText("");
                            sp_type.setSelection(0);
                            sp_subtype.setSelection(0);
                        }

                        @Override
                        public void onFail(String msg) {
                            name.setText("");
                            desc.setText("");
                            price.setText("");
                            sp_type.setSelection(0);
                            sp_subtype.setSelection(0);
                        }
                    });

                }
            }
        });


    }
}
