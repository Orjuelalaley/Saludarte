package com.example.saludarteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Inicializa FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a los elementos de la UI
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_sign_in);
        registerButton = findViewById(R.id.btn_register);

        // Agregar listener al botón de inicio de sesión
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            loginUser(email, password);
        });

        // Agregar listener al botón de registro
        registerButton.setOnClickListener(view -> {
            // Crear un nuevo Intent para abrir RegisterActivity
            Intent registerIntent = new Intent(LogInActivity.this, RegisterActivity.class);
            // Iniciar RegisterActivity
            startActivity(registerIntent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LogInActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();

                            // Lanzar MainActivity
                            Intent mainActivityIntent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();  // Opcional - si quieres que el usuario no pueda volver a esta actividad

                        } else {
                            // Inicio de sesión fallido
                            Toast.makeText(LogInActivity.this, "Inicio de sesión fallido: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
