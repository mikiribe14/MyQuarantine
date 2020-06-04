package com.example.myquarantine.application.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquarantine.R;
import com.example.myquarantine.data.Note;
import com.example.myquarantine.application.editnote.EditNoteActivity;
import com.example.myquarantine.application.settings.SettingsActivity;
import com.example.myquarantine.application.viewnote.ViewNoteActivity;
import com.example.myquarantine.application.world.WorldActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnListItemClickListener {
    private RecyclerView noteList;
    private NoteThumbnailAdapter noteThumbnailAdapter;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView username;
    private TextView mailname;

    private HomeViewModel homeViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        setNoteListConfiguration();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //initialNotes();
        refreshNoteList();

        toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpDrawerLayout();
        setUpNavigationView();
        checkIfUserSignedIn();
    }

    public void initialNotes(){
        homeViewModel.saveNote(30,"More coding","Coding all day :) ","");
        homeViewModel.saveNote(32,"Take a walk","I finally could be outside!! ","");
        homeViewModel.saveNote(34,"Back to work","Happily ending my 'vacations' ","");
        homeViewModel.saveNote(25,"Coding","","");
        homeViewModel.saveNote(2,"Buy WC paper","I really needed it, no panic","");
    }

    @Override
    protected void onResume() {
        checkIfUserSignedIn();
        super.onResume();
    }

    @Override
    protected void onStop() {
        ref.removeEventListener(cListener);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        ref.addChildEventListener(cListener);
        super.onRestart();
    }


    public void setNoteListConfiguration(){
        noteList = findViewById(R.id.rv);
        noteList.hasFixedSize();
        noteList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setUpDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        return false;   //for not displaying the 3-dots option in the toolbar
    }

    public void setUpNavigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        username =  headerView.findViewById(R.id.nav_header_username);
        mailname = headerView.findViewById(R.id.nav_header_mail);
    }



    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build() );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.common_google_signin_btn_text_light_focused)
                .build();
        startActivityForResult(signInIntent, 1);
    }

    public void checkIfUserSignedIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null){
            Toast.makeText(this, "Welcome back, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            username.setText(user.getDisplayName());
            mailname.setText(user.getEmail());
            setUpFirebaseDB();

        } else{
            signIn();
        }
    }

    private ChildEventListener cListener;
    public void setUpFirebaseDB(){
         /*     tindria sentit si el usuari només pogues accedir a lo seu. no es aixi pq s'accedeix
                sempre desde local. So, millor no relacionar res amb usuari.
         userId = FirebaseAuth.getInstance().getUid();
         ref = database.getReference(userId);
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        cListener =  ref.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {   }

               @Override
               public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final String title =  (String) dataSnapshot.child("title").getValue();
                    final String description =  (String) dataSnapshot.child("description").getValue();
                    Long daysLong = (Long) dataSnapshot.child("daySinceOutbreak").getValue();
                    final int days = daysLong.intValue();
                    final String audioPath =  (String) dataSnapshot.child("audioPath").getValue();
                    Long idLong = (Long) dataSnapshot.child("id").getValue();
                    final int id = idLong.intValue();

                   homeViewModel.getNote(id).observe(HomeActivity.this, new Observer<Note>() {
                       @Override
                       public void onChanged(Note note) {
                           note.setTitle(title);
                           note.setDescription(description);
                           note.setDaySinceOutbreak(days);
                           note.setAudioPath(audioPath);
                           homeViewModel.updateNote(note);
                           refreshNoteList();
                       }
                   });
               }

               @Override
               public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                   Log.i("firebase","sha eliminat un child");
                   int id = Integer.parseInt(dataSnapshot.getKey());
                   homeViewModel.getNote(id).observe(HomeActivity.this, new Observer<Note>() {
                       @Override
                       public void onChanged(Note note) {
                           if (note==null){return; };
                           note.setStoredInCloud(false);
                           homeViewModel.updateNote(note);
                           refreshNoteList();
                       }
                   });

               }

             @Override
             public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

             @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {    }
           });

    }


    public void refreshNoteList(){
        homeViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {       //refreshes thumbnails
                noteThumbnailAdapter = new NoteThumbnailAdapter(notes,HomeActivity.this,getApplicationContext());
                noteList.setAdapter(noteThumbnailAdapter);
            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navview_menu_items, menu);
        return true;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_world:
                Log.i("navigation","World clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intentWorld = new Intent (this, WorldActivity.class);
                startActivity(intentWorld);

                return true;
            case R.id.nav_settings:
                Log.i("navigation","Settings clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intentSettings = new Intent (this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            default:
                return false;
        }
    }


    public void startEditNote(View view){       //fab clicked
        Intent intent = new Intent (this, EditNoteActivity.class);
        startActivity(intent);
    }


    public void onListItemClick(int clickedItemIndex) { //item clicked
        Note note = noteThumbnailAdapter.getItem(clickedItemIndex);
        Intent intent = new Intent (this, ViewNoteActivity.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onLongListItemClick(int clickedItemIndex) { //item long clicked
        Note note = noteThumbnailAdapter.getItem(clickedItemIndex);
        showPopUp(note, clickedItemIndex);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void showPopUp(Note notE, int position)
    {
        final ViewGroup root = HomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Note note = notE;
        final View view = new View(getApplicationContext());

        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        root.addView(view);

        float x = 8;      //in n_thumbnail_item.xml:    android:layout_marginStart="16px"
        float y = (8 + 110) * position + 300; //        android:layout_height="110px" and android:layout_marginTop="12px"
        view.setX(x);
        view.setY(y);

        PopupMenu popup = new PopupMenu(getApplicationContext(), view, Gravity.CENTER);
        popup.setForceShowIcon(true);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        Intent intent = new Intent (HomeActivity.this, EditNoteActivity.class);
                        intent.putExtra("id", note.getId());
                        startActivity(intent);
                        return true;

                    case R.id.saveInCloud:
                        if (note.getStoredInCloud()){
                            eraseFromCloud(note);
                        } else{
                            saveInCloud(note);
                        }
                        refreshNoteList();
                        return true;

                    case R.id.delete:
                        if (note.getStoredInCloud()) { eraseFromCloud(note); }
                        homeViewModel.deleteNote(note.getId());
                        refreshNoteList();
                        Toast.makeText(HomeActivity.this,"Note deleted",Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return true;
                }
            }
        });

        popup.inflate(R.menu.popup_items);
        popup.show();

    }


    public void saveInCloud(Note note){
        note.setStoredInCloud(true);
        homeViewModel.updateNote(note);
        ref.child(String.valueOf(note.getId())).setValue(note);
    }

    public void eraseFromCloud(Note note){
        ref.child(String.valueOf(note.getId())).removeValue();
        note.setStoredInCloud(false);
        homeViewModel.updateNote(note);
    }

}
