package com.example.myquarantine.application.settings;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myquarantine.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    private TextView outbreakDateText;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private SettingsViewModel settingsViewModel;

    private AlertDialog signOutDialog;
    private AlertDialog.Builder signOutBuilder;
    private AlertDialog deleteNoteDialog;
    private AlertDialog.Builder deleteNoteBuilder;

    private LinearLayout delNoteLayout;
    private LinearLayout signOutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        outbreakDateText = findViewById(R.id.outbreakDate);

        setLayouts();
        setConfirmDeleteNotesDialog();
        setConfirmSignOutDialog();

        setCalendarDialogConfiguration();
        setOutbreakDayText();

    }

    public void setLayouts(){
        delNoteLayout = findViewById(R.id.delNotesLayout);
        delNoteLayout.setOnTouchListener( new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        delNoteLayout.setBackgroundColor(getResources().getColor(R.color.settingsItemSelected));
                        deleteNotesRequested();
                        break;
                    case MotionEvent.ACTION_UP:
                        delNoteLayout.setBackgroundColor(getResources().getColor(R.color.settingsBackground));
                        break;
                }
                return true;
            }
        });

        signOutLayout = findViewById(R.id.signOutLayout);
        signOutLayout.setOnTouchListener( new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        signOutLayout.setBackgroundColor(getResources().getColor(R.color.settingsItemSelected));
                        signOutRequested();
                        break;
                    case MotionEvent.ACTION_UP:
                        signOutLayout.setBackgroundColor(getResources().getColor(R.color.settingsBackground));
                        break;
                }
                return true;
            }
        });
    }

    public void setConfirmDeleteNotesDialog(){
        deleteNoteBuilder = new AlertDialog.Builder(this);
        deleteNoteBuilder.setMessage("Are you sure you want to delete all your notes?");
        deleteNoteBuilder.setTitle("Confirmation request");

        deleteNoteBuilder.setPositiveButton("Yes, I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllNotes();
                dialog.cancel();
            }
        });

        deleteNoteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingsActivity.this,"Nothing deleted",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

    }

    public void setConfirmSignOutDialog(){
        signOutBuilder = new AlertDialog.Builder(this);
        signOutBuilder.setMessage("Are you sure you sign out your account?");
        signOutBuilder.setTitle("Confirmation request");

        signOutBuilder.setPositiveButton("Yes, I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
                dialog.cancel();
            }
        });

        signOutBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingsActivity.this,"Sign out not completed",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

    }

    public void setOutbreakDayText(){
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        if (!prefs.contains("yearOutbreak")){ return; }; //if there is not, quit

        String year = prefs.getString("yearOutbreak","0");
        String mon = prefs.getString("monthOutbreak","0");
        String day = prefs.getString("dayOutbreak","0");

        String month = String.valueOf(Integer.parseInt(mon) + 1);
        String outbreakDate = day + "/" + month +"/" + year;
        outbreakDateText.setText(outbreakDate);
    }

    private void setCalendarDialogConfiguration(){
        outbreakDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SettingsActivity.this,
                        onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int mon, int dayOfMonth) {
                int month = mon + 1;
                String outbreakDate = dayOfMonth + "/" + month +"/" + year;
                outbreakDateText.setText(outbreakDate);

                SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor pEditor = prefs.edit();
                pEditor.putString("yearOutbreak",String.valueOf(year));
                pEditor.putString("monthOutbreak",String.valueOf(month));
                pEditor.putString("dayOutbreak", String.valueOf(dayOfMonth));
                pEditor.apply();

            }
        };
    }


    public void signOutRequested(){
        signOutDialog = signOutBuilder.create();
        signOutDialog.show();
    }

    public void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SettingsActivity.this, "Sign out completed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void deleteNotesRequested(){
        deleteNoteDialog = deleteNoteBuilder.create();
        deleteNoteDialog.show();
    }

    public void deleteAllNotes (){
        settingsViewModel.deleteAllNotes();
        Toast.makeText(this,"All notes deleted",Toast.LENGTH_LONG).show();
    }
}
