package com.example.wizquiz;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.content.Intent;

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
                String codeInput = etOTP.getText().toString().trim();
                if (codeInput.isEmpty()) {
                    Toast.makeText(VerifyCodeActivity.this, "Ju lutem futni kodin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                OTPData otpData = databaseHelper.getOTPInfo(email);
                if (otpData == null) {
                    Toast.makeText(VerifyCodeActivity.this, "S’ka kod të regjistruar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long now = System.currentTimeMillis();
                if (now > otpData.getExpirationTime()) {
                    Toast.makeText(VerifyCodeActivity.this, "Kodi ka skaduar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!codeInput.equals(otpData.getOtpCode())) {
                    Toast.makeText(VerifyCodeActivity.this, "Kodi nuk është i saktë!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(VerifyCodeActivity.this, "Kodi u verifikua me sukses!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                i.putExtra("EMAIL", email);
                startActivity(i);
                finish();
            }
        });
    }
}
