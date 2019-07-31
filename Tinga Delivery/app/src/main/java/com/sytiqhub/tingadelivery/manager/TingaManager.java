package com.sytiqhub.tingadelivery.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sytiqhub.tingadelivery.AppController;
import com.sytiqhub.tingadelivery.bean.DeliveryBean;
import com.sytiqhub.tingadelivery.bean.FoodBean;
import com.sytiqhub.tingadelivery.bean.OrderBean;
import com.sytiqhub.tingadelivery.bean.OrderFoodBean;

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
                DeliveryBean bean = new DeliveryBean();
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

                            String uid = data_array.getString("delivery_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String image_code = data_array.getString("image_code");
                            String phone_number = data_array.getString("phone_number");
                            String email = data_array.getString("email");
                            String location_lat = data_array.getString("latitude");
                            String location_long = data_array.getString("longitude");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");

                            Log.d("imagepath: ",image_code);

                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setPhone_number(phone_number);
                            bean.setEmail(email);
                            bean.setStatus(status);
                            bean.setRating(rating);
                            bean.setImage_code(image_code);

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
                AppConfig.URL_GET_ORDERS, new Response.Listener<String>() {

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
                            String customer_phone_number = data_array.getString("customer_phone_number");
                            String order_status = data_array.getString("order_status");
                            String total_price = data_array.getString("total_price");
                            String order_date = data_array.getString("order_date");
                            String order_time = data_array.getString("order_time");
                            String order_lat = data_array.getString("order_lat");
                            String order_long = data_array.getString("order_long");
                            String order_address = data_array.getString("order_address");

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
                            bean.setCustomer_phone_number(customer_phone_number);
                            bean.setStatus(order_status);
                            bean.setTotal_price(Integer.parseInt(total_price));
                            bean.setOrder_date(order_date);
                            bean.setOrder_time(order_time);
                            bean.setOrder_long(order_long);
                            bean.setOrder_lat(order_lat);
                            bean.setOrder_address(order_address);
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
                params.put("d_id", rid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void getDeliveryDetails(final Activity activity, final String did, final int num, final DeliveryCallBack onCallBack) {

        String tag_string_req = "getdeliverydetails";
        if(num==1){
            pDialog = new ProgressDialog(activity);
            //pDialog.setCancelable(false);
            pDialog.setMessage("Getting Details, Please wait ...");
            showDialog();
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DELIVERY_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                DeliveryBean bean = new DeliveryBean();
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

                            String uid = data_array.getString("delivery_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String image_code = data_array.getString("image_code");
                            String phone_number = data_array.getString("phone_number");
                            String email = data_array.getString("email");
                            String location_lat = data_array.getString("latitude");
                            String location_long = data_array.getString("longitude");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");

                            Log.d("imagepath: ",image_code);

                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setPhone_number(phone_number);
                            bean.setEmail(email);
                            bean.setStatus(status);
                            bean.setRating(rating);
                            bean.setImage_code(image_code);

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
                params.put("d_id", did);
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

                            if(status.equalsIgnoreCase("Accepted_Delivery")){

                                mdatabase.child("food_status").setValue("Accepted_Delivery");

                            }else if(status.equalsIgnoreCase("Rejected_Delivery")){

                                mdatabase.child("food_status").setValue("Rejected_Delivery");
                                mdatabase.child("order_feedback").setValue(feedback);

                            }else if(status.equalsIgnoreCase("Ready_Delivery")){

                                mdatabase.child("food_status").setValue("Ready_Delivery");

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

    public void UpdateLocation(final Context activity, final int delivery_id, final String lat, final String lon) {

        String tag_string_req = "update_location";
        //pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        //pDialog.setMessage("Updating order status, Please wait ...");
        //showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_LOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                //hideDialog();

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
                            //Toast.makeText(activity,message+" "+lat+", "+lon, Toast.LENGTH_LONG).show();
                            Log.d("success code: ",message+" "+lat+", "+lon);

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            //Toast.makeText(activity,message + " Try again later.", Toast.LENGTH_LONG).show();
                            Log.d("success code: ",message + " Try again later.");

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    //hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

                //orderStatusCallBack.onSuccess(order_id);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("location_lat", lat);
                params.put("location_long", lon);
                params.put("delivery_id", String.valueOf(delivery_id));
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    public void ChangeDeliveryStatus(final Activity activity, final String delivery_id, final String status, final DeliveryStatusCallBack deliveryStatusCallBack) {

        String tag_string_req = "update_food";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Updating delivery status, Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_DELIVERY_STATUS, new Response.Listener<String>() {

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

                deliveryStatusCallBack.onSuccess(delivery_id);

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
                params.put("delivery_id", String.valueOf(delivery_id));
                params.put("delivery_status", String.valueOf(status));
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

    public interface DeliveryStatusCallBack {
        void onSuccess(String food_id);

        void onFail(String msg);
    }

    public interface AddFoodCallBack {
        void onSuccess(String msg);

        void onFail(String msg);
    }


    public interface DeliveryCallBack {
        void onSuccess(DeliveryBean deliveryBean);

        void onFail(String msg);
    }

    public interface LoginCallBack {
        void onSuccess(DeliveryBean deliveryBean);

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
