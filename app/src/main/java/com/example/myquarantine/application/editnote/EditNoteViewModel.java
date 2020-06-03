package com.example.myquarantine.application.editnote;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myquarantine.data.Note;
import com.example.myquarantine.db.NoteRepository;
import java.util.Calendar;


import static android.content.Context.MODE_PRIVATE;

public class EditNoteViewModel extends AndroidViewModel {
    private NoteRepository repository;

    public EditNoteViewModel(@NonNull Application application) {
        super(application);
        repository = NoteRepository.getInstance(application);
    }

    public LiveData<Note> getNote(int id) {
        return repository.getNote(id);
    }

    public void saveNote(int daySinceOutbreak, String title, String description, String audioPath){
        repository.insert(new Note(daySinceOutbreak,title,description,audioPath));
    }

    public void updateNote(Note note, int day, String title, String description, String audioPath ){
        note.setDaySinceOutbreak(day);
        note.setTitle(title);
        note.setDescription(description);
        note.setAudioPath(audioPath);
        repository.update(note);
    }


    public int getDaysSinceOutbreak() {
        SharedPreferences prefs = getApplication().getSharedPreferences("MyPreferences", MODE_PRIVATE);

        if (!prefs.contains("yearOutbreak")) {
            return -1;
        }
        ;

        String year = prefs.getString("yearOutbreak", "0");
        String month = prefs.getString("monthOutbreak", "0");
        String day = prefs.getString("dayOutbreak", "0");

        Calendar calendar = Calendar.getInstance();
        int diffYears = calendar.get(Calendar.YEAR) - Integer.parseInt(year);
        int diffMonth = calendar.get(Calendar.MONTH) + 1 - Integer.parseInt(month);
        int diffDays = calendar.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(day);

        return diffDays + diffMonth * 30 + diffYears * 365;
    }

}
