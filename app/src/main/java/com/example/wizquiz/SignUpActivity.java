package com.example.wizquiz;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper; // për operacione në databazë

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { registerUser(); }
        });
    }

    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Vendosni një email valid");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Vendosni fjalëkalimin");
            etPassword.requestFocus();
            return;
        }
        if(!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*]).+$")){
            etPassword.setError("Fjalëkalimi duhet të përmbajë të paktën një numër dhe një karakter special");
            etPassword.requestFocus();
            return;
        }

        // Hashing i fjalëkalimit para se të ruhet
        String hashedPassword = HashUtil.sha256(password);

        // Ruaj të dhënat e përdoruesit në databazë
        dbHelper.addUser(email, hashedPassword);
        Toast.makeText(this, "Regjistrim i suksesshëm", Toast.LENGTH_SHORT).show();
        finish(); // Kthehu në LoginActivity
    }
}
