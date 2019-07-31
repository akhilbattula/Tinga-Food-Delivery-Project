package com.sytiqhub.tingadelivery.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.adapters.CategoryAdapter;
import com.sytiqhub.tingadelivery.bean.CategoryBean;
import com.sytiqhub.tingadelivery.bean.FoodBean;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class VegItemsFragment extends Fragment {

    public List<FoodBean> ITEMS = new ArrayList<FoodBean>();
    List<CategoryBean> lcat=new ArrayList<>();

    RecyclerView recycler;
    TextView txt;
    CategoryAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    String restaurant_id;

    public VegItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        View view = inflater.inflate(R.layout.fragment_veg_items, container, false);

        final PreferenceManager preferenceManager = new PreferenceManager(getActivity());
        recycler = view.findViewById(R.id.vegitems_recycler);
        txt = view.findViewById(R.id.no_items);

        Bundle b = getArguments();
        restaurant_id = b.getString("rid");

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefresh = view.findViewById(R.id.pullToRefresh_myorder);
        addContent(2,preferenceManager.getRestaurantDetails().getId());

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2,restaurant_id);
            }
        });
        return view;
    }

    public void addContent(int i,String rid){

        TingaManager tingaManager = new TingaManager();

        tingaManager.getFoodItems(getActivity(), rid, "VEG", i, new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> foodbeanlist) {
                ITEMS.clear();
                lcat.clear();
                ITEMS = foodbeanlist;
                if(ITEMS.size()<=0){
                    txt.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                }else{

                    Set<String> categoryset = new HashSet<>();

                    for(int i = 0;i<ITEMS.size();i++){

                        categoryset.add(ITEMS.get(i).getSubtype_tag());

                    }
                    List<String> categorylist_temp = new ArrayList<String>(categoryset);
                    List<String> categorylist = new ArrayList<String>();

                    if(categorylist_temp.contains("Tinga Specials")){
                        categorylist.add("Tinga Specials");
                    }
                    if(categorylist_temp.contains("Combos")){
                        categorylist.add("Combos");
                    }
                    if(categorylist_temp.contains("Family Pack")){
                        categorylist.add("Family Pack");
                    }
                    if(categorylist_temp.contains("Rice and Biryani")){
                        categorylist.add("Rice and Biryani");
                    }
                    if(categorylist_temp.contains("Starters")){
                        categorylist.add("Starters");
                    }
                    if(categorylist_temp.contains("Main Course")){
                        categorylist.add("Main Course");
                    }
                    if(categorylist_temp.contains("Soups")){
                        categorylist.add("Soups");
                    }
                    if(categorylist_temp.contains("Breads")){
                        categorylist.add("Breads");
                    }
                    if(categorylist_temp.contains("Chinese")){
                        categorylist.add("Chinese");
                    }

                    for(int i=0;i< categorylist.size();i++) {  //2
                        List<FoodBean> lfb = new ArrayList<>();

                        for (int j = 0; j < ITEMS.size(); j++) {  //3
                            if (ITEMS.get(j).getSubtype_tag().equalsIgnoreCase(categorylist.get(i)) &&
                                    ITEMS.get(j).getStatus().equalsIgnoreCase("Available")) {
                                lfb.add(ITEMS.get(j));
                            }
                        }
                        for (int j = 0; j < ITEMS.size(); j++) {  //3
                            if (ITEMS.get(j).getSubtype_tag().equalsIgnoreCase(categorylist.get(i)) &&
                                    !(ITEMS.get(j).getStatus().equalsIgnoreCase("Available"))) {
                                lfb.add(ITEMS.get(j));
                            }
                        }

                        Log.d("lfb size",String.valueOf(lfb.size()));
                        if(lfb.size()>0){
                            Log.d("lfb size",String.valueOf(lfb.size()));
                            lcat.add(new CategoryBean(categorylist.get(i), lfb));
                        }
                    }

                    recycler.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.GONE);
                    adapter = new CategoryAdapter(getActivity(),getActivity(),lcat);
                    recycler.setAdapter(adapter);
                }

                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getActivity(), "Failed retrieve data...", Toast.LENGTH_SHORT).show();
                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            addContent(2,restaurant_id);
        }
    }

}
