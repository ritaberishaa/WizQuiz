package com.example.wizquiz;  // ose paketa jote

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword, tvSignUp;

    private DatabaseHelper databaseHelper; // klasa jote e SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // emri i skedarit XML (p.sh. login.xml)

        // 1) Lidhu me komponentët e layout-it sipas ID-ve që ke
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);   // Kjo zëvendëson tvCreateAccount

        // 2) Inicializo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // 3) Kur klikon butonin LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validim fillestar
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Plotësoni email-in dhe fjalëkalimin!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kontroll në DB
                Cursor cursor = databaseHelper.getUserByEmail(email);
                if (cursor != null && cursor.moveToFirst()) {
                    // Merr kolonën e password-it
                    int colIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD);
                    String storedPassword = cursor.getString(colIndex);
                    cursor.close();

                    if (storedPassword.equals(password)) {
                        Toast.makeText(LoginActivity.this,
                                "Login i suksesshëm!",
                                Toast.LENGTH_SHORT).show();

                        // p.sh.: startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        // finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Fjalëkalimi nuk është i saktë!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Ky email nuk ekziston! Regjistrohu më parë.",
                            Toast.LENGTH_SHORT).show();
                    if (cursor != null) cursor.close();
                }
            }
        });

        // 4) Forgot Password (opsionale)
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Këtu mund të hapësh ForgotPasswordActivity ose të bësh diçka tjetër
                Toast.makeText(LoginActivity.this,
                        "Opsioni 'Forgot Password' nuk është implementuar!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 5) Sign Up link
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kalo te SignUpActivity
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
