package com.example.taskmanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.content.DialogInterface;
import android.widget.TextView;

import com.example.taskmanager.database.Points;
import com.example.taskmanager.database.PointsDao;
import com.example.taskmanager.database.TaskDatabase;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.taskmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView pointsTextView;

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

        View headerView = navigationView.getHeaderView(0);
        pointsTextView = headerView.findViewById(R.id.pointsTextView);

        observePoints();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tasks, R.id.nav_rewards, R.id.nav_settings, R.id.nav_create_reward, R.id.nav_create_task)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            if (navController.getCurrentDestination() != null &&
                    (navController.getCurrentDestination().getId() == R.id.nav_create_task ||
                            navController.getCurrentDestination().getId() == R.id.nav_create_reward)) {
                navController.navigateUp(); // Navigate back
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showCreateOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Options")
                .setItems(new CharSequence[]{"Create Task", "Create Reward", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        switch (which) {
                            case 0:
                                // Navigate to the create task fragment
                                navController.navigate(R.id.nav_create_task);
                                break;
                            case 1:
                                // Navigate to the create reward fragment
                                navController.navigate(R.id.nav_create_reward);
                                break;
                            case 2:
                                // Cancel option, dismiss the dialog
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}