package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

    // variable for Text view.
    private TextView retrieveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getInstance().getReference().child("number/int");

        // initializing our object class variable.
        retrieveTV = findViewById(R.id.idTVRetrieveData);

        // calling method
        // for getting data.
        getdata();
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String valueString = snapshot.getValue(String.class);

                // Parse the string value to an integer
                int value = Integer.parseInt(valueString);

                // Now you have the integer value
                retrieveTV.setText("Level " + value + "%");

                if (value >= 90){

                    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

                        NotificationChannel channel= new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager =getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
                    String message="Please Collect Garbage as Bin is 90% Filled";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"My Notification");
                    builder.setContentTitle("BIN IS ALMOST FILLED");
                    builder.setContentText(message);
                    builder.setSmallIcon(R.drawable.bin1);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity.this);
                    managerCompat.notify(1,builder.build());




                }
                }







            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bin(View view) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", 33.676350 + "," + 73.136847);
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }
}