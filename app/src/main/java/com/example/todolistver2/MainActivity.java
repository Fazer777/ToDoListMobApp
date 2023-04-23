package com.example.todolistver2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavBar;
    private NavController navController;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();


    }


    @Override
    protected void onStart() {
        super.onStart();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavBar, navController);
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController);


    }

    private void initElements() {
        bottomNavBar = findViewById(R.id.bottom_nav_menu);
        toolbar = findViewById(R.id.main_act_toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
        return navController.navigateUp()|| super.onSupportNavigateUp();
    }
}