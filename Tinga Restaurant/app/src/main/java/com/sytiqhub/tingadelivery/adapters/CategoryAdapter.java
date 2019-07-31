package com.sytiqhub.tingadelivery.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.bean.CategoryBean;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyVHolder>{
    List<CategoryBean> lc;
    Context context;
    Activity mactivity;

    public CategoryAdapter(Activity activity,Context context, List<CategoryBean> lc) {
        this.lc = lc;
        this.context = context;
        this.mactivity = activity;
    }

    @Override
    public CategoryAdapter.MyVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        MyVHolder mh=new MyVHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyVHolder holder, int position) {
        CategoryBean categoryBean=lc.get(position);

        holder.tvv.setText(categoryBean.getm());

        holder.rscv.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(holder.rscv,false);

        FoodListAdapter sca=new FoodListAdapter(mactivity,context,categoryBean.gets());

        holder.rscv.setLayoutManager(new LinearLayoutManager(context));
        holder.rscv.setAdapter(sca);

    }

    @Override
    public int getItemCount() {
        return lc.size();
    }

    public class MyVHolder extends RecyclerView.ViewHolder{

        public final View mView;
        public TextView tvv;
        RecyclerView rscv;
        public MyVHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvv= itemView.findViewById(R.id.category_name_textview);
            rscv= itemView.findViewById(R.id.sub_Category_name);

        }
    }
}