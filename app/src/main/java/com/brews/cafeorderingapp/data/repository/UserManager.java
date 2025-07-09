package com.brews.cafeorderingapp.repository;

import com.google.firebase.auth.FirebaseAuth;

public class UserManager {
    private static UserManager instance;
    private String userId;

    private UserManager() {
        // Load userId from Firebase or SharedPreferences
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

}

