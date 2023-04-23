package com.example.todolistver2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddNoteActivity extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    Menu addNoteMenu;
    EditText etDescriptionNote;
    TextView tvDateTime;
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initLayoutElements();

        dbManager = new DbManager(this);
        setSupportActionBar(toolbar);

        tvDateTime.setText(Note.convertLocalDateTimeToString(
                LocalDateTime.now(ZoneId.of("Europe/Moscow"))));

    }

    private void initLayoutElements() {
        toolbar = findViewById(R.id.add_note_id_toolbar);
        etDescriptionNote = findViewById(R.id.add_note_id_description);
        tvDateTime = findViewById(R.id.add_note_id_date_time_view);
        etDescriptionNote.addTextChangedListener(this);
    }

    private void showHideCreateIcon(boolean show){
        addNoteMenu.findItem(R.id.menu_add_note_toolbar_create).setVisible(show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addNoteMenu = menu;
        getMenuInflater().inflate(R.menu.add_note_toolbar_menu, menu);
        showHideCreateIcon(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_note_toolbar_create){

            //Toast.makeText(this, "Good!", Toast.LENGTH_SHORT).show();
            createNote();
        }
        return super.onOptionsItemSelected(item);
    }

    // Метод создания новой заметки
    private void createNote() {
        Toast.makeText(this, "Testd\nTestG\n" + Note.convertLocalDateTimeToString(
                LocalDateTime.now(ZoneId.of("Europe/Moscow"))), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        showHideCreateIcon(!TextUtils.isEmpty(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}