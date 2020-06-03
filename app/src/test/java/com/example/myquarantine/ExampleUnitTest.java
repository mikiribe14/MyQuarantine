package com.example.myquarantine;

import com.example.myquarantine.data.Note;
import com.example.myquarantine.application.editnote.EditNoteViewModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public EditNoteViewModel editNoteViewModel;


    @Test
    public void autoFillingTitle() {
        Note note = new Note(34,"","description");
        Note note2 = new Note(34,"","","audiopath");
        assertEquals("My day",  note.getTitle());
        assertEquals("My day",  note2.getTitle());
    }

    @Test
    public void autoFillingDescription() {
        Note note = new Note(34,"title","");
        Note note2 = new Note(34,"","","audiopath");
        assertEquals("Another boring day in this quarantine...",  note.getTitle());
        assertEquals("Another boring day in this quarantine...",  note2.getTitle());
    }


    @Test
    public void deletingNote() {




    }




}