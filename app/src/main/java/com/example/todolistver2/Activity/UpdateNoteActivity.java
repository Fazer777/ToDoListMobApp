package com.example.todolistver2.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.todolistver2.Adapters.CategoryAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Category;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class UpdateNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    Menu updateNoteMenu;
    EditText etDescriptionNote;
    TextView tvDateTime;
    Spinner spCategory;
    DbManager dbManager;
    CategoryAdapter categoryAdapter;
    private int noteIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initLayoutElements();
        noteIndex = 0;
        dbManager = new DbManager(UpdateNoteActivity.this);
        categoryAdapter = new CategoryAdapter(UpdateNoteActivity.this);
        spCategory.setAdapter(categoryAdapter);
        setSupportActionBar(toolbar);
        getIntentAndSetData();

    }

    private void getIntentAndSetData() {
        Intent intent = getIntent();
        if (intent != null) {
            try {
                noteIndex = intent.getIntExtra(Constants.INTENT_INDEX_KEY, Constants.INTENT_DEFAULT_VALUE);
                if (noteIndex == Constants.INTENT_DEFAULT_VALUE) {
                    throw new Exception("Note's index is below zero");
                }
                Note note = (Note) intent.getSerializableExtra(Constants.INTENT_UPDATE_NOTE_KEY);
                etDescriptionNote.setText(note.getDescription());
                tvDateTime.setText(Constants.convertLocalDateTimeToString(note.getLocalDateTime()));
                spCategory.setSelection(categoryAdapter.getItemId(note.getCategory().getName()));
            }
            catch (Exception ex) {
                Toast.makeText(UpdateNoteActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    private void initLayoutElements() {
        toolbar = findViewById(R.id.add_note_id_toolbar);
        etDescriptionNote = findViewById(R.id.add_note_id_description);
        tvDateTime = findViewById(R.id.add_note_id_date_time_view);
        spCategory = findViewById(R.id.add_note_id_spinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateNoteMenu = menu;
        getMenuInflater().inflate(R.menu.add_note_toolbar_menu, menu);
        updateNoteMenu.findItem(R.id.menu_add_note_toolbar_create).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_note_toolbar_create){
            updateNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote() {

        Note note = new Note();
        note.setDescription(etDescriptionNote.getText().toString());
        Category category = (Category) spCategory.getSelectedItem();
        note.setCategory(category);
        note.setLocalDateTime(LocalDateTime.now(ZoneId.of("Europe/Moscow")));

        dbManager.updateNoteDatabase(noteIndex + 1, note);

        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_INDEX_KEY, noteIndex);
        intent.putExtra(Constants.INTENT_UPDATE_NOTE_KEY, note);
        setResult(Constants.NOTE_EDIT_ACTION, intent);
        finish();
    }
}
