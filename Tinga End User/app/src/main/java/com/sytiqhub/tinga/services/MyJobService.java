package com.sytiqhub.tinga.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.activities.OrderStatusActivity;

public class MyJobService extends JobService {

    DatabaseReference mdatabase;
    int order_id;
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        Toast.makeText(this, "Starting Job", Toast.LENGTH_SHORT).show();

        Bundle extras = job.getExtras();

        order_id = extras.getInt("order_id");

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders");

        mdatabase.child(String.valueOf(order_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equals("food_status")){

                    if(dataSnapshot.getValue(String.class).equals("Accepted_Restaurant")){

                        addNotification("Tinga Notification","Your order is accepted by the restaurant and is being prepared...",order_id);

                    }else if(dataSnapshot.getValue(String.class).equals("Rejected_Restaurant")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                addNotification("Tinga Notification","Your order has been rejected by the restaurant. Please order with other restaurant...",order_id);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else if(dataSnapshot.getValue(String.class).equals("Ready_Restaurant")){

                        addNotification("Tinga Notification","Your order is ready for delivery...",order_id);

                    }else if(dataSnapshot.getValue(String.class).equals("Accepted_Delivery")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                addNotification("Tinga Notification","Our executive "+dataSnapshot.child("delivery_name").getValue(String.class)+" is on the way to collect your order...",order_id);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot.getValue(String.class).equals("Collected_Delivery")){

                        addNotification("Tinga Notification","Your order is collected and is on the way to you...",order_id);

                    }else if(dataSnapshot.getValue(String.class).equals("Delivered")){

                        mdatabase.child(String.valueOf(order_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                addNotification("Tinga Notification","Your Order is delivered on "+dataSnapshot.child("delivery_time").getValue(String.class),order_id);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(this, "Stoping Job", Toast.LENGTH_SHORT).show();

        return false; // Answers the question: "Should this job be retried?"
    }


    private void addNotification(String title,String content,int order_id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent notificationIntent = new Intent(this, OrderStatusActivity.class);
        notificationIntent.putExtra("order_id",order_id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}