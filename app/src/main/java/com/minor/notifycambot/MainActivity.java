package com.minor.notifycambot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();
        databaseReference.child("notifi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent=new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fragment","Notification");
                PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(MainActivity.this);
                notificationBuilder.setContentTitle(dataSnapshot.getValue().toString());
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_chat);
                notificationBuilder.setContentIntent(pendingIntent);
                NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String CHANNEL_ID = "my_channel_01";// The id of the channel.
                    CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationBuilder.setChannelId(CHANNEL_ID);
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(notificationChannel);
                    notificationManager.notify(0,notificationBuilder.build());
                }
                else {

                    notificationManager.notify(0,notificationBuilder.build());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
