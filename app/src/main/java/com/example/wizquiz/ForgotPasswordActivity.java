package com.example.wizquiz;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { resetPassword(); }
        });
    }

    private void resetPassword(){
        String email = etEmail.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Vendosni një email valid");
            etEmail.requestFocus();
            return;
        }

        // Simulimi i dërgimit të një link‑u për rivendosjen e fjalëkalimit
        Toast.makeText(this, "Link për rivendosjen e fjalëkalimit është dërguar në emailin tuaj", Toast.LENGTH_SHORT).show();
        finish();
    }
}
