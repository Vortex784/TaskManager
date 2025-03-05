package com.maxte.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.maxte.taskmanager.database.Points;
import com.maxte.taskmanager.database.PointsDao;
import com.maxte.taskmanager.database.TaskDatabase;
import com.maxte.taskmanager.databinding.ActivityMainBinding;
import com.maxte.taskmanager.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView pointsTextView, userNameTextView, userEmailTextView;
    private ImageView userImageView;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogleSignIn;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateOptionsDialog();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tasks, R.id.nav_rewards, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Access views in nav_header_main.xml
        View headerView = navigationView.getHeaderView(0);
        pointsTextView = headerView.findViewById(R.id.pointsTextView);
        userNameTextView = headerView.findViewById(R.id.userNameTextView);
        userEmailTextView = headerView.findViewById(R.id.emailTextView);
        userImageView = headerView.findViewById(R.id.imageView);
        btnGoogleSignIn = headerView.findViewById(R.id.btnGoogleSignIn);
        btnSignOut = headerView.findViewById(R.id.btnSignOut);

        observePoints();
        // Get user data from intent
        String userName = getIntent().getStringExtra("user_name");
        String userEmail = getIntent().getStringExtra("user_email");
        String userPhoto = getIntent().getStringExtra("user_photo");

        // Update UI
        if (userName != null) userNameTextView.setText(userName);
        if (userEmail != null) userEmailTextView.setText(userEmail);
        if (userPhoto != null && !userPhoto.isEmpty()) {
            Glide.with(this).load(userPhoto).circleCrop().into(userImageView);
        } else {
            userImageView.setImageResource(R.drawable.ic_launcher_round); // Placeholder image
        }

        // Set click listener for Google Sign-In button
        btnGoogleSignIn.setOnClickListener(view -> handleSignInButtonClick());
        btnSignOut.setOnClickListener(v -> signOut());
        SignInButton btnGoogleSignIn = headerView.findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); // Start LoginActivity for sign-in
        });

        // Load existing user info if already signed in
        loadUserInfo();
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Clear user data
                userNameTextView.setText("Guest");
                userEmailTextView.setText("Not signed in");
                userImageView.setImageResource(R.drawable.ic_launcher_round);

                // Show sign-in button, hide sign-out button
                btnGoogleSignIn.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

                // Redirect to login screen (optional)
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Sign out failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles click on Google Sign-In button.
     */
    private void handleSignInButtonClick() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Already signed in
            Toast.makeText(this, "Already signed in as " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            // Start Google Sign-In flow
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        }
    }

    /**
     * Loads user information if already signed in.
     */
    private void loadUserInfo() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            userNameTextView.setText(account.getDisplayName());
            userEmailTextView.setText(account.getEmail());

            Uri photoUri = account.getPhotoUrl();
            if (photoUri != null) {
                Glide.with(this).load(photoUri).circleCrop().into(userImageView);
            } else {
                userImageView.setImageResource(R.drawable.ic_launcher_round);
            }

            // Show sign-out button, hide sign-in button
            btnGoogleSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Logged in as " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            userNameTextView.setText("Guest");
            userEmailTextView.setText("Not signed in");
            userImageView.setImageResource(R.drawable.ic_launcher_round);

            // Show sign-in button, hide sign-out button
            btnGoogleSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }

    // Handle Google Sign-In result
    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            });

    /**
     * Processes the result of the Google Sign-In.
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                loadUserInfo();
                Toast.makeText(this, "Signed in as " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w("Google Sign-In", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void observePoints() {
        PointsDao pointsDao = TaskDatabase.getInstance(getApplicationContext()).pointsDao();
        pointsDao.getPointsLiveData().observe(this, new Observer<Points>() {
            @Override
            public void onChanged(Points points) {
                if (points != null) {
                    pointsTextView.setText("Points: " + points.getPoints());
                } else {
                    pointsTextView.setText("Points: 0");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    /**
     * Show dialog for creating tasks or rewards.
     */
    private void showCreateOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Options")
                .setItems(new CharSequence[]{"Create Task", "Create Reward", "Cancel"}, (dialog, which) -> {
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                    switch (which) {
                        case 0:
                            navController.navigate(R.id.nav_create_task);
                            break;
                        case 1:
                            navController.navigate(R.id.nav_create_reward);
                            break;
                        case 2:
                            dialog.dismiss();
                            break;
                    }
                });
        builder.create().show();
    }
}
