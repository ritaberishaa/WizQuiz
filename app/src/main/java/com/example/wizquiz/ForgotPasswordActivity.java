package com.example.wizquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private MaterialButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Kur klikohet SUBMIT
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                } else {
                    // Këtu implemento logjikën e dërgimit të link‑ut ose reset password
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent", Toast.LENGTH_SHORT).show();
                    finish(); // Mbyll këtë aktivitet ose shko diku tjetër
                }
            }
        });
    }
}
