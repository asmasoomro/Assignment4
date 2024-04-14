package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeScreenAdmin extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Boolean isOpen=false;
    Button view,newItem,create;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_admin);

        newItem = (Button) findViewById(R.id.addItem);
        newItem.setOnClickListener(this);

        view = (Button) findViewById(R.id.viewItems);
        view.setOnClickListener(this);

        create = (Button) findViewById(R.id.createAdmin);
        create.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference  = FirebaseDatabase.getInstance().getReference("admin");
        userId = user.getUid();

        Button lgOut = (Button) findViewById(R.id.logout);
        lgOut.setOnClickListener(this);
        Button showUserInfoButton = findViewById(R.id.showUserInfo);
        showUserInfoButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
        } else if (view.getId() == R.id.addItem) {
            startActivity(new Intent(this, addItem.class));
        } else if (view.getId() == R.id.viewItems) {
            startActivity(new Intent(this, ItemActivity.class));
        } else if (view.getId() == R.id.createAdmin) {
            startActivity(new Intent(this, AdminSignup.class));
        } else if (view.getId() == R.id.showUserInfo) {
            showUserInfo();
        }
    }

    private void showUserInfo() {
        // Get a reference to the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Query the database to get all users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    if (userId != null) {
                        // Retrieve and display the user's information
                        String userName = userSnapshot.child("name").getValue(String.class);
                        String userEmail = userSnapshot.child("email").getValue(String.class);

                        // Display the user details
                        String userInfo = "User ID: " + userId + "\nName: " + userName + "\nEmail: " + userEmail + "\n\n";
                        Toast.makeText(HomeScreenAdmin.this, userInfo, Toast.LENGTH_LONG).show();

                        // Retrieve and display the user's purchase history
                        DataSnapshot purchasesSnapshot = userSnapshot.child("user_purchases");
                        if (purchasesSnapshot.exists()) {
                            StringBuilder purchaseInfo = new StringBuilder("Purchase History:\n");
                            for (DataSnapshot purchase : purchasesSnapshot.getChildren()) {
                                String purchaseDetails = purchase.child("itemName").getValue(String.class);
                                purchaseInfo.append("- ").append(purchaseDetails).append("\n");
                            }
                            Toast.makeText(HomeScreenAdmin.this, purchaseInfo.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HomeScreenAdmin.this, "No purchase history found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors or interruptions in database retrieval
                Toast.makeText(HomeScreenAdmin.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

