package com.example.myquarantine.db;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.myquarantine.data.Note;
import java.util.List;

public class NoteRepository{
    private NoteDao noteDao;
    private static NoteRepository instance;
    private LiveData<List<Note>> allNotes;

    private NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.getNoteDao();
        allNotes = noteDao.getAllNotes();
    }

    public static synchronized NoteRepository getInstance(Application application){
        if (instance==null){
            instance = new NoteRepository(application);
        }
        return instance;
    }

    public LiveData<Note> getNote(int id) {
        return noteDao.getNote(id);
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }


    public void update(Note note){
        new UpdateNoteAsync(noteDao).execute(note);
    }
    private class UpdateNoteAsync extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;

        public UpdateNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }


    public void insert(Note note){
        new InsertNoteAsync(noteDao).execute(note);
    }
    private class InsertNoteAsync extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;

        public InsertNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public void deleteNote(int id){
        new DeleteNoteAsync(noteDao).execute(id);
    }
    private static class DeleteNoteAsync extends AsyncTask<Integer,Void,Void> {
        private NoteDao noteDao;
        private DeleteNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Integer... id) {
            noteDao.deleteNote(id[0]);
            return null;
        }
    }


    public void deleteAllNotes(){
        new DeleteAllNotesAsync(noteDao).execute();
    }
    private static class DeleteAllNotesAsync extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;

        private DeleteAllNotesAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
