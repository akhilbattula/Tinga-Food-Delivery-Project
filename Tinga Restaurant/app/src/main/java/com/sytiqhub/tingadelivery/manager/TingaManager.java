package com.sytiqhub.tingadelivery.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sytiqhub.tingadelivery.AppController;
import com.sytiqhub.tingadelivery.bean.FoodBean;
import com.sytiqhub.tingadelivery.bean.OrderBean;
import com.sytiqhub.tingadelivery.bean.OrderFoodBean;
import com.sytiqhub.tingadelivery.bean.RestaurantBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class TingaManager {

    private ProgressDialog pDialog;
    //FirebaseAuth mAuth;
    //PreferenceManager prefs;
    public TingaManager(){

    }

    public void addFoodItem(final Activity activity, final String name, final String description, final String price, final String type, final String subtype, final String rid, final AddFoodCallBack callBack) {

        String tag_string_req = "add_food";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Adding food item, Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_FOOD_ITEM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));

                        if (success_code == 1) {

                            String message = jObj.getString("message");

                            callBack.onSuccess(message);
                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message , Toast.LENGTH_LONG).show();
                            callBack.onFail(message);

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("description", description);
                params.put("price", price);
                params.put("type", type);
                params.put("subtype", subtype);
                params.put("rid", rid);
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    public void login(final Activity activity, final String username, final String password, final LoginCallBack onCallBack) {

        String tag_string_req = "login";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(true);
        pDialog.setMessage("Logging in, Please wait ...");

        final PreferenceManager prefs = new PreferenceManager(activity);
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                RestaurantBean bean = new RestaurantBean();
                Log.d(TAG, "orders Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    //Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObj = jArray.getJSONObject(i);

                        int success_code = jObj.getInt("success");
                        //Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            //Log.d("data_array : ",data_array.toString());

                            String uid = data_array.getString("restaurant_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String city = data_array.getString("city");
                            String state = data_array.getString("state");
                            String country = data_array.getString("country");
                            String location_lat = data_array.getString("location_lat");
                            String location_long = data_array.getString("location_long");
                            String cuisine = data_array.getString("cuisine");
                            String timings = data_array.getString("timings");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);

                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setCity(city);
                            bean.setState(state);
                            bean.setCountry(country);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setCuisine(cuisine);
                            bean.setTimings(timings);
                            bean.setRating(rating);
                            bean.setStatus(status);
                            bean.setImage_path(image_path);

                            prefs.setLoggedIn(activity,true);

                            onCallBack.onSuccess(bean);


                        } else if (success_code == 0) {

                            String message = jObj.getString("message");
                            onCallBack.onFail(message);

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (JSONException e) {
                    // JSON error
                    hideDialog();
                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private List<FoodBean> fbeans;
    public void getFoodItems(final Activity activity, final String rid, final String keyword, final int num, final FoodCallBack onCallBack) {

        fbeans = new ArrayList<>();
        String tag_string_req = "getFoodItems";
        if(num == 1){
            pDialog = new ProgressDialog(activity);
            pDialog.setCancelable(true);
            pDialog.setMessage("Getting Food Items, Please wait ...");
            showDialog();
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_FOOD_ITEMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Food Response: " + response);
                if(num == 1){
                    hideDialog();
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            Log.d("data_array : ",data_array.toString());

                            String fid = data_array.getString("food_id");
                            String name = data_array.getString("name");
                            String price = data_array.getString("price");
                            String desc = data_array.getString("desc");
                            String typetag = data_array.getString("typetag");
                            String subtypetag = data_array.getString("subtype_tag");
                            String status = data_array.getString("status");
                            String recommended = data_array.getString("recommended");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);

                            Log.d("uid: ",fid);

                            FoodBean bean = new FoodBean();
                            bean.setId(fid);
                            bean.setName(name);
                            bean.setPrice(price);
                            bean.setDesc(desc);
                            bean.setTypetag(typetag);
                            bean.setSubtype_tag(subtypetag);
                            bean.setStatus(status);
                            bean.setRecommended(recommended);
                            bean.setImage_path(image_path);

                            fbeans.add(bean);
                            if(num == 1){
                                hideDialog();
                            }
                            Log.d("Items: ",fbeans.toString());

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");
/*
                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
*/
                        }
                    }

                    onCallBack.onSuccess(fbeans);


                } catch (JSONException e) {
                    // JSON error
                    if(num == 1){
                        hideDialog();
                     //   pDialog.dismiss();
                    }
                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                if(num == 1){
                    hideDialog();
                    pDialog.dismiss();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("r_id", rid);
                params.put("keyword", keyword);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private List<OrderBean> ordersbean;
    public void getAllOrders(final Activity activity, final String rid, final int num, final OrdersCallBack onCallBack) {

        ordersbean = new ArrayList<>();

        String tag_string_req = "getorders";
        if(num==1){
            pDialog = new ProgressDialog(activity);
            //pDialog.setCancelable(false);
            pDialog.setMessage("Getting all orders, Please wait ...");
            showDialog();
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ALL_ORDERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "orders Response: " + response);
                if(num==1){
                    hideDialog();
                }

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    //Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObj = jArray.getJSONObject(i);

                        int success_code = jObj.getInt("success");
                        //Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            //Log.d("data_array : ",data_array.toString());

                            String order_id = data_array.getString("order_id");
                            String customer_name = data_array.getString("customer_name");
                            String customer_id = data_array.getString("customer_id");
                            String order_status = data_array.getString("order_status");
                            String total_price = data_array.getString("total_price");
                            String order_date = data_array.getString("order_date");
                            String order_time = data_array.getString("order_time");

                            JSONArray items_array = (JSONArray)data_array.getJSONArray("food_items");
                            Log.d("items length : ", String.valueOf(items_array.length()));
                            List<OrderFoodBean> itemsbean = new ArrayList<>();
                            for (int j = 0;j<items_array.length();j++){

                                OrderFoodBean orderFoodBean = new OrderFoodBean();

                                JSONObject item = items_array.getJSONObject(j);

                                Log.d("data_array : ",item.toString());

                                orderFoodBean.setFoodId(item.getString("food_id"));
                                orderFoodBean.setFoodName(item.getString("food_name"));
                                orderFoodBean.setFood_price(Integer.parseInt(item.getString("food_price")));
                                orderFoodBean.setQuantity(Integer.parseInt(item.getString("quantity")));
                                orderFoodBean.setTotalPrice(Integer.parseInt(item.getString("total_price")));
                                itemsbean.add(orderFoodBean);

                            }

                            OrderBean bean = new OrderBean();
                            bean.setOrder_id(order_id);
                            bean.setCustomer_name(customer_name);
                            bean.setStatus(order_status);
                            bean.setTotal_price(Integer.parseInt(total_price));
                            bean.setOrder_date(order_date);
                            bean.setOrder_time(order_time);
                            bean.setFoodlist(itemsbean);

                            ordersbean.add(bean);

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(ordersbean);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    if(num==1){
                        hideDialog();
                    }
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                if(num==1){
                    hideDialog();
                }
            }
        }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("rid", rid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void getRestaurantDetails(final Activity activity, final String rid, final int num, final RestaurantCallBack onCallBack) {

        ordersbean = new ArrayList<>();

        String tag_string_req = "getrestaurantdetails";
        if(num==1){
            pDialog = new ProgressDialog(activity);
            //pDialog.setCancelable(false);
            pDialog.setMessage("Getting Restaurant Details, Please wait ...");
            showDialog();
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_RESTAURANT_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                RestaurantBean bean = new RestaurantBean();
                Log.d(TAG, "orders Response: " + response);
                if(num==1){
                    hideDialog();
                }
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    //Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObj = jArray.getJSONObject(i);

                        int success_code = jObj.getInt("success");
                        //Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            //Log.d("data_array : ",data_array.toString());

                            String uid = data_array.getString("restaurant_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String city = data_array.getString("city");
                            String state = data_array.getString("state");
                            String country = data_array.getString("country");
                            String location_lat = data_array.getString("location_lat");
                            String location_long = data_array.getString("location_long");
                            String cuisine = data_array.getString("cuisine");
                            String timings = data_array.getString("timings");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);


                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setCity(city);
                            bean.setState(state);
                            bean.setCountry(country);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setCuisine(cuisine);
                            bean.setTimings(timings);
                            bean.setRating(rating);
                            bean.setStatus(status);
                            bean.setImage_path(image_path);

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");
                            onCallBack.onFail(message);

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(bean);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    if(num==1){
                        hideDialog();
                    }
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                if(num==1){
                    hideDialog();
                }
            }
        }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("rid", rid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void ChangeOrderStatus(final Activity activity, final int order_id, final String status, final String feedback, final OrderStatusCallBack orderStatusCallBack) {

        String tag_string_req = "update_order";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Updating order status, Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));

                        if (success_code == 1) {

                            String message = jObj.getString("message");

                            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(order_id));

                            if(status.equalsIgnoreCase("Accepted_Restaurant")){

                                mdatabase.child("food_status").setValue("Accepted_Restaurant");



                            }else if(status.equalsIgnoreCase("Rejected_Restaurant")){

                                mdatabase.child("food_status").setValue("Rejected_Restaurant");
                                mdatabase.child("order_feedback").setValue(feedback);

                            }else if(status.equalsIgnoreCase("Ready_Restaurant")){

                                mdatabase.child("food_status").setValue("Ready_Restaurant");

                            }else if(status.equalsIgnoreCase("Accepted_Delivery")){

                                mdatabase.child("food_status").setValue("Accepted_Delivery");

                            }else if(status.equalsIgnoreCase("Collected_Delivery")){

                                mdatabase.child("food_status").setValue("Collected_Delivery");

                            }else if(status.equalsIgnoreCase("Delivered")){

                                mdatabase.child("food_status").setValue("Delivered");

                            }

                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

                orderStatusCallBack.onSuccess(order_id);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", String.valueOf(order_id));
                params.put("order_status", String.valueOf(status));
                params.put("order_feedback", feedback);
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    public void ChangeFoodStatus(final Activity activity, final String food_id, final String status, final FoodStatusCallBack foodStatusCallBack) {

        String tag_string_req = "update_food";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Updating food status, Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_FOOD_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));

                        if (success_code == 1) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

                foodStatusCallBack.onSuccess(food_id);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("food_id", String.valueOf(food_id));
                params.put("food_status", String.valueOf(status));
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    public void ChangeRestaurantStatus(final Activity activity, final String restaurant_id, final String status, final RestaurantStatusCallBack restaurantStatusCallBack) {

        String tag_string_req = "update_food";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Updating restaurant status, Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_RESTAURANT_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));

                        if (success_code == 1) {

                            String message = jObj.getString("message");

                            /*Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();*/

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

                restaurantStatusCallBack.onSuccess(restaurant_id);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", String.valueOf(restaurant_id));
                params.put("restaurant_status", String.valueOf(status));
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public interface OrderStatusCallBack {
        void onSuccess(int order_id);

        void onFail(String msg);
    }

    public interface FoodStatusCallBack {
        void onSuccess(String food_id);

        void onFail(String msg);
    }

    public interface RestaurantStatusCallBack {
        void onSuccess(String food_id);

        void onFail(String msg);
    }

    public interface AddFoodCallBack {
        void onSuccess(String msg);

        void onFail(String msg);
    }


    public interface RestaurantCallBack {
        void onSuccess(RestaurantBean restaurantBean);

        void onFail(String msg);
    }

    public interface LoginCallBack {
        void onSuccess(RestaurantBean restaurantBean);

        void onFail(String msg);
    }

    public interface FoodCallBack {
        void onSuccess(List<FoodBean> foodbeanlist);

        void onFail(String msg);
    }

    public interface OrdersCallBack {
        void onSuccess(List<OrderBean> detailsMovies);

        void onFail(String msg);
    }


}
