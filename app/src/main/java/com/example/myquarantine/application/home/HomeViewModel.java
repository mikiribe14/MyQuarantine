package com.example.myquarantine.application.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myquarantine.data.Note;
import com.example.myquarantine.db.NoteRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private NoteRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = NoteRepository.getInstance(application);
    }

    public LiveData<List<Note>> getAllNotes(){ return repository.getAllNotes();  }

    public LiveData<Note> getNote(int id){
        return repository.getNote(id);
    }

    public void updateNote(Note note){
        repository.update(note);
    }

    public void deleteNote(int id){
        repository.deleteNote(id);
    }

    public void saveNote(int daySinceOutbreak, String title, String description, String audioPath){
        repository.insert(new Note(daySinceOutbreak,title,description,audioPath));
    }

}
