package com.sytiqhub.tinga.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.activities.OrderDetailsActivity;
import com.sytiqhub.tinga.activities.OrderStatusActivity;
import com.sytiqhub.tinga.beans.OrderBean;


import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private List<OrderBean> mValues;
    //private final OnListFragmentInteractionListener3 mListener;
    Context mcontext;
    public MyOrdersAdapter(Context context, List<OrderBean> items/*, OnListFragmentInteractionListener3 listener*/) {
        mValues = items;
       // mListener = listener;
        mcontext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        if(mValues.size()<=0){

        }else{

            //holder.mItem = mValues.get(position);
            holder.r_name.setText(mValues.get(position).getRestaurant_name());
            holder.r_address.setText(mValues.get(position).getRestaurant_address());
            holder.order_date.setText(mValues.get(position).getOrder_date()+" "+mValues.get(position).getOrder_time());
            holder.total_amount.setText(mValues.get(position).getTotal_price()+"/-");
            holder.status.setText(mValues.get(position).getStatus());

            Picasso.get().load(Uri.parse(mValues.get(position).getRestaurant_image())).resize(50,50).into(holder.image);
            final Activity activity = (Activity) mcontext;
            if(mValues.get(position).getStatus().equalsIgnoreCase("Pending")){

                holder.status.setText(mValues.get(position).getStatus());

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Accepted_Restaurant")){

                holder.status.setText("Accepted");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Rejected_Restaurant")){

                holder.status.setText("Rejected by Restaurant");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Ready_Restaurant")){

                holder.status.setText("Order is Ready!!! Executive yet to collect");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Accepted_Delivery")){

                holder.status.setText("Our Executive is on the way to collect your order");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Rejected_Delivery")){

                holder.status.setText("Our Executive rejected your delivery. Please place your order with other restaurant.");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Collected_Delivery")){

                holder.status.setText("Our Executive collected your order and is on the way to deliver it to you.");

            }else{

                holder.status.setText(mValues.get(position).getStatus());

            }
            if(mValues.get(position).getStatus().equalsIgnoreCase("Rejected_Restaurant")
            || mValues.get(position).getStatus().equalsIgnoreCase("Completed")
                    || mValues.get(position).getStatus().equalsIgnoreCase("Rejected_Delivery")
            ){

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mcontext, OrderDetailsActivity.class);
                        i.putExtra("order_id",Integer.parseInt(mValues.get(position).getOrder_id()));
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        activity.startActivity(i);

                    }
                });

            }else{

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mcontext, OrderStatusActivity.class);
                        i.putExtra("order_id",Integer.parseInt(mValues.get(position).getOrder_id()));
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        activity.startActivity(i);

                    }
                });

            }

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView r_name;
        public final TextView r_address;
        //public final TextView items;
        public final TextView order_date;
        public final TextView total_amount;
        public final TextView status;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            r_name = view.findViewById(R.id.restaurant_name);
            r_address = view.findViewById(R.id.restaurant_address);
            order_date = view.findViewById(R.id.order_date);
            total_amount = view.findViewById(R.id.total_amount);
            status = view.findViewById(R.id.status);
            image = view.findViewById(R.id.r_image);


        }

    }



}

