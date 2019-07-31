package com.sytiqhub.tinga.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.CategoryBean;
import com.sytiqhub.tinga.beans.FoodBean;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyVHolder>{
    List<CategoryBean> lc;
    Context context;

    public CategoryAdapter(Context context, List<CategoryBean> lc) {
        this.lc = lc;
        this.context = context;
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

        FoodListAdapter sca=new FoodListAdapter(context,categoryBean.gets(), new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(FoodBean item) {
                Log.d("f_price",item.getPrice());
/*
                Intent i = new Intent(context, FoodDescriptionActivity.class);
                i.putExtra("f_id",item.getId());
                i.putExtra("f_name",item.getName());
                i.putExtra("f_desc",item.getDesc());
                i.putExtra("f_price",item.getPrice());
                i.putExtra("f_status",item.getStatus());
                i.putExtra("f_image",item.getImage_path());
                //context.getApplicationContext().overridePendingTransition(R.anim.enter, R.anim.exit);
                context.startActivity(i);
*/
            }
        });

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