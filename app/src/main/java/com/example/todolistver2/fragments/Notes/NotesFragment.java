package com.example.todolistver2.fragments.Notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todolistver2.Activity.AddNoteActivity;
import com.example.todolistver2.Activity.MainActivity;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;
import com.example.todolistver2.RecyclerViewAdapter.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment {
    FloatingActionButton buttonAddNote;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Note> notes;

    private final ActivityResultLauncher<Intent> createNote = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            Toast.makeText(getActivity(), "Create raise", Toast.LENGTH_SHORT).show();
        }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        notes = new ArrayList<>();
        notes.add(new Note("Test", LocalDateTime.now(), "TestCategory"));
        notes.add(new Note("Test", LocalDateTime.now(), "TestCategory"));
        notes.add(new Note("Test", LocalDateTime.now(), "TestCategory"));
        recyclerViewAdapter =new RecyclerViewAdapter(getActivity(), notes);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(view).navigate(R.id.action_notesFragment_to_addNoteFragment);
                Intent intent = new Intent((MainActivity)getActivity(), AddNoteActivity.class);
                try {
                    createNote.launch(intent);
                }
                catch (Exception ex){
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}