package com.example.myquarantine.application.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myquarantine.data.Note;
import com.example.myquarantine.db.NoteRepository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {
    private NoteRepository repository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = NoteRepository.getInstance(application);    }

    public void deleteAllNotes(){
        repository.deleteAllNotes();
    }

}
