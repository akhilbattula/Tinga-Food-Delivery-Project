package com.sytiqhub.tinga.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.activities.HomeActivity;
import com.sytiqhub.tinga.beans.UserBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFillActivity extends Activity {

    EditText tv_fname,tv_lname,tv_mobile,tv_email;
    Button b_continue,b_logout;
    //Button verify;

    TingaManager tingaManager;
    String uid,type;
    UserBean userBean;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    PreferenceManager prefs;
    ImageButton logout;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile_fill);

        tingaManager = new TingaManager();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    System.out.println("User logged in");
                } else {
                    Intent i = new Intent(ProfileFillActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(i);
                    finish();
                }
            }
        };

        prefs = new PreferenceManager(ProfileFillActivity.this);

        uid = getIntent().getExtras().get("uid").toString();
        Log.d("akhillll",uid);
        type = getIntent().getExtras().get("type").toString();
        Log.d("akhillll",type);

        tv_fname = findViewById(R.id.profile_fname);
        tv_lname = findViewById(R.id.profile_lname);
        tv_mobile = findViewById(R.id.profile_mobile);
        tv_email = findViewById(R.id.profile_email);
        b_continue = findViewById(R.id.btn_continue);
        //b_logout = findViewById(R.id.btn_logout);
        //verify = findViewById(R.id.btn_verify);

        logout = findViewById(R.id.profile_fill_logout);
        user = mAuth.getCurrentUser();
        if(prefs.getMobile_verified()==1){

            b_continue.setVisibility(View.VISIBLE);

        }
        /*else{

            b_continue.setVisibility(View.GONE);

        }*/

        if(!(type.equalsIgnoreCase("edit_profile"))){

            //Log.d("Provider List: ",user.getProviders().get(0));
            logout.setVisibility(View.VISIBLE);
            String provider = user.getProviderData().get(1).getProviderId();
            if(provider.contains("facebook.com")){
                Profile profile = Profile.getCurrentProfile();
                tv_fname.setText(profile.getFirstName());
                tv_lname.setText(profile.getLastName());
                tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
                //tv_mobile.setEnabled(false);
                tv_email.setText(mAuth.getCurrentUser().getEmail());
            }else if(provider.contains("google.com")){
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileFillActivity.this);
                tv_fname.setText(acct.getDisplayName());
                tv_lname.setText(acct.getFamilyName());
                tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
                //tv_mobile.setEnabled(false);
                tv_email.setText(acct.getEmail());
            }else{
                tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
                //tv_mobile.setEnabled(false);
                //tv_mobile.setBackgroundColor(Color.parseColor("#dedee6"));
                //verify.setEnabled(false);
            }

        }else{

            logout.setVisibility(View.GONE);
            tv_fname.setText(prefs.getUserDetails().getFname());
            tv_lname.setText(prefs.getUserDetails().getLname());
            tv_mobile.setText(prefs.getUserDetails().getPhone_number());
            tv_email.setText(prefs.getUserDetails().getEmail());
            //tv_mobile.setEnabled(false);
            //verify.setVisibility(View.GONE);
        }

