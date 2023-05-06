package com.example.todolistver2.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Models.Task;
import com.example.todolistver2.R;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class RecyclerViewTaskAdapter extends RecyclerView.Adapter<RecyclerViewTaskAdapter.MyViewHolder> {

    Context context;
    List<Task> taskList;

    IOnItemClickListener listener;

    public RecyclerViewTaskAdapter(Context context, List<Task>taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerViewTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recycler_view_tasks, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTaskAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(taskList.get(position).getName());
        holder.cardView.setCardBackgroundColor(taskList.get(position).getColor());
        if (taskList.get(position).getCompleted()){
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String date = LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear();
            holder.tvDate.setText("Завершено " + date);
        }
        else{
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvDate.setText(taskList.get(position).getDate().format(Constants.format_dd_MM_YYYY));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_task_id_card_view);
            tvName = itemView.findViewById(R.id.item_task_id_task_name);
            tvDate = itemView.findViewById(R.id.item_task_id_task_date);

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null){
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

    public void setOnItemClickListener(IOnItemClickListener listener){
        this.listener = listener;
    }
}
