package com.maxte.taskmanager.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.maxte.taskmanager.MainActivity;
import com.maxte.taskmanager.R;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private SignInButton btnGoogleSignIn;




    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                } else {
                    Toast.makeText(this, "Sign-In canceled or failed.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(v -> signIn());
        checkIfAlreadySignedIn();
    }

    private void checkIfAlreadySignedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Show Toast message
            Toast.makeText(this, "Already signed in as " + account.getDisplayName(), Toast.LENGTH_LONG).show();

            // Redirect to MainActivity
            goToMainActivity(account);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.d("LoginActivity", "Initiating sign-in");
        signInLauncher.launch(signInIntent);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user_name", account.getDisplayName());
                intent.putExtra("user_email", account.getEmail());
                String photoUrl = "";
                if (account.getPhotoUrl() != null) {
                    photoUrl = account.getPhotoUrl().toString();
                }

                intent.putExtra("user_photo", photoUrl);
                startActivity(intent);
                finish(); // Close LoginActivity after passing data
            }
        } catch (ApiException e) {
            // This is where we capture the error and log more details
            Log.e("Google Sign-In", "signInResult:failed code=" + e.getStatusCode(), e);
            Toast.makeText(this, "Sign-In failed. Error Code: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();

            // You can show more detailed error messages based on the error code
            switch (e.getStatusCode()) {
                case 12501:
                    Toast.makeText(this, "Sign-In was canceled by the user.", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    Toast.makeText(this, "App is not configured correctly. Please check your Google API setup.", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(this, "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * Redirects to MainActivity with user details.
     */
    private void goToMainActivity(GoogleSignInAccount account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_name", account.getDisplayName());
        intent.putExtra("user_email", account.getEmail());
        intent.putExtra("user_photo", account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
        startActivity(intent);
        finish();
    }
}

