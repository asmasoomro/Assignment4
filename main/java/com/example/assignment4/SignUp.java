package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private Button createUser, homeScreen;
    private EditText fname, lname, em, pwrd,ad1,ad2,cty;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        createUser = (Button) findViewById(R.id.button);
        createUser.setOnClickListener(this);

        homeScreen = (Button) findViewById(R.id.button2);
        homeScreen.setOnClickListener(this);


        fname = (EditText) findViewById(R.id.editTextTextPersonName3);
        lname = (EditText) findViewById(R.id.editTextTextPersonName4);
        em = (EditText) findViewById(R.id.editTextTextPersonName5);
        pwrd = (EditText) findViewById(R.id.editTextTextPersonName6);
        ad1=(EditText) findViewById(R.id.address1);
        ad2=(EditText) findViewById(R.id.address2);
        cty=(EditText) findViewById(R.id.county);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (view.getId() == R.id.button) {
            registerUser();
        }
    }


    private void registerUser() {
        String firstName = fname.getText().toString().trim();
        String lastName = lname.getText().toString().trim();
        String password = pwrd.getText().toString().trim();
        String email = em.getText().toString().trim();
        String address1 =ad1.getText().toString().trim();
        String address2 = ad2.getText().toString().trim();
        String county = cty.getText().toString().trim();
        String address = address1+", "+ address2+ ", "+ county;
        int purchaseCount=0;


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
            em.setError("Appropriate email required");
            em.requestFocus();
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
        if (address1.isEmpty()) {
            ad1.setError("Address line 1 required");
            ad1.requestFocus();
            return;
        }
        if (address2.isEmpty()) {
            ad2.setError("Address line 2 required");
            ad2.requestFocus();
            return;
        }
        if (county.isEmpty()) {
            cty.setError("County required");
            cty.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(firstName, lastName, password, email,address,purchaseCount);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "User has been added!", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(SignUp.this, "Failed to register", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this, "Failed to register", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}