package com.example.todolistver2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistver2.Models.Note;
import com.example.todolistver2.R;

import java.util.List;

public class RecyclerViewNoteAdapter extends RecyclerView.Adapter<RecyclerViewNoteAdapter.MyViewHolder>{

    private Context context;
    private List<Note> notes;


    public RecyclerViewNoteAdapter(Context context, List<Note> notes){
        this.context = context;
        this.notes = notes;
    }


    @NonNull
    @Override
    public RecyclerViewNoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewNoteAdapter.MyViewHolder holder, int position) {
        holder.noteId.setText(String.valueOf(position + 1));
        holder.noteDescription.setText(notes.get(position).getDescription());
        holder.noteDate.setText(Note.convertLocalDateTimeToString(notes.get(position).getLocalDateTime()));
        holder.cardView.setCardBackgroundColor(notes.get(position).getCategory().getColor());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView noteDescription, noteDate, noteId;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteId = itemView.findViewById(R.id.note_id_number);
            noteDescription = itemView.findViewById(R.id.note_id_description);
            noteDate = itemView.findViewById(R.id.note_id_date);
            cardView = itemView.findViewById(R.id.item_note_card_view);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null){
                        //itemView.setSelected(!itemView.isSelected());
                        int position = getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            listener.onItemLongClick(itemView, position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public interface IOnItemClickListener{
        void onItemClick(View itemView, int position);
        void onItemLongClick(View itemView, int position);
    }

    IOnItemClickListener listener;

    public void setOnItemClickListener(IOnItemClickListener listener){
        this.listener = listener;
    }
}
