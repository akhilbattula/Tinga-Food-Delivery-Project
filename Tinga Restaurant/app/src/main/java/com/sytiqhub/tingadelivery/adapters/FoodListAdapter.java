package com.sytiqhub.tingadelivery.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.bean.FoodBean;
import com.sytiqhub.tingadelivery.manager.TingaManager;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private List<FoodBean> mValues;
    private Context context;
    Activity mactivity;
    public FoodListAdapter(Activity activity, Context mcontext, List<FoodBean> items) {
        mValues = items;
        context = mcontext;
        mactivity = activity;
    }
    private static int count=0;

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);
        return new FoodListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodListAdapter.ViewHolder holder, final int position) {

        final TingaManager tinga = new TingaManager();
        if(!(mValues.size()<=0)){

            holder.mItem = mValues.get(position);
            holder.tv_food_name.setText(mValues.get(position).getName());
            holder.tv_food_price.setText(mValues.get(position).getPrice()+"/-");

            if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){

                holder.sw_status.setChecked(true);

            }else{

                holder.sw_status.setChecked(false);

            }

            holder.sw_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        tinga.ChangeFoodStatus(mactivity, mValues.get(position).getId(), "Available", new TingaManager.FoodStatusCallBack() {
                            @Override
                            public void onSuccess(String food_id) {

                            }

                            @Override
                            public void onFail(String msg) {

                            }
                        });
                    }else{
                        tinga.ChangeFoodStatus(mactivity, mValues.get(position).getId(), "Not Available", new TingaManager.FoodStatusCallBack() {
                            @Override
                            public void onSuccess(String food_id) {

                            }

                            @Override
                            public void onFail(String msg) {

                            }
                        });

                    }
                }
            });

            if(mValues.get(position).getTypetag().equalsIgnoreCase("VEG")){
                Picasso.get().load(R.drawable.veg).into(holder.type_image);
                holder.tv_food_type.setText("VEG");
            }else if(mValues.get(position).getTypetag().equalsIgnoreCase("NON-VEG")){
                Picasso.get().load(R.drawable.nonveg).into(holder.type_image);
                holder.tv_food_type.setText("NON-VEG");
            }else if(mValues.get(position).getTypetag().equalsIgnoreCase("EGG")){
                Picasso.get().load(R.drawable.egg).into(holder.type_image);
                holder.tv_food_type.setText("EGG");
            }

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(context, "You've clicked on item", Toast.LENGTH_SHORT).show();

                    return false;
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView tv_food_name;
        public final TextView tv_food_price;
        public final Switch sw_status;
        public final TextView tv_food_type;
        public FoodBean mItem;
        public ImageView type_image;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_food_name = view.findViewById(R.id.food_item_name);
            tv_food_price = view.findViewById(R.id.food_price);
            sw_status = view.findViewById(R.id.food_switch);
            //image = view.findViewById(R.id.food_image);
            tv_food_type = view.findViewById(R.id.food_type);

            type_image = view.findViewById(R.id.img_food_type);

        }
    }
}
