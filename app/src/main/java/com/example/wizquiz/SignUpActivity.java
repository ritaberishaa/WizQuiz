package com.example.wizquiz;  // ose paketa jote

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    // Duke u bazuar në ID-të e tua ekzistuese në layout
    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;
    private TextView tvAlreadyHaveAccount;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);  //layout-i yt

        // 1) Lidhja me view-t, identik me ID e tua
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);

        // 2) Inicializimi i DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // 3) Klikimi i butonit "Create Account"
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lexojmë vlerat nga EditText
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Kontrollet e thjeshta
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                        || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "Ju lutem plotësoni të gjitha fushat!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this,
                            "Fjalëkalimet nuk përputhen!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // (Opsionale) Hash password-in para se ta ruajmë
                // p.sh.: password = HashUtil.hashPassword(password);

                // Shtojmë user-in në DB
                databaseHelper.addUser(email, password, name, phone);

                // Mesazh suksesi
                Toast.makeText(SignUpActivity.this,
                        "Llogaria u krijua me sukses!",
                        Toast.LENGTH_SHORT).show();

                // Hap LoginActivity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        // 4) Nëse ke llogari, klikon "Kam tashmë llogari?"
        tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kalon te LoginActivity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
