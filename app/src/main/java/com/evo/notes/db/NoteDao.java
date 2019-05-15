package com.evo.notes.db;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note LIMIT :offset, :limit")
    List<Note> getAll(int offset, int limit);

    @Query("SELECT * FROM note ORDER BY uid DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note WHERE uid = :id")
    Note findById(int id);

    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Query("DELETE FROM note WHERE uid = :id")
    void remove(int id);

    @Query("SELECT * FROM note ORDER BY uid DESC")
    DataSource.Factory<Integer, Note> getAllNotesPaged();
}




