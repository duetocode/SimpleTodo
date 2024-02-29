package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListItemHolder> {
    int itemCount = 0;
    private final TaskDao taskDao;

    private final ExecutorService executorService;

    public TodoListAdapter(TaskDao taskDao, ExecutorService executorService, int taskCount) {
        this.taskDao = taskDao;
        this.executorService = executorService;
        this.itemCount = taskCount;
    }

    @NonNull
    @Override
    public TodoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        TodoListItemHolder holder = new TodoListItemHolder(view);
        holder.checkbox.setOnCheckedChangeListener((btn, value) -> this.onChange(holder, value));
        return holder;
    }

    private void onChange(TodoListItemHolder holder, boolean checked) {
        final int index = holder.index;
        executorService.execute(() -> {
            final Task task = taskDao.get(index);
            // check if the holder is still representing the same data
            if (index != holder.index) return;
            task.checked = checked;
            taskDao.update(task);
            holder.itemView.post(() -> notifyItemChanged(index));
        });
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListItemHolder holder, int position) {
        // save the index for async call
        final int index = position;
        holder.index = index;

        executorService.execute(() -> {
            final Task task = taskDao.get(index);
            // give up if the holder's target index has changed
            if (index != holder.index) return;
            holder.itemView.post(() -> holder.setTask(task));
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
