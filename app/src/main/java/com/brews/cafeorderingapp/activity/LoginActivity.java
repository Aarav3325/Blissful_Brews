package com.brews.cafeorderingapp.activity;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.brews.cafeorderingapp.R;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog pd;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText loginEmail = findViewById(R.id.loginEmail);
        EditText loginPassword = findViewById(R.id.loginPassword);
        TextView signUpRedirect = findViewById(R.id.signUpRedirect);
        Button signInBtn = findViewById(R.id.signInBtn);
        ImageView googleSignInBtn = findViewById(R.id.googleSignInBtn);

        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, NewMenu.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1037942784085-tvq81buj17666tjvdmfid76kifbrgemp.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        googleSignInBtn.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        signUpRedirect.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        signInBtn.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            loginUser(email, password);
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        pd.setMessage("Logging In");
        pd.show();
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);
                    if (account != null) {
                        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(this, task -> {
                                    pd.dismiss();
                                    if (task.isSuccessful() && !isFinishing()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        startActivity(new Intent(LoginActivity.this, NewMenu.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                        displayToast("Welcome back " + user.getDisplayName());
                                    } else {

                                        Log.i("MYTAG", "Login Failed" + task.getException().getMessage());
                                        displayToast("Authentication Failed: " + task.getException().getMessage());
                                        Log.e("LoginError", "Google Sign-In Error", task.getException());
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    pd.dismiss();
                                    displayToast("Login failed: " + e.getMessage());
                                    Log.i("MYTAG", "Login Failed" + e.getMessage());
                                });
                    }
                } catch (ApiException e) {
                    pd.dismiss();
                    Log.e(TAG, "Google sign-in failed", e);
                }
            } else {
                pd.dismiss();
                Log.e("GoogleSignIn", "Task failed", signInAccountTask.getException());
                displayToast("Google sign-in cancelled or failed");
            }
        }
    }

    private void loginUser(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            pd.setMessage("Seasoning your experience... Please wait!");
            pd.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        pd.dismiss();
                        if (!isFinishing()) {
                            startActivity(new Intent(LoginActivity.this, NewMenu.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        displayToast("Login failed: " + e.getMessage());
                    });
        } else {
            displayToast("Please fill in both email and password.");
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        super.onDestroy();
    }
}
