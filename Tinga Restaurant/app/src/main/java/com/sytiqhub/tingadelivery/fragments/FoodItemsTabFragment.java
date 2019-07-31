package com.sytiqhub.tingadelivery.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sytiqhub.tingadelivery.MainActivity;
import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodItemsTabFragment extends Fragment {


    public FoodItemsTabFragment() {
        // Required empty public constructor
    }

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_items,container, false);
        // Setting ViewPager for each Tabs
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((MainActivity)getActivity()).getSupportActionBar().show();

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

        VegItemsFragment vegItemsFragment = new VegItemsFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("rid",pref.getRestaurantDetails().getId());
        vegItemsFragment.setArguments(bundle1);

        NonvegItemsFragment nonvegItemsFragment = new NonvegItemsFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("rid",pref.getRestaurantDetails().getId());
        nonvegItemsFragment.setArguments(bundle2);

        EggItemsFragment eggItemsFragment = new EggItemsFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("rid",pref.getRestaurantDetails().getId());
        eggItemsFragment.setArguments(bundle3);

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(vegItemsFragment, "Veg Items");
        adapter.addFragment(nonvegItemsFragment, "Nonveg Items");
        adapter.addFragment(eggItemsFragment, "Egg Items");
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
