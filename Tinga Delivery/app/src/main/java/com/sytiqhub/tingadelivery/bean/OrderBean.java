package com.sytiqhub.tingadelivery.bean;

import java.util.List;

public class OrderBean {

    private String order_id,restaurant_id,status,order_date,order_time,order_lat,order_long,order_address,customer_name,customer_phone_number;
    private int total_price;
    private List<OrderFoodBean> foodlist;


    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<OrderFoodBean> getFoodlist() {
        return foodlist;
    }

    public void setFoodlist(List<OrderFoodBean> foodlist) {
        this.foodlist = foodlist;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public String getOrder_long() {
        return order_long;
    }

    public void setOrder_long(String order_long) {
        this.order_long = order_long;
    }

    public String getOrder_lat() {
        return order_lat;
    }

    public void setOrder_lat(String order_lat) {
        this.order_lat = order_lat;
    }

    public String getCustomer_phone_number() {
        return customer_phone_number;
    }

    public void setCustomer_phone_number(String customer_phone_number) {
        this.customer_phone_number = customer_phone_number;
    }
}
