package com.example.myquarantine.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myquarantine.data.Note;
import java.util.List;

@Dao
public interface NoteDao{

    @Insert
    void insert (Note note);

    @Update
    void update (Note note);

    @Query("DELETE FROM note_table WHERE id LIKE :idNumber")
    void deleteNote(int idNumber);

    @Query("SELECT * FROM note_table WHERE id LIKE :idNumber")
    LiveData<Note> getNote(int idNumber);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table WHERE storedInCloud = 1  UNION " +
            "SELECT * FROM note_table WHERE storedInCloud = 0 ORDER BY storedInCloud DESC, daySinceOutbreak DESC ")
    LiveData<List<Note>> getAllNotes();

}
