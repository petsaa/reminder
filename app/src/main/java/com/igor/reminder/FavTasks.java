package com.igor.reminder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class FavTasks extends AppCompatActivity {

    private List<TaskToDo> favTaskList;
    private LinearLayout favsPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_tasks);

        favsPage = (LinearLayout) findViewById(R.id.favsPage);

        Intent intent = getIntent();
        favTaskList = (List<TaskToDo>) intent.getSerializableExtra("FAVS_LIST");
        LoadTasks();
    }

    private void LoadTasks() {
        for (TaskToDo element : favTaskList) {
            showTask(element);
        }
    }

    private void showTask(final TaskToDo task){
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(task.getTaskId() % 2 == 0) {
            layout.setBackgroundColor(Color.parseColor("#f8ffdc"));
        }else{
            layout.setBackgroundColor(Color.parseColor("#f1ebe3"));
        }
        layout.setWeightSum(4);
        layout.setLayoutParams(linLayoutParams);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.weight = 3;
        params1.gravity = Gravity.LEFT;
        TextView messageTask = new TextView(this);
        messageTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        messageTask.setText(task.getTaskName());
        messageTask.setLayoutParams(params1);
        layout.addView(messageTask);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        params2.gravity = Gravity.RIGHT;
        TextView deadLine = new TextView(this);
        deadLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        deadLine.setText(task.getDate());
        deadLine.setLayoutParams(params2);
        layout.addView(deadLine);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ADD_FROM_FAVS", (Serializable) task);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        favsPage.addView(layout);
    }
}
