package com.sytiqhub.tinga.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.activities.HomeActivity;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener1;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener2;
import com.sytiqhub.tinga.adapters.ProfileAdapter;
import com.sytiqhub.tinga.auth.ProfileFillActivity;
import com.sytiqhub.tinga.beans.UserBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    String[] ITEMS = { "Guidelines", "Terms & Conditions", "Privacy Policy", "Logout"};
    RecyclerView recycler;
    TextView txt_name,txt_email,txt_phno,txt_location,txt_edit_profile;
    ImageView img_profile_pic;
    FirebaseAuth firebaseAuth;
    OnListFragmentInteractionListener2 mListener;
    private AdView mAdView;
    private Uri selectedImageUri1 = null;
    private static final int GALLERY_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

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
        img_profile_pic = view.findViewById(R.id.profile_picture);
        txt_name = view.findViewById(R.id.profile_name);
        txt_email = view.findViewById(R.id.profile_email);
        txt_phno = view.findViewById(R.id.profile_phno);
        txt_location = view.findViewById(R.id.profile_location);
        txt_edit_profile = view.findViewById(R.id.profile_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        recycler = view.findViewById(R.id.profile_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PreferenceManager prefs = new PreferenceManager(getActivity());
        final UserBean userBean = prefs.getUserDetails();
        final TingaManager tingaManager = new TingaManager();

        txt_name.setText(userBean.getFname()+" "+userBean.getLname());
        txt_email.setText(userBean.getEmail());
        txt_phno.setText(userBean.getPhone_number());
        txt_location.setText(prefs.getLocationDetails().get("city"));

        recycler.setAdapter(new ProfileAdapter(ITEMS, new OnListFragmentInteractionListener2() {
            @Override
            public void onListFragmentInteraction(int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Log.d("Provider List: ",user.getProviderId());
                        String provider = user.getProviderData().get(1).getProviderId();
                        tingaManager.Logout(getActivity(),prefs.getLoginID(),provider);
                        break;
                }
            }
        }));

        img_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallertIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallertIntent.setType("image/*");
                getActivity().startActivityForResult(gallertIntent,GALLERY_REQUEST);
            }
        });

        txt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ProfileFillActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type","edit_profile");
                intent.putExtra("uid",userBean.getUid());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        return view;
    }

 @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                selectedImageUri1 = data.getData();
                //imageuri = getPath(selectedImageUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri1);
                    img_profile_pic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
