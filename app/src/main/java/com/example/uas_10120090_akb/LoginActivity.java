package com.example.uas_10120090_akb;
//NIM   : 10102090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnMasuk;
    private Button btnDaftar;
    private SignInButton btnGoogle;
    private GoogleSignInClient nGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        edtEmail = (EditText)findViewById(R.id.tv_email);
        edtPass = (EditText)findViewById(R.id.tv_pass);
        btnMasuk = (Button) findViewById(R.id.loginButton);
        btnDaftar = (Button)findViewById(R.id.daftarButton);
        btnGoogle = findViewById(R.id.btn_google);


        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);

        btnGoogle.setOnClickListener(v -> {
            googleSignIn();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("30905424194-nfdpktjpk8t7ohfg09q787gv5hg8efhl.apps.googleusercontent.com")
                .requestEmail()
                .build();
        nGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignIn(){
        Intent signInIntent = nGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1001);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001){
            Task <GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GOOGLE SIGN IN", "FirebaseAuthWithGoogle : " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e){
                Log.w("GOOGLE SIGN IN", "Google Sign In Failed", e);
            }
        }
    }
    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }


        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());


                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("GOOGLE SIGN IN", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Log.w("GOOGLE SIGN IN", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Google Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }
    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }


        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());


                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());


        writeNewAdmin(user.getUid(), username, user.getEmail());


        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Required");
            result = false;
        } else {
            edtEmail.setError(null);
        }

        if (TextUtils.isEmpty(edtPass.getText().toString())) {
            edtPass.setError("Required");
            result = false;
        } else {
            edtPass.setError(null);
        }

        return result;
    }


    private void writeNewAdmin(String userId, String name, String email) {
        Admin admin = new Admin(name, email);

        mDatabase.child("admins").child(userId).setValue(admin);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.loginButton) {
            signIn();
        } else if (i == R.id.daftarButton) {
            signUp();
        }
    }

}