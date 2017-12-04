package com.example.employeetimetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView recordsList;
    private Button clearButton;
    private Button backButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ArrayList<String> records = new ArrayList<>();
    private ArrayList<String> recordKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordsList = (ListView) findViewById(R.id.recordsList);
        clearButton = (Button) findViewById(R.id.clearButton);
        backButton = (Button) findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        final RecordListAdapter arrayAdapter = new RecordListAdapter(this, R.layout.recordslist_adapter, records, this);
        recordsList.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);

                records.add(key + " : " + value);
                recordKeys.add(key);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);

                int index = recordKeys.indexOf(key);

                records.set(index, key + " : " + value);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);

                int index = recordKeys.indexOf(key);

                records.remove(index);
                recordKeys.remove(index);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                records.clear();

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        clearButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == clearButton){
            // clear all the records
            FirebaseUser user = mAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
            databaseReference.removeValue();
        }

        if(view == backButton){
            // go back to Clock Activity
            finish();
            startActivity(new Intent(this, ClockActivity.class));
        }

    }
}

