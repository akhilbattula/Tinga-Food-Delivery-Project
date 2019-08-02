package com.sytiqhub.tinga.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sytiqhub.tinga.AddAddressActivity;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.adapters.AddressAdapter;
import com.sytiqhub.tinga.beans.AddressBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.List;
import java.util.Locale;


public class LocationDialogFragment extends DialogFragment {

    Context mcontext;
    com.sytiqhub.tinga.fragments.LocationDialogFragment.OnListFragmentInteractionListener monListFragmentInteractionListener;
    PreferenceManager prefs;
    Button current_location, new_address;
    RecyclerView recycler_addresses;
    ProgressDialog progress;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    public LocationDialogFragment(){

    }

    @SuppressLint("ValidFragment")
    public LocationDialogFragment(Context context, OnListFragmentInteractionListener onListFragmentInteractionListener){
        mcontext = context;
        monListFragmentInteractionListener = onListFragmentInteractionListener;
    }

    AddressAdapter addressAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.custom_dialog, container, false);
        current_location = view.findViewById(R.id.btn_current_location);
        new_address = view.findViewById(R.id.btn_another_address);
        recycler_addresses = view.findViewById(R.id.recycler);
        progress = new ProgressDialog(getActivity());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        prefs= new PreferenceManager(getActivity());

        addContent(prefs.getUID(),1);

        final LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatLong();
            }
        });

        new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Light_NoTitleBar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String address);

    }

    public void getLatLong() {

        progress.setMessage("Fetching your locaton, Please Wait...!");
        progress.show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            prefs.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                            prefs.setLongitude(String.valueOf(mLastLocation.getLongitude()));

                            prefs.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                            prefs.setLongitude(String.valueOf(mLastLocation.getLongitude()));
                            Log.v("akhilll", prefs.getLatitude());
                            Log.v("akhilll", prefs.getLongitude());
                            Geocoder gcd = new Geocoder(getActivity(),
                                    Locale.getDefault());

                            List<Address> addresses;
                            try {
                                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude(), 1);
                                if (addresses.size() > 0) {
                                    String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    prefs.setLocation(address);
                                    prefs.setLocationDetails(addresses.get(0).getLocality(), addresses.get(0).getAdminArea(), addresses.get(0).getCountryName(), addresses.get(0).getPostalCode());
                                    //Toast.makeText(HomeActivity.this, "Current location has been set to " + address, Toast.LENGTH_SHORT).show();
                                    monListFragmentInteractionListener.onListFragmentInteraction(address);
                                }
                                Log.v("akhilll", prefs.getLocation());
                                progress.dismiss();
                                dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                progress.dismiss();
                                dismiss();
                            }

                        } else {
                            Log.w("akhilll", "getLastLocation:exception", task.getException());
                            //showSnackbar(getString(R.string.no_location_detected));
                            dismiss();
                        }
                    }
                });

    }

    public void addContent(String uid,int i){

        TingaManager tingaManager = new TingaManager();

        tingaManager.getAllAddress(getActivity(), uid, i, new TingaManager.AllAddressCallBack() {
            @Override
            public void onSuccess(List<AddressBean> addresses) {
                addressAdapter = new AddressAdapter(addresses, new AddressAdapter.OnInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(AddressBean addressBean) {

                        prefs.setLocation(addressBean.getAddress());
                        prefs.setLatitude(addressBean.getLocation_lat());
                        prefs.setLongitude(addressBean.getLocation_long());

                        monListFragmentInteractionListener.onListFragmentInteraction(addressBean.getAddress());


                        dismiss();
                    }
                });
                recycler_addresses.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler_addresses.setAdapter(addressAdapter);
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        prefs= new PreferenceManager(getActivity());
        addContent(prefs.getUID(),2);

    }
}