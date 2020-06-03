package com.example.myquarantine.application.editnote;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.myquarantine.R;
import com.example.myquarantine.data.Note;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.UUID;

public class EditNoteActivity extends AppCompatActivity {
    private TextView dayNumber;
    private TextView audioText;
    private EditText title;
    private EditText description;
    private ConstraintLayout layout;
    private Button startRecB;
    private Button stopRecB;
    private Button playRecB;
    private Button stopPlayB;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioPath="";
    private final int REQUEST_PERMISSION_CODE = 1000;

    private EditNoteViewModel editNoteViewModel;

    private int daysSinceOutbreak;
    private int previousId=0;
    private Note note;
    private boolean editingPreexistent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        editNoteViewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);

        layout = findViewById(R.id.editNoteLayout);
        title = findViewById(R.id.titleField);
        description = findViewById(R.id.descriptionField);
        dayNumber = findViewById(R.id.dayNumber);
        audioText = findViewById(R.id.audioText);

        daysSinceOutbreak = editNoteViewModel.getDaysSinceOutbreak();
        if (daysSinceOutbreak != -1) {
            dayNumber.setText(String.valueOf(daysSinceOutbreak));
        }

        if (editingPreexistent = getIntent().hasExtra("id")){    //if editing a preexistent Note
            retrieveNoteData();
        }

        setUpAudioButtons();

    }

    public void setUpAudioButtons(){
        startRecB = findViewById(R.id.startButton);
        stopRecB = findViewById(R.id.stopButton);
        playRecB = findViewById(R.id.playButton);
        stopPlayB = findViewById(R.id.pauseButton);

        startRecB.setVisibility(View.VISIBLE);
        stopRecB.setVisibility(View.INVISIBLE);
        playRecB.setVisibility(View.INVISIBLE);
        stopPlayB.setVisibility(View.INVISIBLE);

    }

    public void retrieveNoteData(){
        previousId = getIntent().getIntExtra("id",0);
        editNoteViewModel.getNote(previousId).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note prevNote) {
                dayNumber.setText(String.valueOf(prevNote.getDaySinceOutbreak()));
                title.setText(prevNote.getTitle());
                description.setText(prevNote.getDescription());
                audioPath = prevNote.getAudioPath();
                note=prevNote;
                if(prevNote.hasAudio()){  playRecB.setVisibility(View.VISIBLE);  }
                if(note.getStoredInCloud()){
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.note_important);
                    layout.setBackgroundColor(color);
                }
            }
        });
    }

    public void saveNote(View view){
        if (editingPreexistent){
            editNoteViewModel.updateNote(note,daysSinceOutbreak,
                                        title.getText().toString(),
                                        description.getText().toString(),
                                        audioPath);

            if (note.getStoredInCloud()){ updateInCloud(note); }

        } else{
            editNoteViewModel.saveNote(daysSinceOutbreak,
                                        title.getText().toString(),
                                        description.getText().toString(),
                                        audioPath);
        }

        finish();
    }

    public void updateInCloud(Note note){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child(String.valueOf(note.getId())).setValue(note);
    }



/********************************** PERMISSIONS ******************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case (REQUEST_PERMISSION_CODE): {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean getPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
    }
    /********************************** END PERMISSIONS ******************************/



    /************************************* MEDIA ************************************/
    public boolean setupMediaRecorder(){
        boolean extStorPerm = getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean recordAudPerm = getPermission(Manifest.permission.RECORD_AUDIO);

        if(!extStorPerm || !recordAudPerm){
            Toast.makeText(getApplicationContext(), "You don't have the permissions", Toast.LENGTH_LONG).show();
            return false;
        }

        audioPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioPath);

        return true;
    }

    public boolean startRecording(View v){
        if (!setupMediaRecorder()){return false;}

        try{
            mediaRecorder.prepare();
            mediaRecorder.start();
            audioText.setText("Recording...");

            startRecB.setVisibility(View.INVISIBLE);
            stopRecB.setVisibility(View.VISIBLE);
            playRecB.setVisibility(View.INVISIBLE);
            stopPlayB.setVisibility(View.INVISIBLE);

        } catch (IOException e){
            e.printStackTrace();

            Log.e("error",e.getMessage());
        }
        return true;
    }

    public void stopRecording(View v){
        mediaRecorder.stop();
        mediaRecorder.release();
        audioText.setText("Your audio");

        startRecB.setVisibility(View.VISIBLE);
        stopRecB.setVisibility(View.INVISIBLE);
        playRecB.setVisibility(View.VISIBLE);
        stopPlayB.setVisibility(View.INVISIBLE);

    }

    public void playRecording (View v){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            audioText.setText("Playing...");
            startRecB.setVisibility(View.INVISIBLE);
            playRecB.setVisibility(View.INVISIBLE);
            stopPlayB.setVisibility(View.VISIBLE);

        } catch(IOException e){
            e.printStackTrace();
            Log.e("error","Exception in mediaPlayer");
        }
        mediaPlayer.start();

    }

    public void stopPlaying(View v){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            audioText.setText("Your audio");

            startRecB.setVisibility(View.VISIBLE);
            playRecB.setVisibility(View.VISIBLE);
            stopPlayB.setVisibility(View.INVISIBLE);
        }
    }
    /************************************* END MEDIA ************************************/

}