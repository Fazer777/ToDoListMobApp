package com.example.todolistver2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavBar;
    private NavController navController;
    private Toolbar toolbar;
    //ISelectedBundle selectedBundle;


    DbManager dbManager;
    //NotesFragment notesFragment = new NotesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavBar, navController);

        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }
        catch (Exception ex){
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        loadDataFromDatabase();


    }

    private void loadDataFromDatabase() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void initElements() {
        bottomNavBar = findViewById(R.id.bottom_nav_menu);
        toolbar = findViewById(R.id.main_act_toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp()|| super.onSupportNavigateUp();
    }


//    public interface ISelectedBundle{
//        void onBundleSelect(Bundle bundle);
//    }
//
//    public void setOnBundleSelected(ISelectedBundle selectedBundle){
//        this.selectedBundle = selectedBundle;
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}