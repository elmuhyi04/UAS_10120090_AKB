package com.example.uas_10120090_akb;
//NIM   : 10120090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Note> arrayList;
    private OnItemClickListener onItemClickListener;

    public NoteAdapter(Context context, ArrayList<Note> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, judul, kategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.ttanggal);
            judul = itemView.findViewById(R.id.tjudul);
            kategori = itemView.findViewById(R.id.tkategori);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(arrayList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Note note) {
            tanggal.setText(note.getTanggal());
            judul.setText(note.getJudul());
            kategori.setText(note.getKategori());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Note note);
    }
}
