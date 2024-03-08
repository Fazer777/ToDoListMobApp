package com.example.todolistver2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Models.Task;
import com.example.todolistver2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTaskAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(taskList.get(position).getName());
        holder.cardView.setCardBackgroundColor(taskList.get(position).getColor());
        if (taskList.get(position).getCompleted()){
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            LocalDate date = taskList.get(position).getDateComplete();
            holder.imgV.setImageDrawable(getDrawableCheck());
            holder.tvDate.setText("Завершено " +
                    date.getDayOfMonth() + " " +
                    date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("Ru")) + " " +
                    date.getYear()+ " г.") ;
            holder.tvDate.setTextColor(Color.BLUE);
        }
        else{
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvDate.setText(taskList.get(position).getDate().format(Constants.format_dd_MM_YYYY));
            holder.tvDate.setTextColor(Color.BLACK);
            holder.imgV.setImageDrawable(getDrawableCalendar());
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private Drawable getDrawableCalendar(){
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_calendar_task);
        mDrawable.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    private Drawable getDrawableCheck() {
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add_note_toolbar_check);
        mDrawable.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }



//    private String getMonthName(LocalDate date){
//
//        Calendar calendar = Calendar.getInstance();
//        try {
//            Date date1 = new SimpleDateFormat("dd.MM.yyyy", new Locale("Ru"))
//                    .parse(date.format(Constants.format_dd_MM_YYYY));
//            assert date1 != null;
//            calendar.setTime(date1);
//        } catch (ParseException e) {
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            return date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("Ru"));
//        }
//        return calendar.getDisplayName(Calendar.MONTH,
//                Calendar.SHORT_FORMAT, new Locale("ru"));
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate;
        CardView cardView;
        ImageView imgV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_task_id_cv_color);
            tvName = itemView.findViewById(R.id.item_task_id_task_name);
            tvDate = itemView.findViewById(R.id.item_task_id_task_date);
            imgV = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(view -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position !=RecyclerView.NO_POSITION){
                        listener.onItemClick(itemView, position);
                    }
                }
            });

            itemView.setOnLongClickListener(view -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position !=RecyclerView.NO_POSITION){
                        listener.onItemLongClick(itemView, position);
                    }
                }
                return true;
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

    public void setTaskList(List<Task> list){
        taskList = list;
        notifyDataSetChanged();
    }
}
