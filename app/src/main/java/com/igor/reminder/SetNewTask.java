package com.igor.reminder;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.NO_GRAVITY;

public class SetNewTask extends AppCompatActivity {
    EditText taskMessageText;
    EditText taskNameEdit;
    TextView enterDate;
    TextView enterTime;
    TextView dateTextView;
    TextView timeTextView;
    LinearLayout newTaskPage;

    Calendar c = Calendar.getInstance();

    int yearTask = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    int hour = c.get(Calendar.HOUR);
    int minuteTask  = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_task);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        taskMessageText = (EditText) findViewById(R.id.MessageText);
        newTaskPage = (LinearLayout) findViewById(R.id.newTaskPage);
        taskNameEdit = (EditText) findViewById(R.id.taskNameEdit);
        dateTextView.setText(day + "." + (month + 1) + "." + yearTask);
        dateTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final DatePickerDialog dateDialog = new DatePickerDialog(SetNewTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        yearTask = year;
                        month = monthOfYear;
                        day = dayOfMonth;
                        //enterDate.setText(day + "." + month + "." + yearTask);
                        dateTextView.setText(day + "." + (month + 1) + "." + yearTask);
                    }
                }, yearTask, month, day);
                /*dateDialog.setContentView(R.layout.datepopup);
                dateDialog.setTitle("Choose Date");*/
                dateDialog.show();
                return false;
            }
        });
        timeTextView.setText(hour + ":" + minuteTask);
        timeTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(SetNewTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        minuteTask = minute;
                        //enterTime.setText(hour + ":" + minuteTask);
                        timeTextView.setText(hour + ":" + minuteTask);
                    }
                }, hour, minuteTask, true);
                timePickerDialog.show();
                return false;
            }
        });
    }

    public void OkButton(View view){

        TaskToDo newTask = new TaskToDo(taskNameEdit.getText().toString(), taskMessageText.getText().toString());
        newTask.setDeadLine(yearTask, month, day, hour, minuteTask);

        Intent intent = new Intent();
        intent.putExtra("NEW_TASK", (Serializable) newTask);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelButton(View view){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
