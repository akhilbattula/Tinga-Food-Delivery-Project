package com.sytiqhub.tingadelivery.manager;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sytiqhub.tingadelivery.bean.RestaurantBean;

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

    public static final String KEY_RID = "rid";
    public static final String KEY_NAME = "restaurant_name";
    public static final String KEY_RATING = "rating";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CUISINE = "cuisine";
    public static final String KEY_STATUS = "status";
    public static final String KEY_IMAGEPATH = "image_path";

    public PreferenceManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String rid){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_RID, rid);
        editor.commit();
    }

    public void setLoggedIn(Context context, boolean loggedIn) {
        editor.putBoolean(IS_LOGIN, loggedIn);
        editor.apply();
    }

    public boolean getLoggedStatus(Context context) {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setRestaurantDetails(RestaurantBean restaurantBean){

        editor.putString(KEY_NAME, restaurantBean.getName());
        editor.putString(KEY_RID, restaurantBean.getId());
        editor.putString(KEY_RATING, restaurantBean.getRating());
        editor.putString(KEY_ADDRESS, restaurantBean.getAddress());
        editor.putString(KEY_CITY, restaurantBean.getCity());
        editor.putString(KEY_STATE, restaurantBean.getState());
        editor.putString(KEY_COUNTRY, restaurantBean.getCountry());
        editor.putString(KEY_LATITUDE, restaurantBean.getLatitude());
        editor.putString(KEY_LONGITUDE, restaurantBean.getLongitude());
        editor.putString(KEY_CUISINE, restaurantBean.getCuisine());
        editor.putString(KEY_STATUS, restaurantBean.getStatus());
        editor.putString(KEY_IMAGEPATH, restaurantBean.getImage_path());

        editor.commit();

    }

    public RestaurantBean getRestaurantDetails(){

        RestaurantBean restaurantBean = new RestaurantBean();

        restaurantBean.setId(pref.getString(KEY_RID, null));
        restaurantBean.setName(pref.getString(KEY_NAME, null));
        restaurantBean.setAddress(pref.getString(KEY_ADDRESS, null));
        restaurantBean.setCity(pref.getString(KEY_CITY, null));
        restaurantBean.setState(pref.getString(KEY_STATE, null));
        restaurantBean.setCountry(pref.getString(KEY_COUNTRY, null));
        restaurantBean.setLatitude(pref.getString(KEY_LATITUDE, null));
        restaurantBean.setLongitude(pref.getString(KEY_LONGITUDE, null));
        restaurantBean.setCuisine(pref.getString(KEY_CUISINE, null));
        //restaurantBean.setTimings(pref.getString(KEY_Ti, null));
        restaurantBean.setRating(pref.getString(KEY_RATING, null));
        restaurantBean.setStatus(pref.getString(KEY_STATUS, null));
        restaurantBean.setImage_path(pref.getString(KEY_IMAGEPATH, null));

        return restaurantBean;
    }





}
