package com.example.todolistver2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Adapters.CategoryAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;
import com.example.todolistver2.Models.Category;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddNoteActivity extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    Menu addNoteMenu;
    EditText etDescriptionNote;
    TextView tvDateTime;
    Spinner spCategory;

    DbManager dbManager;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initLayoutElements();
        dbManager = new DbManager(AddNoteActivity.this);
        categoryAdapter = new CategoryAdapter(AddNoteActivity.this);

        setSupportActionBar(toolbar);

        tvDateTime.setText(Constants.convertLocalDateTimeToString(
                LocalDateTime.now(ZoneId.of("Europe/Moscow"))));



        spCategory.setAdapter(categoryAdapter);
    }

    private void initLayoutElements() {
        toolbar = findViewById(R.id.add_note_id_toolbar);
        etDescriptionNote = findViewById(R.id.add_note_id_description);
        tvDateTime = findViewById(R.id.add_note_id_date_time_view);
        spCategory = findViewById(R.id.add_note_id_spinner);
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
            createNote();
        }
        return super.onOptionsItemSelected(item);
    }

    // Метод создания новой заметки
    private void createNote() {
        Note note = new Note();
        note.setDescription(etDescriptionNote.getText().toString());
        Category category = (Category) spCategory.getSelectedItem();
        note.setCategory(category);
        note.setLocalDateTime(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        dbManager.addNoteDatabase(note);
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_CREATE_NOTE_KEY, note);
        setResult(Constants.NOTE_CREATE_ACTION, intent);
        finish();
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