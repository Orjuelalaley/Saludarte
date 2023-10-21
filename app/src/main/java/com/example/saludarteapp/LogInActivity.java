package com.example.saludarteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saludarteapp.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    FirebaseApp firebaseApp;

    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        firebaseApp = FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        binding.btnSignIn.setOnClickListener(v -> loginUser());
        binding.btnRegister.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        if(Objects.requireNonNull(binding.etEmail).getText().toString().isEmpty() ) {
            binding.btnSignIn.setError(getString(R.string.mail_error_label));
            return;
        }else binding.etEmail.setError(null);
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()){
                binding.etEmail.setError(getString(R.string.error_email_label));
                return;
        }else binding.etEmail.setError(null);
        if (Objects.requireNonNull(binding.etPassword).getText().toString().isEmpty()) {
                binding.etPassword.setError(getString(R.string.error_pass_label));
                return;
        }else binding.etPassword.setError(null

        );
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            progressDialog.cancel();
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            progressDialog.cancel();
            Snackbar.make(binding.getRoot(), "Error al iniciar sesión", Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}