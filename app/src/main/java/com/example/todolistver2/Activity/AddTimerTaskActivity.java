package com.example.todolistver2.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.TimeZone;

public class AddTimerTaskActivity extends AppCompatActivity implements ColorPickerDialogListener {

    EditText etTaskName;
    TextView tvTaskColor, tvTaskDate;
    Button btnAddTimerTask;
    int timerTaskColor;
    Calendar calendar;
    DbManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_timer_task);
        etTaskName = findViewById(R.id.add_upd_timer_task_name);
        tvTaskColor = findViewById(R.id.add_upd_timer_task_color);
        tvTaskDate =  findViewById(R.id.add_upd_timer_task_date);
        btnAddTimerTask = findViewById(R.id.add_upd_timer_task_btn_add_upd);
        dbManager = new DbManager(AddTimerTaskActivity.this);
        calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));
        timerTaskColor = getColor(R.color.white);

        tvTaskDate.setText(LocalDate.now().format(Constants.format_dd_MM_YYYY));
        btnAddTimerTask.setText("Добавить");

        tvTaskColor.setOnClickListener(view -> createColorPickerDialog(R.id.add_upd_timer_task_color));

        tvTaskDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTimerTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    LocalDate date = LocalDate.of(year, month, datePicker.getDayOfMonth());
                    String strDate = date.format(Constants.format_dd_MM_YYYY);
                    Toast.makeText(AddTimerTaskActivity.this, strDate, Toast.LENGTH_SHORT).show();
                    tvTaskDate.setText(strDate);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnAddTimerTask.setOnClickListener(view -> {
            TimerTask timerTask = new TimerTask();
            timerTask.setName(etTaskName.getText().toString());
            timerTask.setTime(LocalTime.of(0,0,0));
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(tvTaskDate.getText().toString(), Constants.format_dd_MM_YYYY), LocalTime.now());
            timerTask.setDateTime(dateTime);
            timerTask.setColorTask(timerTaskColor);
            dbManager.addTimerTaskDatabase(timerTask);
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_CREATE_TIMER_TASK_KEY, timerTask);
            setResult(Constants.TIMER_TASK_CREATE_ACTION, intent);
            finish();
        });
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId){
            case R.id.add_upd_timer_task_color:
                try {
                    Drawable mDrawable = ContextCompat.getDrawable(AddTimerTaskActivity.this, R.drawable.background_white_text_view);
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
                catch (Exception ex){
                    Toast.makeText(AddTimerTaskActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;
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
}
