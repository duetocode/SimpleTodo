package com.example.simpletodo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.concurrent.Future;

@Dao
public interface TaskDao {

    @Query("SELECT COUNT(ID) FROM task")
    int taskCount();

    @Insert
    void insert(Task... task);

    @Update
    void update(Task task);

    @Query("SELECT * from task ORDER BY id DESC LIMIT 1 OFFSET :index")
    Task get(int index);
}
