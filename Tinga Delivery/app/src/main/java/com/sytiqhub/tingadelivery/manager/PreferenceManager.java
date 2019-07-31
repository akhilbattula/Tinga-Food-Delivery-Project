package com.sytiqhub.tingadelivery.manager;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sytiqhub.tingadelivery.bean.DeliveryBean;

public class PreferenceManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TingaRestaurantPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_DID = "did";
    public static final String KEY_NAME = "delivery_name";
    public static final String KEY_RATING = "rating";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_STATUS = "status";
    public static final String KEY_IMAGECODE = "image_code";

    public PreferenceManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String did){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_DID, did);
        editor.commit();
    }

    public void setLoggedIn(Context context, boolean loggedIn) {
        editor.putBoolean(IS_LOGIN, loggedIn);
        editor.apply();
    }

    public boolean getLoggedStatus(Context context) {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setDeliveryDetails(DeliveryBean deliveryBean){

        editor.putString(KEY_NAME, deliveryBean.getName());
        editor.putString(KEY_DID, deliveryBean.getId());
        editor.putString(KEY_RATING, deliveryBean.getRating());
        editor.putString(KEY_ADDRESS, deliveryBean.getAddress());
        editor.putString(KEY_EMAIL, deliveryBean.getEmail());
        editor.putString(KEY_LATITUDE, deliveryBean.getLatitude());
        editor.putString(KEY_LONGITUDE, deliveryBean.getLongitude());
        editor.putString(KEY_PHONE_NUMBER, deliveryBean.getPhone_number());
        editor.putString(KEY_STATUS, deliveryBean.getStatus());
        editor.putString(KEY_IMAGECODE, deliveryBean.getImage_code());

        editor.commit();

    }

    public DeliveryBean getDeliveryDetails(){

        DeliveryBean deliveryBean = new DeliveryBean();

        deliveryBean.setId(pref.getString(KEY_DID, null));
        deliveryBean.setName(pref.getString(KEY_NAME, null));
        deliveryBean.setAddress(pref.getString(KEY_ADDRESS, null));
        deliveryBean.setPhone_number(pref.getString(KEY_PHONE_NUMBER, null));
        deliveryBean.setEmail(pref.getString(KEY_EMAIL, null));
        deliveryBean.setLatitude(pref.getString(KEY_LATITUDE, null));
        deliveryBean.setLongitude(pref.getString(KEY_LONGITUDE, null));
        deliveryBean.setRating(pref.getString(KEY_RATING, null));
        deliveryBean.setStatus(pref.getString(KEY_STATUS, null));
        deliveryBean.setImage_code(pref.getString(KEY_IMAGECODE, null));

        return deliveryBean;
    }





}
