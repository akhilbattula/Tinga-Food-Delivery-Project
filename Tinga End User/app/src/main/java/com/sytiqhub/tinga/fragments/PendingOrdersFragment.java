package com.sytiqhub.tinga.fragments;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.MyOrdersAdapter;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrdersFragment extends Fragment{

    public List<OrderBean> ITEMS = new ArrayList<OrderBean>();
    public List<OrderBean> Actual_Items = new ArrayList<OrderBean>();

    RecyclerView recycler;
    TextView txt;
    MyOrdersAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    private AdView mAdView;

    public PendingOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        recycler = view.findViewById(R.id.myorders_recycler);
        txt = view.findViewById(R.id.no_items);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        TingaManager tingaManager = new TingaManager();
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //adapter = new MyOrdersAdapter(ITEMS, mListener);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("akhilll","onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("akhilll","onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("akhilll","onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.e("akhilll","onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e("akhilll","onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.e("akhilll","onAdClosed");
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_myorder);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2);
            }
        });

        addContent(1);
        return view;
    }

    public void addContent(int i) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TingaManager tingaManager = new TingaManager();
        tingaManager.getAllOrders(getActivity(), uid, i, new TingaManager.OrdersCallBack() {
            @Override
            public void onSuccess(List<OrderBean> detailsMovies) {
                ITEMS.clear();
                Actual_Items.clear();
                ITEMS = detailsMovies;
                if (ITEMS.size() <= 0) {
                    txt.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                } else {
                    Log.d("ID: ", ITEMS.get(0).getOrder_id());
                    for(int i = 0;i<ITEMS.size();i++){
                        if(!(ITEMS.get(i).getStatus().equalsIgnoreCase("Completed") ||
                                ITEMS.get(i).getStatus().equalsIgnoreCase("Rejected_Restaurant"))){
                            Actual_Items.add(ITEMS.get(i));
                        }
                    }
                    if (Actual_Items.size() <= 0) {
                        txt.setVisibility(View.VISIBLE);
                        recycler.setVisibility(View.GONE);
                    }else{
                        recycler.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.GONE);

                        adapter = new MyOrdersAdapter(getActivity(),Actual_Items);

                        recycler.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getActivity(), "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });
        if (pullToRefresh.isRefreshing()) {
            pullToRefresh.setRefreshing(false);
        }
    }
}
