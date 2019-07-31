package com.sytiqhub.tingadelivery.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousOrdersTabFragment extends Fragment {

    public PreviousOrdersTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_tab,container, false);
        // Setting ViewPager for each Tabs
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        PreferenceManager pref = new PreferenceManager(getActivity());


        Adapter adapter = new Adapter(getChildFragmentManager());
        CompletedOrdersFragment completedOrdersFragment = new CompletedOrdersFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("rid",pref.getDeliveryDetails().getId());
        completedOrdersFragment.setArguments(bundle1);

        IncompleteOrdersFragment incompleteOrdersFragment = new IncompleteOrdersFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("rid",pref.getDeliveryDetails().getId());
        incompleteOrdersFragment.setArguments(bundle2);

        adapter.addFragment(completedOrdersFragment, "Completed Orders");
        adapter.addFragment(incompleteOrdersFragment, "Incomplete Orders");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
