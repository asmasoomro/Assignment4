package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signup, login,admin;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference reference1,reference2;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.editTextTextPersonName);
        password = (EditText) findViewById(R.id.editTextTextPassword);

        signup = (Button) findViewById(R.id.button1);
        signup.setOnClickListener(this);

        login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(this);

        admin=(Button) findViewById(R.id.adminSignIn);
        admin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            startActivity(new Intent(this, SignUp.class));
        } else if (view.getId() == R.id.Login) {
            userLogin();
        } else if (view.getId() == R.id.adminSignIn) {
            adminLogin();
        }
    }


    private void userLogin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userEmail.isEmpty()){
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if(userPassword.isEmpty()){
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        if(userPassword.length()<6){
            password.setError("Password must be longer than 6 characters");
            password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser login =FirebaseAuth.getInstance().getCurrentUser();

                    reference2  = FirebaseDatabase.getInstance().getReference("users");
                    userId=login.getUid();


                    reference2.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userAcc = snapshot.getValue(User.class);
                            if (userAcc != null) {
                                startActivity(new Intent(MainActivity.this, HomePage.class));

                            } else{
                                Toast.makeText(MainActivity.this,"You do not have an User account", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_LONG).show();

                        }
                    });


                }else{
                    Toast.makeText(MainActivity.this,"Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void adminLogin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userEmail.isEmpty()){
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if(userPassword.isEmpty()){
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        if(userPassword.length()<6){
            password.setError("Password must be longer than 6 characters");
            password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser login =FirebaseAuth.getInstance().getCurrentUser();

                    reference1  = FirebaseDatabase.getInstance().getReference("admin");
                    userId=login.getUid();


                    reference1.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Admin adminAcc = snapshot.getValue(Admin.class);
                            if (adminAcc != null) {

                                boolean admin = adminAcc.getAdmin();
                                if (admin = true) {
                                        startActivity(new Intent(MainActivity.this, HomeScreenAdmin.class));

                                }

                            } else{
                                Toast.makeText(MainActivity.this,"You do not have an admin account", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_LONG).show();

                        }
                    });


                }else{
                    Toast.makeText(MainActivity.this,"Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}