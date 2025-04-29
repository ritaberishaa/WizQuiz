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
    // lidhja me nderfaqen activity_login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);


        // krijimi i databasehelper
        databaseHelper = new DatabaseHelper(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // kontrollohet nese email dhe pasword jane bosh
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Fill in your email and password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = databaseHelper.getUserByEmail(email);
                if (cursor != null && cursor.moveToFirst()) { // nese moveToFirst eshte true
                    // shkon e lexon fjalekalimin e ruajtur
                    int colIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD);
                    String storedHashedPassword = cursor.getString(colIndex);
                    cursor.close();

                    // ketu perdore BCrypt per me krahasu se jane passwordet e njejta
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        Toast.makeText(LoginActivity.this,
                                "Login successful!",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "The password is incorrect!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "This email does not exist! Register first.",
                            Toast.LENGTH_SHORT).show();

                    if (cursor != null) cursor.close();
                }
            }
        });

//      e dergon ne ForgotPasswordActivity
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }

         });

//       e dergon ne SignUpActivity
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
