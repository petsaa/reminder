package com.igor.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final int SHOW_DETAILS = 1;
    final int MARK_DONE = 2;
    final int REMOVE = 3;

    final int ADD_NEW = 4;
    final int FROM_FAVS = 5;

    private final static int currentTaskList = 1;
    private final static int favsTaskList = 2;

    Button enterNewTaskButton;
    LinearLayout mainPage;
    LayoutParams position1;
    LayoutParams position2;
    List<TaskToDo> taskToDoList;
    List<TaskToDo> taskFavourites;

    private TaskToDo curTask;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        position1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        position2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mainPage = (LinearLayout) findViewById(R.id.MainLayout);
        taskFavourites = new ArrayList<TaskToDo>();
        taskToDoList = new ArrayList<TaskToDo>();
        DbGetTask getCurrentTask = new DbGetTask(currentTaskList);
        getCurrentTask.execute();
        DbGetTask getFavsTask = new DbGetTask(favsTaskList);
        getFavsTask.execute();
        enterNewTaskButton = (Button) findViewById(R.id.button);
        enterNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(enterNewTaskButton);
                openContextMenu(enterNewTaskButton);
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void LoadTasks() {
        for (TaskToDo element : taskToDoList) {
            showTask(element);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.igor.reminder/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.igor.reminder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4) {
            if(resultCode == RESULT_OK) {
                if (data == null) {
                    //test.setText("RESULT_ CANCELED");
                    return;
                }
                TaskToDo task = (TaskToDo)data.getSerializableExtra("NEW_TASK");
                task.setTaskId(taskToDoList.size());
                taskToDoList.add(task);
                DbInsertTask insertTask = new DbInsertTask(task, currentTaskList);
                insertTask.execute();
                showTask(task);
            }
        }else if(requestCode == 5){
            if(resultCode == RESULT_OK){
                int detailsResult = data.getIntExtra("DETAILS_RESULT", 0);
                if(detailsResult == 1){
                    //remove
                    int toRemoveTaskId = data.getIntExtra("REMOVE_ID", 0);
                    taskToDoList.remove(toRemoveTaskId);
                    DbDeleteTask deleteTask = new DbDeleteTask(toRemoveTaskId);
                    deleteTask.execute();
                    mainPage.removeAllViews();
                    LoadTasks();
                }else if(detailsResult == 2){
                    //add to favourites
                    TaskToDo favTask = (TaskToDo) data.getSerializableExtra("ADD_TO_FAVOURITES");
                    taskFavourites.add(favTask);
                    DbInsertTask insertTask = new DbInsertTask(favTask, favsTaskList);
                    insertTask.execute();
                }else if(detailsResult == 3){
                    //change task
                    TaskToDo task = (TaskToDo) data.getSerializableExtra("CHANGE_TASK");
                    DbUpdateTask updateTask = new DbUpdateTask(task);
                    updateTask.execute();
                }
            }
        }else if(requestCode == 6){
            if(resultCode == RESULT_OK){
                TaskToDo task = (TaskToDo) data.getSerializableExtra("ADD_FROM_FAVS");
                task.setTaskId(taskToDoList.size());
                taskToDoList.add(task);
                showTask(task);
            }
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
        layout.setClickable(true);

        layout.setTag(task);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDetails.class);
                intent.putExtra("DETAILS_TASK", (Serializable) task);
                startActivityForResult(intent, 5);
            }
        });
        registerForContextMenu(layout);

        mainPage.addView(layout);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == enterNewTaskButton.getId()){
            menu.add(0, ADD_NEW, 0, R.string.addNewTaskMenu);
            menu.add(0, FROM_FAVS, 0, R.string.fromFavsMenu);
        }else {
            curTask = (TaskToDo) v.getTag();
            menu.add(0, SHOW_DETAILS, 0, R.string.showDetsMenu);
            menu.add(0, MARK_DONE, 0, R.string.markDoneMenu);
            menu.add(0, REMOVE, 0, R.string.removeMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case SHOW_DETAILS:
                Intent intentDetails = new Intent(getApplicationContext(), TaskDetails.class);
                intentDetails.putExtra("DETAILS_TASK", (Serializable) curTask);
                startActivityForResult(intentDetails, 5);
                break;
            case MARK_DONE:
                curTask.setIsDone(true);
                break;
            case REMOVE:
                taskToDoList.remove(curTask.getTaskId());
                mainPage.removeAllViews();
                LoadTasks();
                break;
            case ADD_NEW:
                Intent intentNewTask = new Intent(this, SetNewTask.class);
                startActivityForResult(intentNewTask, 4);
                break;
            case FROM_FAVS:
                Intent intent = new Intent(this, FavTasks.class);
                intent.putExtra("FAVS_LIST", (Serializable) taskFavourites);
                startActivityForResult(intent, 6);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /*public void enterNewTask(View view){
        Intent intent = new Intent(this, SetNewTask.class);
        //startActivity(intent);
        startActivityForResult(intent, 4);
    }*/

    class DbInsertTask extends AsyncTask<Void, Void, Void>{

        private DbHelper dbHelper;
        private TaskToDo task;
        private int currentOrFavs;

        public DbInsertTask(TaskToDo task, int currentOrFavs) {
            this.task = task;
            this.currentOrFavs = currentOrFavs;
        }

        @Override
        protected void onPreExecute() {
            dbHelper = new DbHelper(getApplicationContext());
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase sdb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbHelper.NAME_COLUMN, task.getTaskName());
            values.put(DbHelper.TASK_DESCRIPTION_COLUMN, task.getTaskMessage());
            values.put(DbHelper.TASK_ID, String.valueOf(task.getTaskId()));
            values.put(DbHelper.TASK_YEAR, String.valueOf(task.getYear()));
            values.put(DbHelper.TASK_MONTH, String.valueOf(task.getMonth()));
            values.put(DbHelper.TASK_DAY, String.valueOf(task.getDay()));
            values.put(DbHelper.TASK_HOUR, String.valueOf(task.getHour()));
            values.put(DbHelper.TASK_MINUTE, String.valueOf(task.getMinute()));
            if(currentOrFavs == currentTaskList) {
                sdb.insert(DbHelper.TASKLIST_TABLE_NAME, null, values);
            }else{
                sdb.insert(DbHelper.TASK_FAVS_TABLE, null, values);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dbHelper.close();
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "finish inserting", Toast.LENGTH_LONG).show();
        }
    }

    class DbDeleteTask extends AsyncTask<Void, Void, Void>{
        private int toRemoveTask;
        private DbHelper helper;

        public DbDeleteTask(int toRemoveTask) {
            this.toRemoveTask = toRemoveTask;
        }

        @Override
        protected void onPreExecute() {
            helper = new DbHelper(getApplicationContext());
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase sdb = helper.getWritableDatabase();
            sdb.delete(DbHelper.TASKLIST_TABLE_NAME, DbHelper.TASK_ID + "=?", new String[]{String.valueOf(toRemoveTask)});
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            helper.close();
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "finish deleting", Toast.LENGTH_LONG).show();
        }
    }

    class DbUpdateTask extends AsyncTask<Void, Void, Void>{

        private TaskToDo task;
        private DbHelper helper;

        public DbUpdateTask(TaskToDo task) {
            this.task = task;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase sdb = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbHelper.NAME_COLUMN, task.getTaskName());
            values.put(DbHelper.TASK_DESCRIPTION_COLUMN, task.getTaskMessage());
            values.put(DbHelper.TASK_ID, String.valueOf(task.getTaskId()));
            values.put(DbHelper.TASK_YEAR, String.valueOf(task.getYear()));
            values.put(DbHelper.TASK_MONTH, String.valueOf(task.getMonth()));
            values.put(DbHelper.TASK_DAY, String.valueOf(task.getDay()));
            values.put(DbHelper.TASK_HOUR, String.valueOf(task.getHour()));
            values.put(DbHelper.TASK_MINUTE, String.valueOf(task.getMinute()));
            sdb.update(DbHelper.TASKLIST_TABLE_NAME, values, DbHelper.TASK_ID + "=?", new String[]{String.valueOf(task.getTaskId())});
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            helper.close();
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "finish updating", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            helper = new DbHelper(getApplicationContext());
            super.onPreExecute();
        }
    }

    class DbGetTask extends AsyncTask<Void, TaskToDo, Void>{

        private DbHelper helper;
        private int currentOrFavs;

        public DbGetTask(int currentOrFavs) {
            this.currentOrFavs = currentOrFavs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            helper = new DbHelper(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            helper.close();
//            LoadTasks();
            Toast.makeText(getApplicationContext(), "finish loading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(TaskToDo... values) {
            super.onProgressUpdate(values);
            if(currentOrFavs == currentTaskList) {
                taskToDoList.add(values[0]);
                showTask(values[0]);
            }else{
                taskFavourites.add(values[0]);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase sDb = helper.getReadableDatabase();
            Cursor cursor;
            if(currentOrFavs == currentTaskList) {
                cursor = sDb.query(DbHelper.TASKLIST_TABLE_NAME, new String[]{DbHelper.NAME_COLUMN, DbHelper.TASK_DESCRIPTION_COLUMN, DbHelper.TASK_ID,
                        DbHelper.TASK_YEAR, DbHelper.TASK_MONTH, DbHelper.TASK_DAY, DbHelper.TASK_HOUR, DbHelper.TASK_MINUTE}, null, null, null, null, null);
            }else{
                cursor = sDb.query(DbHelper.TASK_FAVS_TABLE, new String[]{DbHelper.NAME_COLUMN, DbHelper.TASK_DESCRIPTION_COLUMN, DbHelper.TASK_ID,
                        DbHelper.TASK_YEAR, DbHelper.TASK_MONTH, DbHelper.TASK_DAY, DbHelper.TASK_HOUR, DbHelper.TASK_MINUTE}, null, null, null, null, null);
            }
            if(cursor.moveToFirst()) {
                do {
                    TaskToDo task = new TaskToDo(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("description")));
                    //task.setTaskId(taskToDoList.size());
                    task.setTaskId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_ID))));
                    task.setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_YEAR))));
                    task.setMonth(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_MONTH))));
                    task.setDay(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_DAY))));
                    task.setHour(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_HOUR))));
                    task.setMinute(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.TASK_MINUTE))));
                    publishProgress(task);
                } while (cursor.moveToNext());
            }
            return null;
        }
    }
}
