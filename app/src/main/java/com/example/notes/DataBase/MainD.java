package com.example.notes.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notes.Models.Note;

import java.util.List;

@Dao
public interface MainD {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note notes);

    @Query("SELECT * From notes Order by id Desc")
    List<Note> getAll();

    @Query("UPDATE notes SET title = :title,notes = :notes WHERE ID = :id")
    void update(int id,String title,String notes);

    @Delete
    void delete(Note notes);
    @Query("Update notes SET pin = :pin Where ID = :id")
    void pin(int id,boolean pin);
}
