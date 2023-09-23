package com.example.saludarteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saludarteapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        // Referencias a los elementos de la UI
        // Agregar listener al botón de registro
        binding.btnCompleteRegistration.setOnClickListener(view -> {
            String email = binding.emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            registerUser(email, password);
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();

                            // Lanzar MainActivity
                            Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();  // Opcional - si quieres que el usuario no pueda volver a esta actividad

                        } else {
                            // Registro fallido
                            Toast.makeText(RegisterActivity.this, "Registro fallido: " + Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
