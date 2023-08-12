package com.example.uas_10120090_akb;
//NIM   : 10102090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;
            switch (item.getItemId()){
                case R.id.profile:
                    f = new FragmentProfile();
                    break;
                case R.id.diary:
                    f = new NoteControl();
                    break;
                case R.id.infoapps:
                    Intent intent = new Intent(MainActivity.this, SlideActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    showLogoutConfirmation();
                    return true;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, f).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        FirebaseApp.initializeApp(this);
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Apakah Anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutUser();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("30905424194-nfdpktjpk8t7ohfg09q787gv5hg8efhl.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            clearSessionData();
                            showLoginScreen();
                        } else {

                            Log.e(TAG, "Logout failed", task.getException());
                        }
                    }
                });
    }
    private void clearSessionData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
