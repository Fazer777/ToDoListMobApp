package com.example.todolistver2.fragments.Tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    ConstraintLayout layoutBottomSheet;
    BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    Calendar calendar;
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


        recyclerViewTaskAdapter = new RecyclerViewTaskAdapter(context, taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewTaskAdapter);
        calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));

        btnAddTask.setOnClickListener(view1 -> {
            BottomSheetDialog  dialog = new BottomSheetDialog(context);
            selectedColor = getResources().getColor(R.color.white, context.getTheme());
            dialog.setContentView(R.layout.bottom_sheet);
            initBottomSheetElements(dialog);
            sheetBtnClose.setOnClickListener(view2 -> dialog.dismiss());


            sheetBtnSave.setOnClickListener(view2 -> {
                if (!sheetEtName.getText().toString().matches("") && !sheetTvDate.getText().toString().matches("")){
                    Task task = setTaskData();
                    taskList.add(Constants.INSERT_POSITION, task);
                    recyclerViewTaskAdapter.notifyItemInserted(Constants.INSERT_POSITION);
                    dbManager.addTaskDatabase(task);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(context, "Заполните поля: Название и Дата", Toast.LENGTH_SHORT).show();
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
                dialog.setContentView(R.layout.bottom_sheet);
                initBottomSheetElements(dialog);
                getTaskData(position);

                sheetBtnClose.setOnClickListener(view1 -> dialog.dismiss());

                sheetBtnSave.setOnClickListener(view1 -> {
                    if (!sheetEtName.getText().toString().matches("") && !sheetTvDate.getText().toString().matches("")){
                        Task task = setTaskData();
                        taskList.set(position, task);
                        recyclerViewTaskAdapter.notifyItemChanged(position);
                        dbManager.updateTaskDataBase(position +1, task);
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(context, "Заполните поля: Название и Дата", Toast.LENGTH_SHORT).show();
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
                        .setTitle("Do you want to remove this note ?")
                        .setNegativeButton("Yes", (dialogInterface, i) -> {
                            taskList.remove(position);
                            recyclerViewTaskAdapter.notifyItemRemoved(position);
                            dbManager.deleteTaskDatabase(position + 1);
                            Toast.makeText(context, "Delete!", Toast.LENGTH_SHORT).show();
                        })
                        .setPositiveButton("No", (dialogInterface, i) -> {
                            Toast.makeText(context, "Not delete", Toast.LENGTH_SHORT).show();
                            view1.setCardBackgroundColor(task.getColor());
                            dialogInterface.dismiss();
                        }).setCancelable(false).create().show();

            }
        });
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
        sheetTvColor.setBackgroundColor(taskList.get(position).getColor());
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
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.background_white_text_view);
                    assert mDrawable != null;
                    mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    sheetTvColor.setBackground(mDrawable);
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
        // BottomSheet elements
        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

    }

    private Task setTaskData(){
        Task task = new Task();
        task.setName(sheetEtName.getText().toString());
        task.setDescription(sheetEtDescription.getText().toString());
        task.setColor(selectedColor);
        task.setDate(LocalDate.parse(sheetTvDate.getText(), Constants.format_dd_MM_YYYY));
        task.setCompleted(sheetCbComplete.isChecked());
        return task;
    }
}