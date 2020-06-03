package com.example.myquarantine.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int daySinceOutbreak;
    private String title;
    private String description;
    private String audioPath;
    private boolean storedInCloud;

    public Note (int daySinceOutbreak, String title, String description, String audioPath){
        this.daySinceOutbreak = daySinceOutbreak;
        if (title.equals("")){
            this.title = "My day";
        } else{
            this.title = title;
        }

        if (description.equals("")){
            this.description = "Another boring day in this quarantine...";
        } else{
            this.description = description;
        }

        this.audioPath = audioPath;
        this.storedInCloud = false;
    }

    public Note (int daySinceOutbrea, String titl, String descriptio){
        this(daySinceOutbrea,titl,descriptio,"");
    }

    public Note (){ }

    public boolean hasAudio(){
        return !audioPath.equals("");
    }

    public void setId(int id) {  this.id = id;  }

    public void setDaySinceOutbreak(int daySinceOutbreak) {
        this.daySinceOutbreak = daySinceOutbreak;
    }

    public void setDescription(String description) {
        if (description.equals("")){
            this.description = "Another boring day in this quarantine...";
        } else{
            this.description = description;
        }
    }

    public void setTitle(String title) {
        if (title.equals("")){
            this.title = "My day";
        } else{
            this.title = title;
        }
    }

    public void setAudioPath(String audioPath) {  this.audioPath = audioPath;  }

    public void setStoredInCloud(boolean stored){ this.storedInCloud = stored;  }


    public int getId() {
        return id;
    }

    public int getDaySinceOutbreak() { return daySinceOutbreak; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getAudioPath() { return audioPath;  }

    public boolean getStoredInCloud(){  return storedInCloud;  }

}

