package com.example.magalona_fk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.magalona_friendskeeper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = AppDatabase.getInstance(this);

        TextInputEditText etFullName = findViewById(R.id.etFullName);
        TextInputEditText etEmailSignup = findViewById(R.id.etEmailSignup);
        TextInputEditText etPasswordSignup = findViewById(R.id.etPasswordSignup);
        TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String email = etEmailSignup.getText().toString();
            String password = etPasswordSignup.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                User existingUser = db.userDao().getUserByUsername(email);
                if (existingUser != null) {
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(email, password, fullName);
                    db.userDao().insert(user);
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        tvLogin.setOnClickListener(v -> finish());
    }
}
