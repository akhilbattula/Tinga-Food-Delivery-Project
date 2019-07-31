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
import com.sytiqhub.tingadelivery.adapters.MyOrdersAdapter;
import com.sytiqhub.tingadelivery.bean.OrderBean;
import com.sytiqhub.tingadelivery.listners.OrderStatusListener;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedOrdersFragment extends Fragment {

    public List<OrderBean> ITEMS = new ArrayList<OrderBean>();
    public List<OrderBean> Actual_Items = new ArrayList<OrderBean>();

    RecyclerView recycler;
    TextView txt;
    MyOrdersAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    String restaurant_id;

    public CompletedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_orders, container, false);
        recycler = view.findViewById(R.id.myorders_recycler);
        txt = view.findViewById(R.id.no_items);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        Bundle b = getArguments();
        restaurant_id = b.getString("rid");

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_myorder);

        addContent(1,restaurant_id);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2,restaurant_id);
            }
        });

        return view;

    }

    public void addContent(int i, final String rid){

        TingaManager tingaManager = new TingaManager();
        tingaManager.getAllOrders(getActivity(), rid, i, new TingaManager.OrdersCallBack() {
            @Override
            public void onSuccess(List<OrderBean> detailsMovies) {
                ITEMS.clear();
                Actual_Items.clear();
                ITEMS = detailsMovies;
                if(ITEMS.size()<=0){
                    txt.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                }else{
                    Log.d("ID: ",ITEMS.get(0).getOrder_id());
                    for(int i = 0;i<ITEMS.size();i++){
                        Log.d("Stauts: ",ITEMS.get(i).getStatus());
                        if((ITEMS.get(i).getStatus().equalsIgnoreCase("Completed"))){
                                Actual_Items.add(ITEMS.get(i));
                        }
                    }
                    Collections.reverse(Actual_Items);

                    if (Actual_Items.size() <= 0) {
                        txt.setVisibility(View.VISIBLE);
                        recycler.setVisibility(View.GONE);
                    }else{
                        recycler.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.GONE);
                        adapter = new MyOrdersAdapter(getActivity(), Actual_Items, new OrderStatusListener() {
                            @Override
                            public void onStatusChangeInteraction(int order_id) {
                                addContent(1,rid);
                            }
                        });

                        recycler.setAdapter(adapter);
                    }

                }
                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getActivity(), "Failed retrieve data...", Toast.LENGTH_SHORT).show();
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
