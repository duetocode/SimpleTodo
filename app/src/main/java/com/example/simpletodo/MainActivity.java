package com.example.simpletodo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    AppDatabase database;
    private TodoListAdapter todoListAdapter;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the database
        executorService.submit(() -> {
            database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task").allowMainThreadQueries().build();
            runOnUiThread(() -> {
                // setup the recycler view
                RecyclerView recyclerView = findViewById(R.id.todo_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                // count the number of tasks
                final int taskCount = database.taskDAO().taskCount();
                todoListAdapter = new TodoListAdapter(database.taskDAO(), executorService, taskCount);
                recyclerView.setAdapter(todoListAdapter);

                // the fab
                findViewById(R.id.new_task).setOnClickListener(this::newTask);
            });
        });

    }

    private void newTask(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            final Task task = new Task();
            task.name = input.getText().toString();
            task.checked = false;
            executorService.execute(() -> {
                final TaskDao taskDao = database.taskDAO();
                taskDao.insert(task);
                todoListAdapter.itemCount = taskDao.taskCount();
                runOnUiThread(() -> todoListAdapter.notifyItemInserted(0));
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}