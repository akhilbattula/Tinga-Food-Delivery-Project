package com.sytiqhub.tingadelivery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sytiqhub.tingadelivery.bean.RestaurantBean;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

public class LoginActivity extends AppCompatActivity {

    EditText et_username,et_password;
    Button bt_login;
    TextView tv_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        getSupportActionBar().hide();


        final TingaManager tingaManager = new TingaManager();

        final PreferenceManager prefs = new PreferenceManager(this);

        if(prefs.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(intent);
            finish();
        }

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        bt_login = findViewById(R.id.btn_login);

        tv_forgot_password = findViewById(R.id.txt_forgot_password);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(username.isEmpty()){
                    et_username.setError("Enter username");
                }else if (password.isEmpty()){
                    et_password.setError("Enter password");
                }else{

                    tingaManager.login(LoginActivity.this, username, password, new TingaManager.LoginCallBack() {
                        @Override
                        public void onSuccess(RestaurantBean restaurantBean) {

                            prefs.setRestaurantDetails(restaurantBean);

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("id",restaurantBean.getId());
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFail(String msg) {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            et_username.setText("");
                            et_password.setText("");
                        }
                    });

                }

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Forgot Password")
                        .setMessage("Please call at +91 xxxxxxxxxx to reset your password.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
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
