package com.example.simpletodo;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoListItemHolder extends RecyclerView.ViewHolder {
    int index = -1;
    CheckBox checkbox;
    TextView textView;

    public TodoListItemHolder(@NonNull View itemView) {
        super(itemView);
        checkbox = itemView.findViewById(R.id.checkbox);
        textView = itemView.findViewById(R.id.task_name);
    }

    public void setTask(Task task) {
        textView.setText(task.name);
        checkbox.setChecked(task.checked);
    }

}
