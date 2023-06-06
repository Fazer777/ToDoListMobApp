package com.example.todolistver2.fragments.Timer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Activity.AddTimerTaskActivity;
import com.example.todolistver2.Activity.UpdateTimerTaskActivity;
import com.example.todolistver2.Adapters.RecyclerViewTimerAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.TimerTask;
import com.example.todolistver2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TimerFragment extends Fragment {
    Chronometer chronometer;
    boolean isRunning;
    TextView tvTimerTaskName;
    RecyclerView recyclerView;
    RecyclerViewTimerAdapter recyclerViewTimerAdapter;
    Context context;
    List<TimerTask> timerTasks;
    ItemTouchHelper itemTouchHelper;
    CardView timerCardView;
    int selectedPositionTask;
    int selectedTaskColor;
    TimerTask deletedTimerTask;
    Calendar calendar;
    FloatingActionButton btnAddTask;
    DbManager dbManager;
    boolean isDatePick = false;
    String selectedDate;

    TextView tv;
    ImageView imgV;

    private final ActivityResultLauncher<Intent> createTimerTask = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.TIMER_TASK_CREATE_ACTION){
                    Intent intent = result.getData();
                    if (intent != null ) {
                        TimerTask timerTask = (TimerTask) intent.getSerializableExtra(Constants.INTENT_CREATE_TIMER_TASK_KEY);
                        timerTask.setItemIndex(timerTasks.size());
                        timerTasks.add(timerTask);
                        recyclerViewTimerAdapter.notifyItemInserted(timerTasks.size()-1);
                        dbManager.addTimerTaskDatabase(timerTask);
                        setNoData();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateTimerTask = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.TIMER_TASK_UPDATE_ACTION){
                    Intent intent = result.getData();
                    if (intent != null){
                        TimerTask timerTask = (TimerTask) intent.getSerializableExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY);
                        int index = intent.getIntExtra(Constants.INTENT_INDEX_KEY, Constants.INTENT_DEFAULT_VALUE);
                        if (index != Constants.INTENT_DEFAULT_VALUE){
                            timerTasks.set(index, timerTask);
                            recyclerViewTimerAdapter.notifyItemChanged(index);
                        }
                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        selectedPositionTask = -1;
        dbManager = new DbManager(requireActivity());
        timerTasks = new ArrayList<>();
        timerTasks = dbManager.getAllTimerTasksDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chronometer = view.findViewById(R.id.timer_id_chronometer);
        tvTimerTaskName = view.findViewById(R.id.timer_id_task_name);
        recyclerView = view.findViewById(R.id.timer_id_recycler_view);
        timerCardView = view.findViewById(R.id.timer_id_card_view);
        btnAddTask = view.findViewById(R.id.timer_id_btn_add_task);
        tv = view.findViewById(R.id.textView);
        imgV = view.findViewById(R.id.imageView);
        MenuHost menuHost = requireActivity();

        recyclerViewTimerAdapter = new RecyclerViewTimerAdapter(context, timerTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( recyclerViewTimerAdapter);
        setNoData();
        tvTimerTaskName.setText(getString(R.string.choose_task_for_timer));
        calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));

        recyclerViewTimerAdapter.setOnItemClickListener(new RecyclerViewTimerAdapter.IOnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                startStopChronometer(position);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                if (position != selectedPositionTask){
                    Intent intent = new Intent(context, UpdateTimerTaskActivity.class);
                    intent.putExtra(Constants.INTENT_INDEX_KEY, position);
                    intent.putExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY, timerTasks.get(position));
                    updateTimerTask.launch(intent);
                }
            }
        });
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.timer_task_toolbar_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pickDate:
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                context, (datePicker, year, month, day) -> {
                                    isDatePick = true;
                                    month = month + 1;
                                    LocalDate date = LocalDate.of(year, month, datePicker.getDayOfMonth());
                                    selectedDate = date.format(Constants.format_dd_MM_YYYY);
                                    Toast.makeText(context, selectedDate, Toast.LENGTH_SHORT).show();
                                    timerTasks = dbManager.getFilteredTimerTasksByDate(selectedDate);
                                    recyclerViewTimerAdapter.setTimerTasks(timerTasks);
                                    setNoData();
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.showAll:
                        isDatePick = false;
                        timerTasks = dbManager.getAllTimerTasksDatabase();
                        recyclerViewTimerAdapter.setTimerTasks(timerTasks);
                        setNoData();
                        break;
                }
                return true;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        btnAddTask.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AddTimerTaskActivity.class);
            try {
                createTimerTask.launch(intent);
            }
            catch (Exception ex){
                Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }


        });

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getLayoutPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:{
                    if (position == selectedPositionTask){
                        recyclerViewTimerAdapter.notifyItemChanged(position);
                        itemTouchHelper.startSwipe(viewHolder);
                        Toast.makeText(context, getText(R.string.stop_timer), Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedTimerTask = timerTasks.get(position);
                    timerTasks.remove(position);
                    recyclerViewTimerAdapter.notifyItemRemoved(position);
                    dbManager.deleteTimerTaskDatabase(deletedTimerTask.getItemIndex());
                    Toast.makeText(context, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    if(isDatePick){
                        timerTasks = dbManager.getFilteredTimerTasksByDate(selectedDate);
                    }
                    else{
                        timerTasks = dbManager.getAllTimerTasksDatabase();
                    }
                    recyclerViewTimerAdapter.setTimerTasks(timerTasks);
                    setNoData();
                }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.red, context.getTheme()))
                            .addSwipeLeftActionIcon(R.drawable.ic_delete)
                                    .create().decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void startStopChronometer(int positionTask){
        if (!isRunning){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isRunning = true;
            tvTimerTaskName.setText(timerTasks.get(positionTask).getName());
            selectedPositionTask = positionTask;
            selectedTaskColor = timerTasks.get(selectedPositionTask).getColorTask() ;
            timerCardView.setCardBackgroundColor(timerTasks.get(selectedPositionTask).getColorTask());
            timerTasks.get(selectedPositionTask).setColorTask(getResources().getColor(R.color.color_icon_category_default, context.getTheme()));
            recyclerViewTimerAdapter.notifyItemChanged(selectedPositionTask);
            Toast.makeText(context, "Таймер запущен", Toast.LENGTH_SHORT).show();
        }
        else {
            if (positionTask == selectedPositionTask){
                chronometer.stop();
                long amountMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.setBase(SystemClock.elapsedRealtime());
                isRunning = false;

                timerTasks.get(selectedPositionTask).addToTimeTask(amountMillis);
                timerTasks.get(selectedPositionTask).setColorTask(selectedTaskColor);

                recyclerViewTimerAdapter.notifyItemChanged(selectedPositionTask);

                timerCardView.setCardBackgroundColor(getResources().getColor(R.color.Mandy ,context.getTheme()));
                dbManager.updateTimeTimerTaskDatabase(positionTask,  timerTasks.get(selectedPositionTask).getTime());

                tvTimerTaskName.setText(getString(R.string.choose_task_for_timer));
                selectedPositionTask = -1;
                Toast.makeText(context, "Таймер оставновлен", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setNoData(){
        if(timerTasks.size()==0){
            tv.setVisibility(View.VISIBLE);
            imgV.setVisibility(View.VISIBLE);
        }
        else{
            tv.setVisibility(View.GONE);
            imgV.setVisibility(View.GONE);
        }
    }
}