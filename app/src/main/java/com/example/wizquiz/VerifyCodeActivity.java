package com.example.wizquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class VerifyCodeActivity extends AppCompatActivity {

    private TextInputEditText etOTP;
    private MaterialButton btnVerify;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        etOTP = findViewById(R.id.etOTP);
        btnVerify = findViewById(R.id.btnVerify);
        databaseHelper = new DatabaseHelper(this);
        email = getIntent().getStringExtra("EMAIL");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputOTP = etOTP.getText().toString().trim();
                if (inputOTP.isEmpty()) {
                    Toast.makeText(VerifyCodeActivity.this, "Please enter the OTP code.", Toast.LENGTH_SHORT).show();
                    return;
                }
                OTPData otpData = databaseHelper.getOTPInfo(email);
                if (otpData == null) {
                    Toast.makeText(VerifyCodeActivity.this, "No OTP code found. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (System.currentTimeMillis() > otpData.getExpirationTime()) {
                    Toast.makeText(VerifyCodeActivity.this, "The OTP code has expired. Please request a new one.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!inputOTP.equals(otpData.getOtpCode())) {
                    Toast.makeText(VerifyCodeActivity.this, "The OTP code is incorrect.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(VerifyCodeActivity.this, "OTP verification completed successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();
            }
        });
    }
}
