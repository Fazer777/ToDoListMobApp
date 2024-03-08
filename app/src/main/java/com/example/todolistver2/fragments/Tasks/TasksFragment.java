package com.example.todolistver2.fragments.Tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Activity.CategoryActivity;
import com.example.todolistver2.Adapters.RecyclerViewTaskAdapter;
import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Task;
import com.example.todolistver2.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class TasksFragment extends Fragment{

    Context context;
    RecyclerView recyclerView;
    RecyclerViewTaskAdapter recyclerViewTaskAdapter;
    FloatingActionButton btnAddTask;
    List<Task> taskList;
//    ConstraintLayout layoutBottomSheet;
//    BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    Calendar calendar;
    TextView tv;
    ImageView imgV;
    boolean isDatePick =false;
    String selectedDate;
    // Bottom sheet layout element
    Button sheetBtnSave;
    ImageButton sheetBtnClose;
    TextView sheetTvDate, sheetTvColor;
    EditText sheetEtName, sheetEtDescription;
    CheckBox sheetCbComplete;
    int selectedColor;
    DbManager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        dbManager = new DbManager(context);
        taskList = new ArrayList<>();
        taskList = dbManager.getAllTasksDatabase();

       // Toast.makeText(getActivity(), "TasksOnCreate", Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        MenuHost menuHost = requireActivity();
        recyclerViewTaskAdapter = new RecyclerViewTaskAdapter(context, taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewTaskAdapter);
        setNoData();
        calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));

        btnAddTask.setOnClickListener(view1 -> {
            BottomSheetDialog  dialog = new BottomSheetDialog(context);
            selectedColor = getResources().getColor(R.color.white, context.getTheme());
            dialog.setContentView(R.layout.bottom_sheet_tasks);
            initBottomSheetElements(dialog);
            sheetBtnClose.setOnClickListener(view2 -> dialog.dismiss());

            sheetBtnSave.setOnClickListener(view2 -> {
                if (!sheetEtName.getText().toString().matches("") && !sheetTvDate.getText().toString().matches("")){
                    Task task = setTaskData(new Task());
                    task.setItemIndex(taskList.size());
                    taskList.add(task);
                    recyclerViewTaskAdapter.notifyItemInserted(taskList.size() - 1);
                    dbManager.addTaskDatabase(task);
                    setNoData();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(context, getResources().getString(R.string.required_name_date), Toast.LENGTH_SHORT).show();
                }
            });

            sheetTvColor.setOnClickListener(view2 -> setTaskColor());

            sheetTvDate.setOnClickListener(view2 -> setTaskDate());
            dialog.show();
        });

        recyclerViewTaskAdapter.setOnItemClickListener(new RecyclerViewTaskAdapter.IOnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                BottomSheetDialog  dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.bottom_sheet_tasks);
                initBottomSheetElements(dialog);
                getTaskData(position);

                sheetBtnClose.setOnClickListener(view1 -> dialog.dismiss());

                sheetBtnSave.setOnClickListener(view1 -> {
                    if (!sheetEtName.getText().toString().matches("") && !sheetTvDate.getText().toString().matches("")){
                        Task task = setTaskData(taskList.get(position));
                        taskList.set(position, task);
                        recyclerViewTaskAdapter.notifyItemChanged(position);
                        dbManager.updateTaskDataBase(task.getItemIndex(), task);
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(context, getResources().getString(R.string.required_name_date), Toast.LENGTH_SHORT).show();
                    }
                });

                sheetTvColor.setOnClickListener(view1 -> setTaskColor());

                sheetTvDate.setOnClickListener(view1 -> setTaskDate());
                dialog.show();
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                Task task = taskList.get(position);
                CardView view1 = itemView.findViewById(R.id.item_task_id_card_view);
                view1.setCardBackgroundColor(getResources().getColor(R.color.teal_700, context.getTheme()));
                new AlertDialog.Builder(requireActivity())
                        .setTitle(getResources().getString(R.string.delete_selected_task))
                        .setNegativeButton("Да", (dialogInterface, i) -> {
                            taskList.remove(position);
                            recyclerViewTaskAdapter.notifyItemRemoved(position);
                            dbManager.deleteTaskDatabase(task.getItemIndex());
                            Toast.makeText(context, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                            view1.setCardBackgroundColor(Color.WHITE);
                            if(isDatePick){
                                taskList = dbManager.getFilteredTasksByDate(selectedDate);
                            }
                            else{
                                taskList = dbManager.getAllTasksDatabase();
                            }
                            recyclerViewTaskAdapter.setTaskList(taskList);
                            setNoData();
                        })
                        .setPositiveButton("Нет", (dialogInterface, i) -> {
                            Toast.makeText(context, getResources().getString(R.string.cancel_removing), Toast.LENGTH_SHORT).show();
                            view1.setCardBackgroundColor(Color.WHITE);
                            dialogInterface.dismiss();
                        }).setCancelable(false).create().show();
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
                            taskList = dbManager.getFilteredTasksByDate(selectedDate);
                            recyclerViewTaskAdapter.setTaskList(taskList);
                            setNoData();
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.showAll:
                        isDatePick = false;
                        taskList = dbManager.getAllTasksDatabase();
                        recyclerViewTaskAdapter.setTaskList(taskList);
                        setNoData();
                        break;
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void initBottomSheetElements(BottomSheetDialog dialog) {
        sheetEtName = dialog.findViewById(R.id.bottom_sheet_editText);
        sheetEtDescription = dialog.findViewById(R.id.bottom_sheet_et_description);
        sheetTvColor = dialog.findViewById(R.id.bottom_sheet_textViewColor);
        sheetTvDate = dialog.findViewById(R.id.bottom_sheet_textViewDate);
        sheetBtnSave = dialog.findViewById(R.id.bottom_sheet_btn_save);
        sheetBtnClose = dialog.findViewById(R.id.bottom_sheet_btn_close);
        sheetCbComplete = dialog.findViewById(R.id.bottom_sheet_checkbox);
    }

    private void getTaskData(int position){
        sheetEtName.setText(taskList.get(position).getName());
        sheetEtDescription.setText(taskList.get(position).getDescription());
        sheetTvDate.setText(taskList.get(position).getDate().format(Constants.format_dd_MM_YYYY));
        setBackgroundColorTextView(taskList.get(position).getColor());
        selectedColor = taskList.get(position).getColor();
        sheetCbComplete.setChecked(taskList.get(position).getCompleted());
    }

    private void setTaskDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, (datePicker, year, month, day) -> {
                    month = month + 1;
                    LocalDate date = LocalDate.of(year, month, datePicker.getDayOfMonth());
                    String strDate = date.format(Constants.format_dd_MM_YYYY);
                    Toast.makeText(context, strDate, Toast.LENGTH_SHORT).show();
                    sheetTvDate.setText(strDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setTaskColor() {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().create();
        colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                selectedColor = color;
                try {
                    setBackgroundColorTextView(color);
                }
                catch (Exception ex){
                    Toast.makeText(context, "Task_drawable: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
        colorPickerDialog.show(requireActivity().getSupportFragmentManager(), null);
    }

    private void init(View view) {
        // Tasks layout elements
        recyclerView = view.findViewById(R.id.tasks_id_recycler_view);
        btnAddTask = view.findViewById(R.id.tasks_id_btn_add);
        tv = view.findViewById(R.id.textView);
        imgV = view.findViewById(R.id.imageView);
        // BottomSheet elements
//        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
//        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

    }

    private Task setTaskData(Task task){
        //Task task = taskList.get(position);
        task.setName(sheetEtName.getText().toString());
        task.setDescription(sheetEtDescription.getText().toString());
        task.setColor(selectedColor);
        task.setDate(LocalDate.parse(sheetTvDate.getText(), Constants.format_dd_MM_YYYY));
        task.setCompleted(sheetCbComplete.isChecked());
        if (task.getCompleted()){
            task.setDateComplete(LocalDate.now());
        }
        else{
            task.setDateComplete(null);
        }
        return task;
    }

    private void setNoData(){
        if (taskList.size() == 0){
            tv.setVisibility(View.VISIBLE);
            imgV.setVisibility(View.VISIBLE);
        }
        else{
            tv.setVisibility(View.GONE);
            imgV.setVisibility(View.GONE);
        }
    }

    private void setBackgroundColorTextView(int color){
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.background_white_text_view);
        assert mDrawable != null;
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        sheetTvColor.setBackground(mDrawable);
    }
}