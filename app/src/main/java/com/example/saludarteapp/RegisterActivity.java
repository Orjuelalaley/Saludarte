package com.example.saludarteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saludarteapp.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        binding.btnCompleteRegistration.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        if (Objects.requireNonNull(binding.registerEmail.getEditText()).getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.getEditText().getText().toString()).matches()){
            binding.registerEmail.setError(getString(R.string.mail_error_label));
            return;
        } else {
            binding.registerEmail.setErrorEnabled(false);
        }

        if (Objects.requireNonNull(binding.registerPass.getEditText()).getText().toString().isEmpty()) {
            binding.registerPass.setError(getString(R.string.error_pass_label));
            return;
        } else {
            binding.registerPass.setError(null);
        }

        if (Objects.requireNonNull(binding.registerConfirmPassword.getEditText()).getText().toString().isEmpty()) {
            binding.registerConfirmPassword.setError("Por favor, confirma la contraseña");
            return;
        } else {
            binding.registerConfirmPassword.setError(null);
        }

        String password = binding.registerPass.getEditText().getText().toString();
        String confirmPassword = binding.registerConfirmPassword.getEditText().getText().toString();

        if (!password.equals(confirmPassword)) {
            binding.registerConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        } else {
            binding.registerConfirmPassword.setError(null);
        }

        // Si la validación de la contraseña y la confirmación es exitosa, procede con el registro.
        progressDialog.setMessage("Registrando Usuario...");
        progressDialog.show();
        binding.btnCompleteRegistration.setVisibility(View.GONE);

// Aquí puedes agregar la lógica para registrar al usuario.


        mAuth.createUserWithEmailAndPassword(binding.registerEmail.getEditText().getText().toString(),
                        binding.registerPass.getEditText().getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        progressDialog.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Usuario registrado exitosamente")
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.dismiss();
                                    finish();
                                });
                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                        binding.btnCompleteRegistration.setVisibility(View.VISIBLE);
                        binding.registerEmail.getEditText().setText("");
                        Objects.requireNonNull(binding.registerPass.getEditText()).setText("");
                    } else {
                        progressDialog.cancel();
                        binding.btnCompleteRegistration.setVisibility(View.VISIBLE);
                        if (task.isCanceled()) {
                            showErrorDialog("Hubo un error al registrar el usuario");
                            binding.btnCompleteRegistration.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
