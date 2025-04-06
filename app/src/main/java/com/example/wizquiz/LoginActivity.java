package com.example.wizquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword, tvSignUp;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        databaseHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Plotësoni email-in dhe fjalëkalimin!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = databaseHelper.getUserByEmail(email);
                if (cursor != null && cursor.moveToFirst()) {
                    int colIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD);
                    String storedHashedPassword = cursor.getString(colIndex);
                    cursor.close();

                    // Verifikojmë passwordin duke përdorur BCrypt
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        Toast.makeText(LoginActivity.this,
                                "Login i suksesshëm!",
                                Toast.LENGTH_SHORT).show();

                        // Kaloni tek aktiviteti tjetër pas login
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
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

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,
                        "Opsioni 'Forgot Password' nuk është implementuar!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
