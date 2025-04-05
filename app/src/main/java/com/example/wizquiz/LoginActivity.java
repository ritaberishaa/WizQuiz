package com.example.wizquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnSignUp, btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializo elementet nga layout‑i
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // Shto event‑in për Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loginUser(); }
        });

        // Shto event‑in për navigim tek Sign Up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Shto event‑in për Forget Password
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validimi i email‑it
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Vendosni një email valid");
            etEmail.requestFocus();
            return;
        }

        // Validimi i fjalëkalimit
        if(password.isEmpty()){
            etPassword.setError("Vendosni fjalëkalimin");
            etPassword.requestFocus();
            return;
        }
        // Kontroll për të paktën një numër dhe një karakter special
        if(!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*]).+$")){
            etPassword.setError("Fjalëkalimi duhet të përmbajë të paktën një numër dhe një karakter special");
            etPassword.requestFocus();
            return;
        }

        // Këtu kontrollohet në databazë (simulim në këtë shembull)
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

        // Shto animacion në tranzicionin tek HomeActivity
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
