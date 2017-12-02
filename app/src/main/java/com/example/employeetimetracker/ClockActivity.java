package com.example.employeetimetracker;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


public class ClockActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userText;
    private Button clockInButton;
    private Button clockOutButton;
    private Button recordsButton;
    private Button hoursButton;
    private Button logoutButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        userText = (TextView) findViewById(R.id.userText);
        clockInButton = (Button) findViewById(R.id.clockInButton);
        clockOutButton = (Button) findViewById(R.id.clockOutButton);
        recordsButton = (Button) findViewById(R.id.recordsButton);
        hoursButton = (Button) findViewById(R.id.hoursButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        userText.setText("Welcome");
        //userText.setText(user.getEmail());

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        clockInButton.setOnClickListener(this);
        clockOutButton.setOnClickListener(this);
        recordsButton.setOnClickListener(this);
        hoursButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == logoutButton){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == clockInButton){
            // clock in activity here
            Date date = new Date();
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            String date_str = dateFormat.format(date);
            //String date_str = DateFormat.getDateTimeInstance().format(date);

            databaseReference.child(date_str).setValue("IN");
        }

        if(view == clockOutButton){
            // clock out activity here
            Date date = new Date();
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            String date_str = dateFormat.format(date);
            //String date_str = DateFormat.getDateTimeInstance().format(date);

            databaseReference.child(date_str).setValue("OUT");
        }

        if(view == recordsButton){
            // go to Records Activity
            finish();
            startActivity(new Intent(this, RecordsActivity.class));
        }

        if(view == hoursButton){
            // go to Hours Activity
            finish();
            startActivity(new Intent(this, HoursActivity.class));
        }
    }



}