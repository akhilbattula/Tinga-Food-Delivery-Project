package com.sytiqhub.tinga.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sytiqhub.tinga.manager.AppConfig;
import com.sytiqhub.tinga.others.AppController;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    private String mVerificationId;

    EditText phone,otp;
    Button b_send_otp,b_signin;
    private FirebaseAuth mAuth;
    TingaManager tingaManager;
    String mode="login",mobile;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        requestQueue = Volley.newRequestQueue(this);

        mode = getIntent().getExtras().getString("mode");
        mobile = getIntent().getExtras().getString("mobile");

        mAuth = FirebaseAuth.getInstance();

        tingaManager = new TingaManager();

        phone = findViewById(R.id.edit_phone);
        otp = findViewById(R.id.edit_otp);

        if(mode.equalsIgnoreCase("verify")){
            phone.setText(mobile);
            phone.setEnabled(false);
            otp.setEnabled(false);
            //phone.setBackgroundColor(Color.parseColor("#dedee6"));
        }

        b_send_otp = findViewById(R.id.button_send_otp);
        b_signin = findViewById(R.id.button_signin);

        b_signin.setVisibility(View.GONE);

        b_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String code = otp.getText().toString().trim();
                final String str_phno = phone.getText().toString();
                if(str_phno.isEmpty()){

                    phone.setError("Please Enter your mobile number(Eg: +919*********)");

                }else if(str_phno.length()<13||str_phno.length()>13){

                    phone.setError("Please enter mobile number along with '+' and country code (Eg: +91**********)");

                }else{
                    register(mAuth.getCurrentUser().getUid(),str_phno.substring(1));
                }

            }
        });

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }

                //verifying the code entered manually
                Log.d("2: ",code);
                try {
                    confirmOtp(mAuth.getCurrentUser().getUid(),otp.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //this method will register the user
    private void register(final String uid, final String phone) {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "OTP", "Please wait...", false, false);

        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                            Log.d("akhillll",response);
                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            if(jsonResponse.getBoolean("return")){
                                //Asking user to confirm otp
                                otp.setEnabled(true);
                                b_signin.setVisibility(View.VISIBLE);
                            }else{
                                //If not successful user may already have registered
                                Toast.makeText(PhoneAuth.this, "Username or Phone number already registered", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(PhoneAuth.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("user", uid);
                params.put("phone", phone);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    //This method would confirm the otp
    private void confirmOtp(final String uid,final String otp_str) throws JSONException {

        //Displaying a progressbar
        final ProgressDialog loading = ProgressDialog.show(PhoneAuth.this, "Authenticating", "Please wait while we check the entered code", false,false);

        //Creating an string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.CONFIRM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);

                            //If it is success
                            if(jsonResponse.getString(AppConfig.TAG_RESPONSE1).equalsIgnoreCase("1")){
                                //dismissing the progressbar
                                loading.dismiss();

                                //Starting a new activity
                                PreferenceManager prefs = new PreferenceManager(PhoneAuth.this);

                                if(mode.equalsIgnoreCase("login")){
                                    prefs.setMobile_verified(1);
                                    tingaManager.checkLogin(PhoneAuth.this,uid);
                                }else if(mode.equalsIgnoreCase("verify")){
                                    prefs.setMobile_verified(1);
                                    finish();
                                }
                            }else{

                                String message = jsonResponse.getString("message");
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(PhoneAuth.this,message+" Please Try Again",Toast.LENGTH_LONG).show();
                                try {
                                    //Asking user to enter otp again
                                    confirmOtp(uid,otp.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(PhoneAuth.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("user", uid);
                params.put("otp", otp_str);
                return params;
            }
        };

        //Adding the request to the queue
        requestQueue.add(stringRequest);
    }



    @Override
    protected void onPause() {
        super.onPause();
        AppController app = new AppController();
        app.cancelPendingRequests("req_login");
        app.cancelPendingRequests("signin_user");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController app = new AppController();
        app.cancelPendingRequests("req_login");
        app.cancelPendingRequests("signin_user");


    }

}
