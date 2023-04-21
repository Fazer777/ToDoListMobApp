package com.example.todolistver2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.RecyclerViewAdapter.RecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavBar;
    private NavController navController;
    private RecyclerView recyclerView;
    private FloatingActionButton buttonAddNote;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DbManager dbManager;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DbManager(MainActivity.this);
        initElements();


        //Получение данных из БД при запуске активности
        //notes = dbManager.getAllNotes()


        buttonAddNote.setOnClickListener(view -> {

        });

        recyclerViewAdapter.setOnItemClickListener((itemView, position) -> {

        });


        //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.notesFragment2);
    }

    //Метод загрузки данных из БД при запуске активности


    @Override
    protected void onStart() {
        super.onStart();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavBar, navController);

    }

    private void initElements() {
        bottomNavBar = findViewById(R.id.bottom_nav_menu);
        recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(this, notes);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
        return navController.navigateUp()|| super.onSupportNavigateUp();
    }
}