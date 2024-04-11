package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignup extends AppCompatActivity implements View.OnClickListener {

    ;
    private Button createUser;
    private EditText fname, lname, adminEmail, pwrd;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        mAuth = FirebaseAuth.getInstance();

        createUser = (Button) findViewById(R.id.signUp);
        createUser.setOnClickListener(this);


        fname = (EditText) findViewById(R.id.fn);
        lname = (EditText) findViewById(R.id.ln);
        adminEmail = (EditText) findViewById(R.id.email);
        pwrd = (EditText) findViewById(R.id.pswd);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUp) {
            registerUser();
        }
    }


    private void registerUser() {

        String firstName = fname.getText().toString().trim();
        String lastName = lname.getText().toString().trim();
        String password = pwrd.getText().toString().trim();
        String email = adminEmail.getText().toString().trim();
        boolean admin= true;


        if (firstName.isEmpty()) {
            fname.setError("First name required");
            fname.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            lname.setError("Last name required");
            lname.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            adminEmail.setError("Appropriate email required");
            adminEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pwrd.setError("password required");
            pwrd.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pwrd.setError("Must be 6 characters");
            pwrd.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Admin admin1 = new Admin(firstName, lastName, password, email,admin);
                            FirebaseDatabase.getInstance().getReference("admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AdminSignup.this, "Admin has been added!", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(AdminSignup.this, "Failed to register", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AdminSignup.this, "Failed to register", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}