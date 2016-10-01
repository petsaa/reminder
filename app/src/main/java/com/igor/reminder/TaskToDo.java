package com.igor.reminder;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by igor on 06.08.16.
 */
public class TaskToDo implements Serializable{
    private String taskName;
    private String taskMessage;
    private boolean isDone;
    private int taskId;

    Calendar c = Calendar.getInstance();

    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH);
    private int day = c.get(Calendar.DAY_OF_MONTH);

    private int hour = c.get(Calendar.HOUR);
    private int minute  = c.get(Calendar.MINUTE);

    public TaskToDo(String taskName, String taskMessage){
        this.taskName = taskName;
        this.taskMessage = taskMessage;
        this.isDone = false;
        this.taskId = 0;
    }

    public String getTaskMessage() {
        return taskMessage;
    }

    public void setTaskMessage(String taskMessage) {
        this.taskMessage = taskMessage;
    }

    public String getDate() {
        return day + "." + (month + 1) + "." + year;
    }

    public void setDeadLine(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
