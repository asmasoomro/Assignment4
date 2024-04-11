package com.example.assignment4;

import com.google.firebase.storage.FirebaseStorage;

public class FirebaseStorageSingleton {
    private static FirebaseStorage instance;

    private FirebaseStorageSingleton(){

    }
    public static FirebaseStorage getInstance(){
        if(instance == null){
            instance =FirebaseStorage.getInstance();
        }
        return instance;
    }
}