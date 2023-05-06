package com.example.todolistver2.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.TimerTask;
import com.example.todolistver2.R;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class UpdateTimerTaskActivity  extends AppCompatActivity implements ColorPickerDialogListener {
    EditText etTaskName;
    TextView tvTaskColor, tvTaskDate;
    Button btnUpdateTask;
    int timerTaskColor;
    int timerTaskIndex;
    TimerTask selectedTimerTask;
    Calendar calendar;
    DbManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_timer_task);
        etTaskName = findViewById(R.id.add_upd_timer_task_name);
        tvTaskColor = findViewById(R.id.add_upd_timer_task_color);
        tvTaskDate  = findViewById(R.id.add_upd_timer_task_date);
        btnUpdateTask = findViewById(R.id.add_upd_timer_task_btn_add_upd);
        dbManager = new DbManager(UpdateTimerTaskActivity.this);
        calendar =Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));
        btnUpdateTask.setText("Изменить");
        try {
            getIntentAndSetData();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnUpdateTask.setOnClickListener(view -> updateTask());

        tvTaskColor.setOnClickListener(view -> createColorPickerDialog(R.id.add_upd_timer_task_color));

        tvTaskDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UpdateTimerTaskActivity.this, (datePicker, year, month, day) -> {
                        month = month + 1;
                        LocalDate date = LocalDate.of(year, month, datePicker.getDayOfMonth());
                        tvTaskDate.setText(date.format(Constants.format_dd_MM_YYYY));
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

    }

    private void updateTask() {
        selectedTimerTask.setName(etTaskName.getText().toString());
        selectedTimerTask.setDateTime(LocalDateTime.of(LocalDate.parse(tvTaskDate.getText().toString(), Constants.format_dd_MM_YYYY), LocalTime.now()));
        selectedTimerTask.setColorTask(timerTaskColor);
        dbManager.updateTimerTaskDatabase(timerTaskIndex + 1, selectedTimerTask);

        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_INDEX_KEY, timerTaskIndex);
        intent.putExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY, selectedTimerTask);
        setResult(Constants.TIMER_TASK_UPDATE_ACTION, intent);
        finish();
    }

    private void getIntentAndSetData() throws Exception {
        Intent intent = getIntent();
        if (intent != null) {

            timerTaskIndex = intent.getIntExtra(Constants.INTENT_INDEX_KEY, Constants.INTENT_DEFAULT_VALUE);
            if (timerTaskIndex == Constants.INTENT_DEFAULT_VALUE) {
                throw new Exception("Note's index is below zero");
            }
            selectedTimerTask = (TimerTask) intent.getSerializableExtra(Constants.INTENT_UPDATE_TIMER_TASK_KEY);
            etTaskName.setText(selectedTimerTask.getName());
            setBackgroundColorTask(selectedTimerTask.getColorTask());
            tvTaskDate.setText(selectedTimerTask.getDateTime().format(Constants.format_dd_MM_YYYY));
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        if (dialogId == R.id.add_upd_timer_task_color) {
            try {
                setBackgroundColorTask(color);
                timerTaskColor = color;
            } catch (Exception ex) {
                Toast.makeText(UpdateTimerTaskActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.CIRCLE)
                .setDialogId(id)
                .show(this);
    }

    private void setBackgroundColorTask(int color){
        Drawable mDrawable = ContextCompat.getDrawable(UpdateTimerTaskActivity.this, R.drawable.background_white_text_view);
        assert mDrawable != null;
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        tvTaskColor.setBackground(mDrawable);
        timerTaskColor = color;
        if (color == getColor(R.color.white)){
            tvTaskColor.setTextColor(getColor(R.color.black));
        }
        else{
            tvTaskColor.setTextColor(getColor(R.color.white));
        }
    }
}
