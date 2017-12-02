package com.example.employeetimetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HoursActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView hoursList;
    private TextView totalHoursText;
    private Button backButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ArrayList<String> records = new ArrayList<>();
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<Long> hoursArray = new ArrayList<Long>();
    private ArrayList<Long> minutesArray = new ArrayList<Long>();
    private ArrayList<Long> secondsArray = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        hoursList = (ListView) findViewById(R.id.hoursList);
        totalHoursText = (TextView) findViewById(R.id.totalHoursText);
        backButton = (Button) findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference(user.getUid());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hours);
        hoursList.setAdapter(arrayAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                long sumHours = 0;
                long sumMinutes = 0;
                long sumSeconds = 0;

                records.add(key);

                if(records.size()%2 == 0){
                    try {
                        Date outTime = dateFormat.parse(records.get(records.size()-1));
                        Date inTime = dateFormat.parse(records.get(records.size()-2));

                        long timeDifference = Math.abs(outTime.getTime() - inTime.getTime());
                        long secondDifference = (timeDifference / 1000) % 60;
                        long minuteDifference = (timeDifference / (1000 * 60)) % 60;
                        long hourDifference = (timeDifference / (1000 * 60 * 60)) % 24;
                        long dayDifference = TimeUnit.MILLISECONDS.toDays(timeDifference);

                        hoursArray.add(hourDifference);
                        minutesArray.add(minuteDifference);
                        secondsArray.add(secondDifference);
                        hours.add(hourDifference + " hours " + minuteDifference + " minute " + secondDifference + " second ");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for(Long longSecond : secondsArray){
                        sumSeconds += longSecond;
                    }

                    sumMinutes = sumSeconds / 60;
                    sumSeconds = sumSeconds % 60;

                    for(Long longMinute : minutesArray){
                        sumMinutes += longMinute;
                    }

                    sumHours = sumMinutes / 60;
                    sumMinutes = sumMinutes % 60;

                    for(Long longHour : hoursArray){
                        sumHours += longHour;
                    }

                    totalHoursText.setText(sumHours + " hours " + sumMinutes + " minute " + sumSeconds + " second");

                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                records.clear();
                hours.clear();
                hoursArray.clear();
                minutesArray.clear();
                secondsArray.clear();

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                records.clear();
                hours.clear();
                hoursArray.clear();
                minutesArray.clear();
                secondsArray.clear();

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                records.clear();
                hours.clear();
                hoursArray.clear();
                minutesArray.clear();
                secondsArray.clear();

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == backButton){
            // go back to Clock Activity
            finish();
            startActivity(new Intent(this, ClockActivity.class));
        }
    }


}