package com.example.todolistver2.fragments.Notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.cardview.widget.CardView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Activity.AddNoteActivity;
import com.example.todolistver2.Activity.CategoryActivity;
import com.example.todolistver2.Activity.UpdateNoteActivity;
import com.example.todolistver2.Adapters.CategorySpinnerFilterAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Category;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;
import com.example.todolistver2.Adapters.RecyclerViewNoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment {
    FloatingActionButton buttonAddNote;
    RecyclerView recyclerView;
    RecyclerViewNoteAdapter recyclerViewAdapter;
    List<Note> notes;
    DbManager dbManager;
    Context context;
    CategorySpinnerFilterAdapter categorySpinnerAdapter;
    Spinner spinner;
    TextView tv;
    ImageView imgV;
    Category selected;
    private final ActivityResultLauncher<Intent> createNote = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Constants.NOTE_CREATE_ACTION){

                Intent intent = result.getData();
                if (intent != null ){
                    Note note = (Note)intent.getSerializableExtra(Constants.INTENT_CREATE_NOTE_KEY);
                    note.setItemIndex(notes.size());
                    notes.add(note);
                    dbManager.addNoteDatabase(note);
                    recyclerViewAdapter.notifyItemInserted(notes.size() - 1);
                }
            }
        }
    );

    private final ActivityResultLauncher<Intent> updateNote = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.NOTE_EDIT_ACTION){
                    Intent intent =result.getData();
                    if (intent != null){
                        Note note = (Note)intent.getSerializableExtra(Constants.INTENT_UPDATE_NOTE_KEY);
                        int index = intent.getIntExtra(Constants.INTENT_INDEX_KEY, Constants.INTENT_DEFAULT_VALUE);
                        if (index != Constants.INTENT_DEFAULT_VALUE){
                            notes.set(index, note);
                            recyclerViewAdapter.notifyItemChanged(index);
                        }
                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DbManager(requireActivity());
        context = getContext();
        notes = new ArrayList<>();
        //notes = dbManager.getAllNotesDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAddNote = view.findViewById(R.id.notes_act_btn_add);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        tv = view.findViewById(R.id.textView);
        imgV = view.findViewById(R.id.imageView);
        spinner = view.findViewById(R.id.notes_spinner);
        recyclerViewAdapter =new RecyclerViewNoteAdapter(context, notes);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        MenuHost menuHost = requireActivity();

        buttonAddNote.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AddNoteActivity.class);
            try {
                createNote.launch(intent);
            }
            catch (Exception ex){
                Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewNoteAdapter.IOnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(context, UpdateNoteActivity.class);
                try{
                    Note note = notes.get(position);
                    intent.putExtra(Constants.INTENT_INDEX_KEY, position);
                    intent.putExtra(Constants.INTENT_UPDATE_NOTE_KEY, note);
                    updateNote.launch(intent);
                }
                catch(Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                Note note = notes.get(position);
                CardView view1 = itemView.findViewById(R.id.item_note_card_view);
                view1.setCardBackgroundColor(getResources().getColor(R.color.teal_700, context.getTheme()));
                new AlertDialog.Builder(requireActivity())
                        .setTitle(getResources().getString(R.string.delete_selected_note))
                        .setNegativeButton("Да", (dialogInterface, i) -> {
                            notes.remove(position);
                            recyclerViewAdapter.notifyItemRemoved(position);
                            dbManager.deleteNoteDatabase(note.getItemIndex());
                            Toast.makeText(context, getResources().getString(R.string.deleted_note), Toast.LENGTH_SHORT).show();

                            if (selected.getName().equals(getResources().getString(R.string.category_all))){
                                notes = dbManager.getAllNotesDatabase();
                            }
                            else{
                                notes = dbManager.getFilteredNotesByCategory(selected.getName());
                            }
                            recyclerViewAdapter.setNotes(notes);
                            setNoDataView();
                        })
                        .setPositiveButton("Нет", (dialogInterface, i) -> {
                            Toast.makeText(context, getResources().getString(R.string.cancel_removing), Toast.LENGTH_SHORT).show();
                            view1.setCardBackgroundColor(note.getCategory().getColor());
                            dialogInterface.dismiss();

                        }).setCancelable(false).create().show();
            }
        });

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.notes_toolbar_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notes_toolbar_categories:
                        Intent intent = new Intent(context, CategoryActivity.class);
                        startActivity(intent);
                       break;
                }
                return true;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        List<Category> tmp = dbManager.getAllCategoriesDatabase();
        tmp.add(0,new Category(getResources().getString(R.string.category_all), -1));
        categorySpinnerAdapter = new CategorySpinnerFilterAdapter(context, tmp);
        spinner.setAdapter(categorySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (Category) spinner.getSelectedItem();
                if(selected.getName().equals(getResources().getString(R.string.category_all))){
                    notes = dbManager.getAllNotesDatabase();
                    recyclerViewAdapter.setNotes(notes);
                }
                else{
                    notes = dbManager.getFilteredNotesByCategory(selected.getName());
                    recyclerViewAdapter.setNotes(notes);
                }
                setNoDataView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        notes = dbManager.getAllNotesDatabase();
        recyclerViewAdapter.setNotes(notes);
        List<Category> tmp = dbManager.getAllCategoriesDatabase();
        tmp.add(0,new Category(getResources().getString(R.string.category_all), -1));
        categorySpinnerAdapter.setCategoryList(tmp);
        spinner.setSelection(categorySpinnerAdapter.getItemId(getResources().getString(R.string.category_all)));
        setNoDataView();
    }

    private void setNoDataView(){
        if(notes.size() == 0){
            tv.setVisibility(View.VISIBLE);
            imgV.setVisibility(View.VISIBLE);
        }
        else {
            tv.setVisibility(View.GONE);
            imgV.setVisibility(View.GONE);
        }
    }
}