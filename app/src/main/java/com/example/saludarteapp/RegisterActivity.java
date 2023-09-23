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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

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
        User user = new User(binding.etRegisterEmail.getText().toString(), binding.etRegisterPassword.getText().toString());

        mAuth.createUserWithEmailAndPassword(binding.etRegisterEmail.getText().toString(),
                binding.etRegisterPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressDialog.cancel();
                binding.btnCompleteRegistration.setVisibility(View.VISIBLE);
                binding.etRegisterEmail.setText("");
                binding.etRegisterPassword.setText("");
                DatabaseReference userRef = db.getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                userRef.setValue(user);
    }
        });
    }
}
