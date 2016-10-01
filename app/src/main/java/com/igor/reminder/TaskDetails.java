package com.igor.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;

public class TaskDetails extends AppCompatActivity {
    EditText taskNameEdit;
    TextView taskDate;
    TextView taskTime;
    EditText messageText;
    private TaskToDo task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details2);

        Intent intent = getIntent();
        task = (TaskToDo) intent.getSerializableExtra("DETAILS_TASK");

        taskNameEdit = (EditText) findViewById(R.id.taskNameEdit);
        taskNameEdit.setText(task.getTaskName());

        taskDate = (TextView) findViewById(R.id.dateView);
        taskDate.setText(task.getDay() + "." + task.getMonth() + "." + task.getYear());
        taskDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        task.setYear(year);
                        task.setMonth(monthOfYear);
                        task.setDay(dayOfMonth);
                    }
                }, task.getYear(), task.getMonth(), task.getDay());
                datePickerDialog.show();
                return false;
            }
        });

        taskTime = (TextView) findViewById(R.id.timeView);
        taskTime.setText(task.getHour() + ":" + task.getMinute());
        taskTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        task.setHour(hourOfDay);
                        task.setMinute(minute);
                    }
                }, task.getHour(), task.getMinute(), true);
                timePickerDialog.show();
                return false;
            }
        });

        messageText = (EditText) findViewById(R.id.MessageText);
        messageText.setText(task.getTaskMessage());
    }

    private void setChanges(){
        task.setTaskName(taskNameEdit.getText().toString());
        task.setTaskMessage(messageText.getText().toString());
    }

    public void setTaskDone(View view){
        task.setIsDone(true);
    }

    public void addToFavourites(View view) {
        setChanges();
        Intent intent = new Intent();
        intent.putExtra("DETAILS_RESULT", 2);
        intent.putExtra("ADD_TO_FAVOURITES", (Serializable) task);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void removeTask(View view) {
        Intent intent = new Intent();
        intent.putExtra("DETAILS_RESULT", 1);
        intent.putExtra("REMOVE_ID", task.getTaskId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setChanges();
        Intent intent = new Intent();
        intent.putExtra("DETAIL_RESULT", 3);
        intent.putExtra("CHANGE_TASK", (Serializable) task);
        setResult(RESULT_OK, intent);
        finish();
    }
}
