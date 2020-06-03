package com.example.myquarantine.application.viewnote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myquarantine.data.Note;
import com.example.myquarantine.db.NoteRepository;

public class ViewNoteViewModel extends AndroidViewModel {
    private NoteRepository repository;

    public ViewNoteViewModel(@NonNull Application application) {
        super(application);
        repository = NoteRepository.getInstance(application);

    }

    public LiveData<Note> getNote(int id) {
        return repository.getNote(id);
    }

}