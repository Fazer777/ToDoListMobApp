package com.example.todolistver2.fragments.Notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.todolistver2.Activity.AddNoteActivity;

import com.example.todolistver2.Activity.UpdateNoteActivity;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;
import com.example.todolistver2.Adapters.RecyclerViewNoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesFragment extends Fragment {
    FloatingActionButton buttonAddNote;
    RecyclerView recyclerView;
    RecyclerViewNoteAdapter recyclerViewAdapter;
    List<Note> notes;
    DbManager dbManager;
    Context context;
//    MenuHost menuHost;
//    Menu toolbarMenu;
    private final ActivityResultLauncher<Intent> createNote = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Constants.NOTE_CREATE_ACTION){
                Toast.makeText(getActivity(), "Create raise", Toast.LENGTH_SHORT).show();
                Intent intent = result.getData();
                if (intent != null ){
                    Note note = (Note)intent.getSerializableExtra(Constants.INTENT_CREATE_NOTE_KEY);
                    notes.add(Constants.INSERT_POSITION, note);
                    recyclerViewAdapter.notifyItemInserted(Constants.INSERT_POSITION);
                }
            }
        }
    );

    private final ActivityResultLauncher<Intent> updateNote = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.NOTE_EDIT_ACTION){
                    Toast.makeText(getActivity(), "Update raise", Toast.LENGTH_SHORT).show();
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
        notes = dbManager.getAllNotesDatabase();
        Collections.reverse(notes);
        Toast.makeText(context, "FragmentOnCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        menuHost = requireActivity();
//        menuHost.addMenuProvider(new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menuInflater.inflate(R.menu.toolbar_notes_fragment, menu);
//                toolbarMenu = menu;
//                showDeleteIcon(false);
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                if (menuItem.getItemId() == R.id.menu_notes_fragment_toolbar_delete){
//                    Toast.makeText(context, "Delete is work", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        buttonAddNote = view.findViewById(R.id.notes_act_btn_add);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        recyclerViewAdapter =new RecyclerViewNoteAdapter(context, notes);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNoteActivity.class);
                try {
                    createNote.launch(intent);
                }
                catch (Exception ex){
                    Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

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
                view1.setCardBackgroundColor(getResources().getColor(R.color.teal_700));
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Do you want to remove this note ?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(position);
                                recyclerViewAdapter.notifyItemRemoved(position);
                                dbManager.deleteNoteDatabase(position + 1);
                                Toast.makeText(context, "Delete!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Not delete", Toast.LENGTH_SHORT).show();
                                view1.setCardBackgroundColor(note.getCategory().getColor());
                                dialogInterface.dismiss();

                            }
                        }).create().show();

            }
        });
    }

//    private void showDeleteIcon(boolean show) {
//        toolbarMenu.findItem(R.id.menu_notes_fragment_toolbar_delete).setVisible(show);
//    }


    @Override
    public void onStart() {
        super.onStart();
    }

}