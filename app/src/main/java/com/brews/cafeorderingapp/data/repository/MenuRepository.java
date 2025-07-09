package com.brews.cafeorderingapp.repository;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuRepository {
    private Application application;
    private DatabaseReference databaseReference;

    public MenuRepository(Application application) {
        this.application = application;
        databaseReference = FirebaseDatabase.getInstance().getReference("newmenu");
    }

    public void fetchMenu(ValueEventListener listener){
        databaseReference.addValueEventListener(listener);
    }


}
