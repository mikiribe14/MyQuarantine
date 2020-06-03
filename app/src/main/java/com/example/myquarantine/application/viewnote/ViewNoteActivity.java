package com.example.myquarantine.application.viewnote;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.myquarantine.R;
import com.example.myquarantine.data.Note;
import com.example.myquarantine.application.editnote.EditNoteActivity;

import java.io.IOException;

public class ViewNoteActivity extends AppCompatActivity {
    private ViewNoteViewModel viewNoteViewModel;

    private ConstraintLayout layout;
    private TextView dayNumber;
    private TextView title;
    private TextView description;
    private TextView audioText;

    private Note note;

    private MediaPlayer mediaPlayer;
    private Button playRecB;
    private Button stopPlayB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);
        viewNoteViewModel = new ViewModelProvider(this).get(ViewNoteViewModel.class);

        layout = findViewById(R.id.viewNoteLayout);
        dayNumber = findViewById(R.id.dayNumber);
        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descriptionText);
        audioText = findViewById(R.id.audioText);

        playRecB = findViewById(R.id.playButtonV);
        stopPlayB = findViewById(R.id.stopButtonV);
        playRecB.setVisibility(View.VISIBLE);
        stopPlayB.setVisibility(View.INVISIBLE);

        int id = getIntent().getIntExtra("id",1);
        viewNoteViewModel.getNote(id).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note notE) {
                note = notE;
                displayNote(notE);
            }
        });

    }

    public void displayNote(Note note){
        if(note.getStoredInCloud()){
            int color = ContextCompat.getColor(getApplicationContext(), R.color.note_important);
            layout.setBackgroundColor(color);
        }
        dayNumber.setText(String.valueOf(note.getDaySinceOutbreak()));
        title.setText(note.getTitle());
        description.setText(note.getDescription());

        if(!note.hasAudio()){
            audioText.setText("Add your voice note");
            playRecB.setVisibility(View.INVISIBLE);
            audioText.setOnClickListener(new View.OnClickListener() {
                @Override
                 public void onClick(View v) {
                    toEditNote(v);
                 }
            });
        }

    }

    public void toEditNote(View v){
        finish();
        Intent intent = new Intent (this, EditNoteActivity.class);
        intent.putExtra("id",note.getId());
        startActivity(intent);
    }


    public void playRecording (View v){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(note.getAudioPath());
            mediaPlayer.prepare();
            audioText.setText("Playing...");
        } catch(IOException e){
            e.printStackTrace();
            Log.e("error","Exception in mediaPlayer");
        }
        mediaPlayer.start();
        playRecB.setVisibility(View.INVISIBLE);
        stopPlayB.setVisibility(View.VISIBLE);
    }

    public void stopPlaying(View v){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            audioText.setText("Your audio");
            playRecB.setVisibility(View.VISIBLE);
            stopPlayB.setVisibility(View.INVISIBLE);
        }
    }

}
