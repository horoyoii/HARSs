package com.example.hars.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private FirebaseDatabase database;
    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;

    public FirebaseUtil(){
        database = FirebaseDatabase.getInstance();
        RootRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

    }

    public DatabaseReference getThisUserRef(){
        return RootRef.child("users").child("12345");
    }

    public DatabaseReference getSeatsRef(){
        return RootRef.child("seats");
    }
    public FirebaseAuth getAuth() {
        return mAuth;
    }
    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

}
