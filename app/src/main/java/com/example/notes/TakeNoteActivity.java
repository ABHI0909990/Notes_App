package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.Models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TakeNoteActivity extends AppCompatActivity {

    EditText titleEd,noteEd;
    ImageView saveBtn;
    Note notes;

    boolean isOldNotes = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        saveBtn = findViewById(R.id.savebtn);
        titleEd = findViewById(R.id.titleId);
        noteEd = findViewById(R.id.NotesEdt);

        notes = new Note();
        try{
            notes = (Note) getIntent().getSerializableExtra("old_notea");
            titleEd.setText(notes.getTitle());
            noteEd.setText(notes.getNotes());
            isOldNotes = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOldNotes) {
                    notes = new Note();
                }
                String title = titleEd.getText().toString();
                String description = noteEd.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(TakeNoteActivity.this,"Please enter Description",Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("EEE,d MMM YYYY HH:mm a");
                Date date = new Date();
                if (!isOldNotes) {
                    notes = new Note();
                }

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(format.format(date));

                Intent i = new Intent();
                i.putExtra("Notes",notes);
                setResult(Activity.RESULT_OK,i);
                finish();

            }
        });


    }
}