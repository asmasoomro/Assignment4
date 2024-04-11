package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PurchaseScreen extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private String userId;
    private FirebaseStorage firebaseStorage;
    private EditText discountCode;
    private DatabaseReference databaseReference, databaseReference2,databaseReference3;
    private ValueEventListener dbListener;
    private List<Upload> uploads;
    private LinearLayout phonesContainer;
    private TextView totalPrice;
    private int total;
    private Button order,discButton;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_screen);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        uploads = new ArrayList<>();
        firebaseStorage = FirebaseStorageSingleton.getInstance();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("purchases").child(userId);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("checkout").child(userId);
        totalPrice = findViewById(R.id.totalPrice);

        discButton = (Button)findViewById(R.id.discount);
        discButton.setOnClickListener(this);
        phonesContainer = findViewById(R.id.Container);
        order = (Button)findViewById(R.id.placeOrder);
        order.setOnClickListener(this);
        dbListener = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uploads.clear();
                phonesContainer.removeAllViews();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);
                    displayPhoneItem(upload);
                }
                updateTotalPrice();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PurchaseScreen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPhoneItem(Upload upload) {
        LinearLayout itemContainer = new LinearLayout(this);
        itemContainer.setOrientation(LinearLayout.VERTICAL);

        // Create TextViews for the phone model, quantity, and price
        TextView phoneMan = new TextView(this);
        TextView phoneQuan = new TextView(this);
        TextView phonePrice = new TextView(this);
        TextView phoneName = new TextView(this);
        Button remove = new Button(this);
        Button increase = new Button(this);
        Button decrease = new Button(this);


        increase.setText("+");
        decrease.setText("-");

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(upload.getQuantity());
                int stock = Integer.parseInt(upload.getStock());
                if (currentQuantity < stock) {
                    currentQuantity++;
                    upload.setStock(String.valueOf(currentQuantity));
                    databaseReference2.child(upload.getKey()).child("quantity").setValue(String.valueOf(currentQuantity));
                    phoneQuan.setText("Quantity: " + currentQuantity);
                    updateTotalPrice();
                } else {
                    Toast.makeText(PurchaseScreen.this, "Not enough stock for " + upload.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(upload.getQuantity());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    upload.setQuantity(String.valueOf(currentQuantity));
                    databaseReference2.child(upload.getKey()).child("quantity").setValue(String.valueOf(currentQuantity));
                    phoneQuan.setText("Quantity: " + currentQuantity);
                    updateTotalPrice();
                } else {
                    Toast.makeText(PurchaseScreen.this, "Quantity should be at least 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        View v = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.setMargins(0, 16, 0, 16);
        v.setLayoutParams(layoutParams);
        v.setBackgroundColor(Color.parseColor("#FFD3D3D3"));
        LinearLayout quantityButtonsContainer = new LinearLayout(this);
        quantityButtonsContainer.setOrientation(LinearLayout.HORIZONTAL);
        quantityButtonsContainer.addView(decrease);
        quantityButtonsContainer.addView(increase);

        phoneName.setText("Name: " + upload.getName());
        phoneName.setTextColor(Color.BLACK);
        phoneName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        phoneMan.setText("Manufacturer: " + upload.getManufacturer());
        phoneMan.setTextColor(Color.BLACK);
        phoneMan.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        phoneQuan.setText("Quantity: " + upload.getQuantity());
        phoneQuan.setTextColor(Color.BLACK);
        phoneQuan.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        phonePrice.setText("Price: €" + upload.getPrice());
        phonePrice.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        itemContainer.addView(phoneName);
        itemContainer.addView(phoneMan);
        itemContainer.addView(phoneQuan);
        itemContainer.addView(phonePrice);
        itemContainer.addView(remove);
        itemContainer.addView(v);
        itemContainer.addView(quantityButtonsContainer);

        phonesContainer.addView(itemContainer);
        remove.setText("delete");

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference2.child(upload.getKey()).removeValue();
                phonesContainer.removeView(itemContainer);
                uploads.remove(upload);
                updateTotalPrice();
                Toast.makeText(PurchaseScreen.this, "Item Removed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateTotalPrice() {
        total = 0;
        for (Upload upload : uploads) {
            total += Integer.parseInt(upload.getPrice())* Integer.parseInt(upload.getQuantity());
        }
        totalPrice.setText("Total Price: €" + total);
    }

    private void updateStock(){
        for (Upload upload : uploads) {

            String origKey = upload.getOriginalItemKey();
            int currentStock = Integer.parseInt(upload.getStock());
            int quantity = Integer.parseInt(upload.getQuantity());
            if (currentStock >= quantity) {
                databaseReference.child(origKey).child("stock").setValue(Integer.toString(currentStock - quantity));
            } else {
                Toast.makeText(this, "This amount is not in stock for " + upload.getName(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.placeOrder) {
            validateCard();
        } else if (view.getId() == R.id.discount) {
            discount();
        }
    }


    private void discount() {
        discountCode = (EditText)findViewById(R.id.discountCde);
        String checkCode = discountCode.getText().toString().trim();

        if(checkCode.equalsIgnoreCase("sale24")&& count!=1){
            int disc=10;
            total =total - disc;
            totalPrice.setText("Total Price: €" + total);
            count++;
            Toast.makeText(this, "€10 Discount applied", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "This is not a current code or code applied", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateCard() {
        TextView customerName = (TextView) findViewById(R.id.Name);
        TextView customerNumber = (TextView) findViewById(R.id.card);
        TextView cardMonth = (TextView) findViewById(R.id.month);
        TextView cardYear = (TextView) findViewById(R.id.year);
        TextView cardCvv = (TextView) findViewById(R.id.cvv);

        String cn = customerName.getText().toString().trim();
        String c= customerNumber.getText().toString().trim();
        String m = cardMonth.getText().toString().trim();
        String y = cardYear.getText().toString().trim();
        String cVV= cardCvv.getText().toString().trim();


        if (cn.isEmpty()) {
            customerName.setError("Card holders name required");
            customerName.requestFocus();
            return;
        }
        if (c.isEmpty()|| c.length()!=16) {
            customerNumber.setError("Incorrect amount");
            customerNumber.requestFocus();
            return;

        }
        if (m.isEmpty() || m.length()!=2) {
            cardMonth.setError("month is required. /mm/ ");
            cardMonth.requestFocus();
            return;
        } if (y.isEmpty() || y.length()!=4) {
            cardYear.setError("year is required /yyyy");
            cardYear.requestFocus();
            return;
        } if (cVV.isEmpty() || cVV.length()!=3) {
            cardCvv.setError("cvv is required and only 3 numbers");
            cardCvv.requestFocus();
            return;
        }
        if(total==0){
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        updateStock();
        removeFromCheckout();
        savePurchase();

        startActivity(new Intent(PurchaseScreen.this,ConfirmationScreen.class));


    }

    private void savePurchase() {
        for (Upload upload : uploads) {
            String uploadId = databaseReference3.push().getKey();
            databaseReference3.child(uploadId).setValue(upload);
        }
    }
    private void removeFromCheckout() {
        databaseReference2.removeValue();

    }
}