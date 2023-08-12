package com.example.uas_10120090_akb;
//NIM   : 10120090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NoteControl extends Fragment {

    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private TextView empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        FirebaseApp.initializeApp(requireContext());
        database = FirebaseDatabase.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("notes");

        Map<String, String> data = new HashMap<>();
        data.put("title", "New Note Added");


        FirebaseFirestore.getInstance().collection("notifications").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        FloatingActionButton add = view.findViewById(R.id.addNote);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(requireContext()).inflate(R.layout.add_note_dialog, null);
                TextInputLayout tanggalLayout, judulLayout, kategoriLayout, isicatatanLayout;
                tanggalLayout = view1.findViewById(R.id.tanggalLayout);
                judulLayout = view1.findViewById(R.id.judulLayout);
                kategoriLayout = view1.findViewById(R.id.kategoriLayout);
                isicatatanLayout = view1.findViewById(R.id.isicatatanLayout);
                TextInputEditText tanggalET, judulET, kategoriET, isicatatanET;
                tanggalET = view1.findViewById(R.id.tanggalET);
                judulET = view1.findViewById(R.id.judulET);
                kategoriET = view1.findViewById(R.id.kategoriET);
                isicatatanET = view1.findViewById(R.id.isicatatanET);

                org.joda.time.DateTime dateTime = new org.joda.time.DateTime();
                tanggalET.setText(dateTime.toString("dd-MM-yyyy"));


                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Add")
                        .setView(view1)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(tanggalET.getText()).toString().isEmpty()) {
                                    tanggalLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(judulET.getText()).toString().isEmpty()) {
                                    judulLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(kategoriET.getText()).toString().isEmpty()) {
                                    kategoriLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(isicatatanET.getText()).toString().isEmpty()) {
                                    isicatatanLayout.setError("This field is required!");
                                } else {
                                    ProgressDialog dialog = new ProgressDialog(requireContext());
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    Note note = new Note();
                                    note.setTanggal(tanggalET.getText().toString());
                                    note.setJudul(judulET.getText().toString());
                                    note.setKategori(kategoriET.getText().toString());
                                    note.setIsiCatatan(isicatatanET.getText().toString());
                                    database.getReference().child("notes").push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(requireContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(requireContext(), "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        empty = view.findViewById(R.id.empty);
        recyclerView = view.findViewById(R.id.recycler);

        database.getReference().child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Note> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    Objects.requireNonNull(note).setKey(dataSnapshot.getKey());
                    arrayList.add(note);
                }

                if (arrayList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                NoteAdapter adapter = new NoteAdapter(requireContext(), arrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Note note) {
                        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_note_dialog, null);
                        TextInputLayout tanggalLayout, judulLayout, kategoriLayout, isicatatanLayout;
                        TextInputEditText tanggalET, judulET, kategoriET, isicatatanET;

                        tanggalET = view.findViewById(R.id.tanggalET);
                        judulET = view.findViewById(R.id.judulET);
                        kategoriET = view.findViewById(R.id.kategoriET);
                        isicatatanET = view.findViewById(R.id.isicatatanET);
                        tanggalLayout = view.findViewById(R.id.tanggalLayout);
                        judulLayout = view.findViewById(R.id.judulLayout);
                        kategoriLayout = view.findViewById(R.id.kategoriLayout);
                        isicatatanLayout = view.findViewById(R.id.isicatatanLayout);

                        tanggalET.setText(note.getTanggal());
                        judulET.setText(note.getJudul());
                        kategoriET.setText(note.getKategori());
                        isicatatanET.setText(note.getIsiCatatan());

                        ProgressDialog progressDialog = new ProgressDialog(requireContext());

                        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(tanggalET.getText()).toString().isEmpty()) {
                                            tanggalLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(judulET.getText()).toString().isEmpty()) {
                                            judulLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(kategoriET.getText()).toString().isEmpty()) {
                                            kategoriLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(isicatatanET.getText()).toString().isEmpty()) {
                                            isicatatanLayout.setError("This field is required!");
                                        } else {
                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Note note1 = new Note();
                                            note1.setTanggal(tanggalET.getText().toString());
                                            note1.setJudul(judulET.getText().toString());
                                            note1.setKategori(kategoriET.getText().toString());
                                            note1.setIsiCatatan(isicatatanET.getText().toString());
                                            database.getReference().child("notes").child(note.getKey()).setValue(note1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(requireContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(requireContext(), "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("Deleting...");
                                        progressDialog.show();
                                        database.getReference().child("notes").child(note.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


}
