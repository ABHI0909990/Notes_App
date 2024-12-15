package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notes.Adapter.NotesAdpterList;
import com.example.notes.DataBase.RoomDB;
import com.example.notes.Models.Note;
import com.example.notes.interfaces.NoteClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesAdpterList notesAdpterList;
    RoomDB database;
    List<Note> notelist = new ArrayList<>();
    Note NoteSelected;

    FloatingActionButton fabBtn;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         recyclerView = findViewById(R.id.noteRC);
         fabBtn = findViewById(R.id.addBTN);
         searchView = findViewById(R.id.SearchView);
         database = RoomDB.getInstance(this);
         notelist = database.mainD().getAll();
         updateRecycle(notelist);

         fabBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(MainActivity.this,TakeNoteActivity.class);
                 startActivityForResult(i,101);
             }
         });

         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String s) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newtext) {
                 Filter(newtext);
                 return true;
             }
         });
    }

    private void Filter(String newtext){
        List<Note> filterList = new ArrayList<>();
        for(Note singleNote : notelist){
            if(singleNote.getTitle().toLowerCase().contains(newtext.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newtext.toLowerCase())){
                filterList.add(singleNote);
            }
        }
        notesAdpterList.filterList(filterList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                Note new_notes = null;
                if (data != null) {
                    new_notes = (Note) data.getSerializableExtra("Notes");
                }
                database.mainD().insert(new_notes);
                notelist.clear();
                notelist.addAll(database.mainD().getAll());
                notesAdpterList.notifyDataSetChanged();
            }
        }else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Note new_notes = null;
                if (data != null) {
                    new_notes = (Note) data.getSerializableExtra("Notes");
                }
                database.mainD().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                notelist.clear();
                notelist.addAll(database.mainD().getAll());
                notesAdpterList.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycle(List<Note> notelist){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesAdpterList = new NotesAdpterList(MainActivity.this,notelist,noteClickListner);
        recyclerView.setAdapter(notesAdpterList);
    }

    private final NoteClickListner noteClickListner = new NoteClickListner() {
        @Override
        public void onClick(Note note) {
            Intent i = new Intent(MainActivity.this,TakeNoteActivity.class);
            i.putExtra("old_notes",note);
            startActivityForResult(i,102);
        }

        @Override
        public void onLongpress(Note note, CardView cardview) {
            NoteSelected = note;
            showPop(cardview);
        }
    };
    private void showPop(CardView cardView){
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.pin){
            if(NoteSelected.getPin()){
                database.mainD().pin(NoteSelected.getID(),false);
                Toast.makeText(this,"UnPinned",Toast.LENGTH_SHORT).show();
            }else {
                database.mainD().pin(NoteSelected.getID(),true);
                Toast.makeText(this,"Pinned",Toast.LENGTH_SHORT).show();
            }
            notelist.clear();
            notelist.addAll(database.mainD().getAll());
            notesAdpterList.notifyDataSetChanged();
            return  true;
        } else if (item.getItemId() == R.id.delete) {
            database.mainD().delete(NoteSelected);
            notelist.remove(NoteSelected);
            notesAdpterList.notifyDataSetChanged();
            Toast.makeText(this, "Note is Deleted successfully..", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

