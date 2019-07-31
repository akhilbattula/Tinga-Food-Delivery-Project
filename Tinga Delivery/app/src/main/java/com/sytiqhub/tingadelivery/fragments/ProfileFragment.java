package com.sytiqhub.tingadelivery.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sytiqhub.tingadelivery.LoginActivity;
import com.sytiqhub.tingadelivery.MainActivity;
import com.sytiqhub.tingadelivery.R;
import com.sytiqhub.tingadelivery.adapters.ProfileAdapter;
import com.sytiqhub.tingadelivery.bean.DeliveryBean;
import com.sytiqhub.tingadelivery.bean.OrderBean;
import com.sytiqhub.tingadelivery.listners.OnListFragmentInteractionListener;
import com.sytiqhub.tingadelivery.manager.PreferenceManager;
import com.sytiqhub.tingadelivery.manager.TingaManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    String[] ITEMS = { "Guidelines", "Terms & Conditions", "Privacy Policy", "Logout"};
    RecyclerView recycler;
    TextView txt,txt_name,txt_email,txt_address,txt_location,txt_rating;
    ImageView img_profile_pic;
    SwipeRefreshLayout pullToRefresh;
    String delivery_id;
    //OnListFragmentInteractionListener2 mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((MainActivity)getActivity()).getSupportActionBar().show();

        Bundle b = getArguments();
        delivery_id = b.getString("rid");

        final PreferenceManager preferenceManager = new PreferenceManager(getActivity());

        recycler = view.findViewById(R.id.profile_recycler);
        img_profile_pic = view.findViewById(R.id.delivery_picture);
        txt_name = view.findViewById(R.id.delivery_name);
        txt_rating = view.findViewById(R.id.delivery_rating);
        txt_address = view.findViewById(R.id.delivery_address);
        //txt_location = view.findViewById(R.id.delivery_location);

        //tingaManager.getDeliveryDetails(getActivity(),);

        txt_name.setText(preferenceManager.getDeliveryDetails().getName());
        txt_rating.setText(preferenceManager.getDeliveryDetails().getRating());
        txt_address.setText(preferenceManager.getDeliveryDetails().getAddress());


       /* byte[] decodedString = Base64.decode("/9j/4AAQSkZJRgABAQAASABIAAD/4QCMRXhpZgAATU0AKgAAAAgABQESAAMAAAABAAEAAAEaAAUAAAABAAAASgEbAAUAAAABAAAAUgEoAAMAAAABAAIAAIdpAA", Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        img_profile_pic.setImageBitmap(decodedByte);*/

        //Picasso.get().load(getImageUri(getActivity(),decodedByte)).into(img_profile_pic);

        recycler.setAdapter(new ProfileAdapter(ITEMS, new OnListFragmentInteractionListener() {
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
                        preferenceManager.setLoggedIn(getActivity(),false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                }

            }
        }));
        return view;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
