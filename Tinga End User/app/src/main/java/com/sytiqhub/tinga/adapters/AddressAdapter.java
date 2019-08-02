package com.sytiqhub.tinga.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.annotations.NotNull;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.AddressBean;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressBean> mValues;
    private OnInteractionListener mListener;
    public AddressAdapter(List<AddressBean> items, OnInteractionListener onInteractionListener) {
        mValues = items;
        mListener = onInteractionListener;

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        if(mValues.size()<=0){

        }else{
            //holder.mItem = mValues.get(position);
            holder.tv_id.setText(String.valueOf(mValues.get(position).getId()));
            holder.tv_title.setText(mValues.get(position).getTitle());
            holder.tv_address.setText(mValues.get(position).getAddress());
            holder.tv_latitude.setText(mValues.get(position).getLocation_lat());
            holder.tv_longitude.setText(mValues.get(position).getLocation_long());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onListFragmentInteraction(mValues.get(position));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView tv_id;
        public final TextView tv_title;
        public final TextView tv_address;
        public final TextView tv_latitude;
        public final TextView tv_longitude;
        public AddressBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_id = view.findViewById(R.id.tv_id);
            tv_title = view.findViewById(R.id.tv_title);
            tv_address = view.findViewById(R.id.tv_address);
            tv_latitude = view.findViewById(R.id.tv_latitude);
            tv_longitude = view.findViewById(R.id.tv_longitude);

        }

    }

    public interface OnInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AddressBean addressBean);

    }

}
