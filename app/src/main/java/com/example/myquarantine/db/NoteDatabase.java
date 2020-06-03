package com.example.myquarantine.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myquarantine.data.Note;

@Database(entities = {Note.class}, version = 4)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    public abstract NoteDao getNoteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
