package com.sytiqhub.tingadelivery.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.bean.OrderBean;
import com.sytiqhub.tingadelivery.bean.OrderFoodBean;
import com.sytiqhub.tingadelivery.listners.OrderStatusListener;
import com.sytiqhub.tingadelivery.manager.TingaManager;

import java.util.List;


public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ViewHolder> {

    private List<OrderBean> mValues;
    private final OrderStatusListener mListener;
    Context mcontext;
    DatabaseReference mdatabase;
    TingaManager tingaManager;
    Activity mactivity;
    public FoodItemsAdapter(Activity activity, List<OrderBean> items, OrderStatusListener listener) {
        mValues = items;
        mListener = listener;
        mactivity = activity;
        //mcontext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        tingaManager = new TingaManager();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        if(mValues.size()<=0){

        }else{
            //holder.mItem = mValues.get(position);
            holder.c_name.setText(mValues.get(position).getCustomer_name());
            StringBuilder stringBuilder = new StringBuilder();
            List<OrderFoodBean> list = mValues.get(position).getFoodlist();

            Log.d("list size: ", String.valueOf(list.size()));
            int count = 0;
            for(int i=0;i<list.size();i++){
                count++;
                Log.d("food name : ", list.get(i).getFoodName());

                stringBuilder.append(list.get(i).getQuantity());
                stringBuilder.append(" x ");
                stringBuilder.append(list.get(i).getFoodName());
                stringBuilder.append(", ");

                if(count==2){
                    stringBuilder.append("\n");
                    count=0;
                }
            }
            String food_list = stringBuilder.toString();

            String final_list = food_list.substring(0,food_list.length()-(food_list.length()-food_list.lastIndexOf(",")));

            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.ready.setVisibility(View.VISIBLE);

            holder.items.setText(final_list);
            holder.order_date.setText(mValues.get(position).getOrder_date()+" "+mValues.get(position).getOrder_time());
            holder.order_id.setText("#"+mValues.get(position).getOrder_id());
            holder.total_amount.setText(mValues.get(position).getTotal_price()+"/-");

            if(mValues.get(position).getStatus().equalsIgnoreCase("Pending")){

                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.ready.setVisibility(View.GONE);
                holder.status.setText(mValues.get(position).getStatus());

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Accepted_Restaurant")){

                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.ready.setVisibility(View.VISIBLE);
                holder.status.setText("Accepted");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Rejected_Restaurant")){

                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.ready.setVisibility(View.GONE);
                holder.status.setText("Rejected");

            }else if(mValues.get(position).getStatus().equalsIgnoreCase("Ready_Restaurant")){

                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.ready.setVisibility(View.GONE);
                holder.status.setText("Order is Ready!!! Executive yet to collect");

            }else{

                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.ready.setVisibility(View.GONE);
                holder.status.setText(mValues.get(position).getStatus());

            }

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tingaManager.ChangeOrderStatus(mactivity, Integer.parseInt(mValues.get(position).getOrder_id()), "Accepted_Restaurant", "null", new TingaManager.OrderStatusCallBack() {
                        @Override
                        public void onSuccess(int order_id) {

                            mListener.onStatusChangeInteraction(order_id);

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RejectDialog(Integer.parseInt(mValues.get(position).getOrder_id()));

                }
            });

            holder.ready.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tingaManager.ChangeOrderStatus(mactivity, Integer.parseInt(mValues.get(position).getOrder_id()),"Ready_Restaurant","null",new TingaManager.OrderStatusCallBack() {
                        @Override
                        public void onSuccess(int order_id) {

                            mListener.onStatusChangeInteraction(order_id);

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void RejectDialog(final int order_id){
        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(mactivity);

        createProjectAlert.setTitle("Reject Order");

        LayoutInflater inflater = mactivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.reject_dialog, null);
        final EditText edit = view.findViewById(R.id.et_reject_reason);

        createProjectAlert.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, int id) {

                        String reason = edit.getText().toString();
                        TingaManager tingaManager = new TingaManager();

                        tingaManager.ChangeOrderStatus(mactivity, order_id, "Rejected_Restaurant", reason, new TingaManager.OrderStatusCallBack() {
                            @Override
                            public void onSuccess(int order_id) {

                                dialog.dismiss();
                                mListener.onStatusChangeInteraction(order_id);

                            }

                            @Override
                            public void onFail(String msg) {

                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();

                    }
                });

        createProjectAlert.create();

        createProjectAlert.show();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView c_name;
        public final TextView items;
        public final TextView order_date;
        public final TextView total_amount;
        public final TextView status;
        public final TextView order_id;
        public final Button accept;
        public final Button reject;
        public final Button ready;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            c_name = view.findViewById(R.id.customer_name);
            items = view.findViewById(R.id.order_items);
            order_date = view.findViewById(R.id.order_date);
            total_amount = view.findViewById(R.id.total_amount);
            status = view.findViewById(R.id.status);
            order_id = view.findViewById(R.id.order_id);
            accept = view.findViewById(R.id.btn_order_accept);
            reject = view.findViewById(R.id.btn_order_reject);
            ready = view.findViewById(R.id.btn_food_ready);

        }

    }



}

