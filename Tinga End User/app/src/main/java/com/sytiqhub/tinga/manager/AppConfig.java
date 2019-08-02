package com.sytiqhub.tinga.manager;

import android.util.Log;

import static com.sytiqhub.tinga.others.AppController.URL;

public class AppConfig {

    public static String URL = "YOUR_SERVER_URL_HERE";

    public static String URL_CHECK_USER = URL+"app/checkuser.php";

    public static String URL_ADD_USER = URL+"app/adduser.php";

    public static String URL_UPDATE_PROFILE = URL+"app/updateprofile.php";

    public static String URL_GET_ALL_RESTAURANTS = URL+"app/getrestaurants.php";

    public static String URL_GET_ALL_ORDERS = URL+"app/getallorders.php";

    public static String URL_GET_ORDER_DETAILS = URL+"app/getorderdetails.php";

    public static String URL_GET_NEAR_BY_RESTAURANTS = URL+"app/getnearbyrestaurants.php";

    public static String URL_FEEDBACK = URL+"app/feedback.php";

    public static String URL_GET_FOOD_ITEMS = URL+"app/getFoodItems.php";

    public static String URL_CHECK_MOBILE_NO = URL+"app/mobileverification.php";

    public static String URL_CHECK_ITEM_STATUS = URL+"app/placeorder.php";

    public static String URL_LOG_OUT = URL+"app/logout.php";

    public static final String TAG_RESPONSE= "ErrorMessage";

    public static final String TAG_RESPONSE1= "success";

    public static final String REGISTER_URL = URL+"app/otp_register.php";

    public static final String CONFIRM_URL = URL+"app/otp_confirm.php";

    public static final String URL_ADD_ADDRESS = URL+"app/addaddress.php";

    public static final String URL_EDIT_ADDRESS = URL+"app/edit_address.php";

    public static final String URL_GET_ALL_ADDRESS = URL+"app/getalladdress.php";
}