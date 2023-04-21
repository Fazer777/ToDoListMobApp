package com.example.todolistver2.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private Context context;
    private List<Note> notes;

    public RecyclerViewAdapter(Context context, List<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.noteId.setText(String.valueOf(position + 1));
        holder.noteDescription.setText(notes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        // TODO Доделать noteDateTime, не забудь также добавить view в item_recyclerView
        TextView noteDescription, noteDateTime, noteId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteId = itemView.findViewById(R.id.note_id_number);
            noteDescription = itemView.findViewById(R.id.note_id_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    // Одна из реализаций нажатия на элемент списка
    public interface IOnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    IOnItemClickListener listener;

    public void setOnItemClickListener(IOnItemClickListener listener){
        this.listener = listener;
    }
}
