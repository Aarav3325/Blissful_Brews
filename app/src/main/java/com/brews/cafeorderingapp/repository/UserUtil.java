package com.brews.cafeorderingapp.repository;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Random;

public class UserUtil {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static String getUserId() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public static String getUserName() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }

    public static String getUserEmail() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public static Uri getProfilePic() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
    }

    public static void logout(){
        firebaseAuth.signOut();
    }

    private static int generateRandomNumber() {
        int number = 1000 + new Random().nextInt(9000);
        return number;
    }

}
