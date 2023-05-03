package com.example.todolistver2.fragments.Timer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Activity.AddNoteActivity;
import com.example.todolistver2.Activity.AddTimerTaskActivity;
import com.example.todolistver2.Activity.UpdateTimerTaskActivity;
import com.example.todolistver2.Adapters.RecyclerViewTimerAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.Models.Task;
import com.example.todolistver2.R;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    List<Task> tasks;
    ItemTouchHelper itemTouchHelper;
    CardView timerCardView;
    int selectedPositionTask;
    int selectedTaskColor;
    Task deletedTask;
    Calendar calendar;
    Button btnAddTask;
    DbManager dbManager;

    private final ActivityResultLauncher<Intent> createTimerTask = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.TIMER_TASK_CREATE_ACTION){
                    Toast.makeText(getActivity(), "Create raise", Toast.LENGTH_SHORT).show();
                    Intent intent = result.getData();
                    if (intent != null ) {
                        Task task = (Task) intent.getSerializableExtra(Constants.INTENT_CREATE_TIMER_TASK_KEY);
                        tasks.add(Constants.INSERT_POSITION, task);
                        recyclerViewTimerAdapter.notifyItemInserted(Constants.INSERT_POSITION);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateTimerTask = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Constants.TIMER_TASK_UPDATE_ACTION){
                    Toast.makeText(getActivity(), "Update raise", Toast.LENGTH_SHORT).show();
                    Intent intent = result.getData();
                    if (intent != null){
                        Task task = (Task) intent.getSerializableExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY);
                        int index = intent.getIntExtra(Constants.INTENT_INDEX_KEY, Constants.INTENT_DEFAULT_VALUE);
                        if (index != Constants.INTENT_DEFAULT_VALUE){
                            tasks.set(index, task);
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
        tasks = new ArrayList<>();
        tasks = dbManager.getAllTimerTasksDatabase();
        Toast.makeText(context, "TimerFragmentOnCreate", Toast.LENGTH_SHORT).show();
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
        MenuHost menuHost = requireActivity();

        recyclerViewTimerAdapter = new RecyclerViewTimerAdapter(context, tasks );
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( recyclerViewTimerAdapter);
        tvTimerTaskName.setText("Выберите задачу для запуска таймера");
        calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));

        recyclerViewTimerAdapter.setOnItemClickListener(new RecyclerViewTimerAdapter.IOnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                startStopChronometer(itemView, isRunning, position);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                if (position != selectedPositionTask){
                    Intent intent = new Intent(context, UpdateTimerTaskActivity.class);
                    intent.putExtra(Constants.INTENT_INDEX_KEY, position);
                    intent.putExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY, tasks.get(position));
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
                        Toast.makeText(context, "pickDate", Toast.LENGTH_SHORT).show();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                LocalDate date = LocalDate.of(year, month, datePicker.getDayOfMonth());
                                String strDate = date.format(Constants.format_dd_MM_YYYY);
                                Toast.makeText(context, strDate, Toast.LENGTH_SHORT).show();
                                recyclerViewTimerAdapter.filterByDate(strDate);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.showAll:
                        Toast.makeText(context, "ShowAll", Toast.LENGTH_SHORT).show();
                        recyclerViewTimerAdapter.filterByDate("");
                        break;
                }
                return true;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddTimerTaskActivity.class);
                try {
                    createTimerTask.launch(intent);
                }
                catch (Exception ex){
                    Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }


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
                        Toast.makeText(context, "Stop timer before deleting this task", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedTask = tasks.get(position);
                    tasks.remove(position);
                    recyclerViewTimerAdapter.notifyItemRemoved(position);
                    dbManager.deleteTimerTaskDatabase(position + 1);

                    Snackbar.make(recyclerView, deletedTask.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Отменить", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tasks.add(position, deletedTask);
                                    recyclerViewTimerAdapter.notifyItemInserted(position);
                                    dbManager.addTimerTaskDatabase(deletedTask);
                                }
                            }).show();
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

    private void startStopChronometer(View view, boolean running, int positionTask){

        if (!isRunning){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isRunning = true;
            tvTimerTaskName.setText(tasks.get(positionTask).getName());
            selectedPositionTask = positionTask;
            selectedTaskColor = tasks.get(selectedPositionTask).getColorTask() ;
            timerCardView.setCardBackgroundColor(tasks.get(selectedPositionTask).getColorTask());
            tasks.get(selectedPositionTask).setColorTask(getResources().getColor(R.color.color_icon_category_default, context.getTheme()));
            recyclerViewTimerAdapter.notifyItemChanged(selectedPositionTask);


        }
        else {
            if (positionTask == selectedPositionTask){
                chronometer.stop();
                long amountMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.setBase(SystemClock.elapsedRealtime());
                isRunning = false;

                tasks.get(selectedPositionTask).addToTimeTask(amountMillis);
                tasks.get(selectedPositionTask).setColorTask(selectedTaskColor);

                recyclerViewTimerAdapter.notifyItemChanged(selectedPositionTask);

                timerCardView.setCardBackgroundColor(getResources().getColor(R.color.white ,context.getTheme()));
                dbManager.updateTimeTimerTaskDatabase(positionTask+1,  tasks.get(selectedPositionTask).getTime());

                tvTimerTaskName.setText("Выберите задачу для запуска таймера");
                selectedPositionTask = -1;
            }
        }
    }
}