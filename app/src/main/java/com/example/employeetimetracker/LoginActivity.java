package com.example.employeetimetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupText = (TextView) findViewById(R.id.signupText);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ClockActivity.class));
        }

        loginButton.setOnClickListener(this);
        signupText.setOnClickListener(this);
    }

    private void userLogin(){
        String em = emailText.getText().toString().trim();
        String pa = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(em) && TextUtils.isEmpty(pa)){
            Toast.makeText(this, "Please enter your email and password.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(TextUtils.isEmpty(em)){
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(pa)){
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mAuth.signInWithEmailAndPassword(em, pa)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ClockActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == loginButton){
            userLogin();
        }

        if(view == signupText){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
    }





}