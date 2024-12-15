package com.example.notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.notes.Models.Note;
import com.example.notes.interfaces.NoteClickListner;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NotesAdpterList extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Note> notesList;
    NoteClickListner listner;

    public NotesAdpterList(Context context, List<Note> notesList, NoteClickListner listner) {
        this.context = context;
        this.notesList = notesList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.titletxt.setText(notesList.get(position).getTitle());
        holder.notestxt.setText(notesList.get(position).getNotes());
        holder.datetxt.setText(notesList.get(position).getDate());
        holder.datetxt.setSelected(true);

        Boolean isPinned = notesList.get(position).getPin();
        if(isPinned){
            holder.imageview.setImageResource(R.drawable.pin);
        }
        else {
            holder.imageview.setImageResource(0);
        }

        int color_code = getRandomColor();
        holder.cardview.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code));

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(notesList.get(holder.getAdapterPosition()));
            }
        });

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listner.onLongpress(notesList.get(holder.getAdapterPosition()),holder.cardview);
                return false;
            }
        });
    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);

        Random random = new Random();
        return colorCode.get(random.nextInt(colorCode.size()));
    }
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filterList(List<Note> filterList){
        notesList = filterList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView cardview;
    TextView notestxt,titletxt,datetxt;
    ImageView imageview;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview = itemView.findViewById(R.id.note_container);
        notestxt = itemView.findViewById(R.id.notesTxt);
        titletxt = itemView.findViewById(R.id.titletxt);
        datetxt = itemView.findViewById(R.id.datesTxt);
        imageview = itemView.findViewById(R.id.pin);
    }
}
