package com.example.saludarteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saludarteapp.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        binding.btnCompleteRegistration.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        if (binding.etRegisterEmail.getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.etRegisterEmail.getText().toString()).matches()){
            binding.etRegisterEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.etRegisterEmail.setError(null);
        if (binding.etRegisterPassword.getText().toString().isEmpty()){
            binding.etRegisterPassword.setError(getString(R.string.error_pass_label));
            return;
        } else binding.etRegisterPassword.setError(null);
        progressDialog.setMessage("Registrando Usuario...");
        progressDialog.show();
        binding.btnCompleteRegistration.setVisibility(View.GONE);

    }

}