/*
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(prefs.getMobile_verified()==1){

                 Toast.makeText(ProfileFillActivity.this,"Your Mobile number is verified",Toast.LENGTH_SHORT).show();

             }else{

                 final String mobile = tv_mobile.getText().toString();
                 Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
                 Matcher matcher = pattern.matcher(mobile);
                 if(mobile.equals("")||mobile.isEmpty()||TextUtils.isEmpty(mobile)){
                     tv_mobile.setError("Please enter Mobile number");
                 }else if(mobile.length()!=13 && !matcher.matches()){
                     tv_mobile.setError("Please enter mobile number along with '+' and country code (Eg: +91**********)");
                 }else{
                     final String phone = tv_mobile.getText().toString();

                     tingaManager.checkMobile(ProfileFillActivity.this, mobile, new TingaManager.MobileCallBack() {
                         @Override
                         public void onSuccess(int successCode,int verification,String uidArgs) {

                             if(successCode == 0){
                                 Intent i = new Intent(ProfileFillActivity.this,PhoneAuth.class);
                                 i.putExtra("mode","verify");
                                 i.putExtra("mobile",phone);
                                 overridePendingTransition(R.anim.enter, R.anim.exit);
                                 startActivity(i);
                             }else{
                                 if(verification == 1){
                                     if(uidArgs.equalsIgnoreCase(uid)){
                                         Toast.makeText(ProfileFillActivity.this, "Mobile number already exists in Tinga database. Try different number!",Toast.LENGTH_SHORT).show();
                                         tv_mobile.setEnabled(true);
                                     }else{
                                         Toast.makeText(ProfileFillActivity.this, "Mobile number already exists in Tinga database, but not associated with this account.",Toast.LENGTH_SHORT).show();
                                         tv_mobile.setEnabled(true);
                                     }
                                 }
                             }
                         }

                         @Override
                         public void onFail(String msg) {

                         }
                     });

                 }
             }
            }
        });
*/
        final String emailPattern = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        b_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(TextUtils.isEmpty(tv_fname.getText().toString())){

                tv_fname.setError("Please enter First name");

            }else if(TextUtils.isEmpty(tv_lname.getText().toString())){

                tv_lname.setError("Please enter Last name");

            }else if(TextUtils.isEmpty(tv_mobile.getText().toString())){

                tv_mobile.setError("Please Enter your mobile number(Eg: +919*********)");

            }else if(tv_mobile.getText().toString().length()>13 || tv_mobile.getText().toString().length()<13){

                tv_mobile.setError("Please Enter your mobile number in proper format(Eg: +919*********)");

            }else if(TextUtils.isEmpty(tv_email.getText().toString())){

                tv_email.setError("Please enter Email ID");

            }else if (!tv_email.getText().toString().matches(emailPattern)){

                tv_email.setError("Please enter valid Email ID");

            }else{

                String phone = tv_mobile.getText().toString();

                userBean = new UserBean();
                userBean.setUid(uid);
                userBean.setFname(tv_fname.getText().toString());
                userBean.setLname(tv_lname.getText().toString());
                userBean.setMobile_verified(1);
                userBean.setPhone_number(phone);
                userBean.setEmail(tv_email.getText().toString());

                Log.d("uid",userBean.getUid());
                Log.d("fname",userBean.getFname());
                Log.d("lname",userBean.getLname());
                Log.d("mobile",userBean.getPhone_number());
                Log.d("Mobile_verified",String.valueOf(userBean.getMobile_verified()));
                Log.d("email",userBean.getEmail());

                tingaManager.UpdateProfile(ProfileFillActivity.this,userBean,type);

                if(prefs.getMobile_verified()==1){
                    Toast.makeText(ProfileFillActivity.this,"Your Mobile number is verified",Toast.LENGTH_SHORT).show();
                    Log.d("uid activity",uid);

                    String phone1 = tv_mobile.getText().toString();

                    userBean = new UserBean();
                    userBean.setUid(uid);
                    userBean.setFname(tv_fname.getText().toString());
                    userBean.setLname(tv_lname.getText().toString());
                    userBean.setMobile_verified(prefs.getMobile_verified());
                    userBean.setPhone_number(phone1);
                    userBean.setEmail(tv_email.getText().toString());

                    Log.d("uid",userBean.getUid());
                    Log.d("fname",userBean.getFname());
                    Log.d("lname",userBean.getLname());
                    Log.d("mobile",userBean.getPhone_number());
                    Log.d("Mobile_verified",String.valueOf(userBean.getMobile_verified()));
                    Log.d("email",userBean.getEmail());

                    tingaManager.UpdateProfile(ProfileFillActivity.this,userBean,"fill_profile");

                }/*else{

                    Toast.makeText(ProfileFillActivity.this,"Please verify your mobile number.",Toast.LENGTH_SHORT).show();

                }*/

            }
            }
        });

/*
        b_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Provider List: ",user.getProviderId());
                String provider = user.getProviderId();
                tingaManager.Logout(ProfileFillActivity.this,prefs.getLoginID(),provider);
            }
        });
*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Provider List: ",user.getProviderId());
                String provider = user.getProviderData().get(1).getProviderId();
                tingaManager.Logout(ProfileFillActivity.this,prefs.getLoginID(),provider);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getMobile_verified()==1){
            //tv_mobile.setEnabled(false);
            //  tv_mobile.setBackgroundColor(Color.parseColor("#dedee6"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("akhillll","onStoppp");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("akhillll","onDestroyyyy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFillActivity.this);
        builder.setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("Provider List: ",user.getProviderId());
                        String provider = user.getProviderId();
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit().clear();
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit().apply();
                        tingaManager.Logout1(ProfileFillActivity.this,prefs.getLoginID(),provider);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }
}
