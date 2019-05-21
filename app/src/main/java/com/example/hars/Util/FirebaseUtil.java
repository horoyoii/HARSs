package com.example.hars.Util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private FirebaseDatabase database;
    private DatabaseReference RootRef;

    public FirebaseUtil(){
        database = FirebaseDatabase.getInstance();
        RootRef = database.getReference();

    }

    public DatabaseReference getThisUserRef(){
        return RootRef.child("users").child("12345");
    }

    public DatabaseReference getSeatsRef(){
        return RootRef.child("seats");
    }


}
