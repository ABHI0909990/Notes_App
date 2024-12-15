package com.example.notes.interfaces;

import androidx.cardview.widget.CardView;

import com.example.notes.Models.Note;

public interface NoteClickListner {

    void onClick(Note note);

    void onLongpress(Note note, CardView cardview);
}
