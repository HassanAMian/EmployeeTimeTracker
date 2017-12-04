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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView signinText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signupButton = (Button) findViewById(R.id.signupButton);
        signinText = (TextView) findViewById(R.id.signinText);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ClockActivity.class));
        }

        signupButton.setOnClickListener(this);
        signinText.setOnClickListener(this);
    }

    private void userRegistration() {
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

        mAuth.createUserWithEmailAndPassword(em, pa)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else{
                            Toast.makeText(SignInActivity.this, "Unable to sign up.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == signupButton){
            userRegistration();
        }

        if(view == signinText){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}
