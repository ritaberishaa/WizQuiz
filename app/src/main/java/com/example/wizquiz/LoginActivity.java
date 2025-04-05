package com.example.wizquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    // Nëse ke elementin tvSignUp në layout, atëherë shkarko atë (nëse jo, kjo do të mbetet null)
    private TextView tvSignUp, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Lidhja e elementeve të UI me kodin
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Nëse ke elementin tvSignUp në layout, shkarko atë; nëse jo, mos e shkakto gabime
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Veprimi për butonin Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Nëse tvSignUp nuk është null, vendos navigimin tek SignUpActivity
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });
        }

        // Nëse tvForgotPassword nuk është null, vendos navigimin tek ForgotPasswordActivity
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            });
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validimi i email‑it
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Vendosni një email valid");
            etEmail.requestFocus();
            return;
        }

        // Validimi i fjalëkalimit
        if (password.isEmpty()) {
            etPassword.setError("Vendosni fjalëkalimin");
            etPassword.requestFocus();
            return;
        }
        // Kontroll për të paktën një numër dhe një karakter special
        if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*]).+$")) {
            etPassword.setError("Fjalëkalimi duhet të përmbajë të paktën një numër dhe një karakter special");
            etPassword.requestFocus();
            return;
        }

        // Simulimi i verifikimit të përdoruesit
        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
