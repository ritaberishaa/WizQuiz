package com.example.wizquiz;  // ose paketa jote

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;
    private TextView tvAlreadyHaveAccount;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);

        databaseHelper = new DatabaseHelper(this);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                        || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "Please fill in all fields!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this,
                            "Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                databaseHelper.addUser(email, hashedPassword, name, phone);

                Toast.makeText(SignUpActivity.this,
                        "Account created successfully",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
