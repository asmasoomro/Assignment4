package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class addItem extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button chooseImage,upload,showItems;
    private EditText prodName,prodCategory,prodManufacturer,stock,price;
    private ImageView imageView;
    private ProgressBar pBar;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        chooseImage = findViewById(R.id.upload);
        upload=findViewById(R.id.saveDetails);
        prodName =findViewById(R.id.productName);
        prodCategory =findViewById(R.id.productCategory);
        prodManufacturer = findViewById(R.id.productManufacturer);
        stock = findViewById(R.id.productStock);
        imageView = findViewById(R.id.imageView2);
        pBar = findViewById(R.id.progressBar);
        showItems = findViewById(R.id.viewItems);
        price=findViewById(R.id.productPrice);

        storageReference = FirebaseStorageSingleton.getInstance().getReference("items");
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(addItem.this, "Upload in progress",Toast.LENGTH_SHORT).show();
                }else {
                    uploadItems();
                }
            }
        });
        showItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagesActivity();

            }
        });

    }

    private void openImagesActivity() {
        Intent intent = new Intent(this,ItemActivity.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadItems() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(addItem.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                            // Get the download URL asynchronously
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    Upload upload = new Upload(prodName.getText().toString().trim(), downloadUrl.toString(), prodCategory.getText().toString().trim(), prodManufacturer.getText().toString().trim(), stock.getText().toString().trim(), price.getText().toString().trim());

                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(upload);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&& resultCode == RESULT_OK
                && data!=null && data.getData()!=null){

            imageUri=data.getData();
            Picasso.get().load(imageUri).into(imageView);

        }
    }
}