package com.example.todolistver2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Models.TimerTask;
import com.example.todolistver2.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTimerAdapter extends RecyclerView.Adapter<RecyclerViewTimerAdapter.MyViewHolder> {

    Context context;
    List<TimerTask> timerTasks;
    List<TimerTask> originalValues;
    IOnItemClickListener listener;

    public RecyclerViewTimerAdapter(Context context, List<TimerTask> timerTasks){
        this.context = context;
        this.timerTasks = timerTasks;
        originalValues = timerTasks;
    }

    public void setOnItemClickListener(IOnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewTimerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO поменял контекст
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recycler_view_timer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTimerAdapter.MyViewHolder holder, int position) {
        TimerTask timerTask = timerTasks.get(position);
        holder.tvTaskName.setText(timerTask.getName());
        holder.tvTaskDate.setText(Constants.convertLocalDateTimeToString(timerTask.getDateTime()));
        holder.tvTaskTime.setText(timerTask.getTime().format(Constants.timeFormat_HH_mm_ss));
        holder.cvContainerTimer.setCardBackgroundColor(timerTask.getColorTask());

    }

    @Override
    public int getItemCount() {
        return timerTasks.size();
    }

    public void filterByDate(String date) {
        timerTasks = originalValues;
        if (!date.matches("")) {
            List<TimerTask> filteredItems = new ArrayList<>();
            for (TimerTask item : timerTasks) {
                if (item.getDateTime().format(Constants.format_dd_MM_YYYY).equals(date)) {
                    filteredItems.add(item);
                }
            }
            timerTasks = filteredItems;
        }
//        else {
//            tasks = originalValues;
//        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cvContainerTimer;
        TextView tvTaskName, tvTaskDate, tvTaskTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvContainerTimer = itemView.findViewById(R.id.item_timer_task_id_card_view);
            tvTaskName = itemView.findViewById(R.id.item_timer_task_id_task_name);
            tvTaskDate = itemView.findViewById(R.id.item_timer_task_id_task_date);
            tvTaskTime = itemView.findViewById(R.id.item_timer_task_id_task_time);


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

}
