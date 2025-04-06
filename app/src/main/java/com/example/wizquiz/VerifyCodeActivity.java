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
        // Përdor layout‑in e ri me UI të përputhur
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
                    Toast.makeText(VerifyCodeActivity.this, "Ju lutem futni kodin OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                OTPData otpData = databaseHelper.getOTPInfo(email);
                if (otpData == null) {
                    Toast.makeText(VerifyCodeActivity.this, "Nuk u gjend kod OTP. Provoni përsëri.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (System.currentTimeMillis() > otpData.getExpirationTime()) {
                    Toast.makeText(VerifyCodeActivity.this, "Kodi OTP ka skaduar. Ju lutem kërkoni një të ri.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!inputOTP.equals(otpData.getOtpCode())) {
                    Toast.makeText(VerifyCodeActivity.this, "Kodi OTP nuk është i saktë", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(VerifyCodeActivity.this, "Verifikimi i OTP-së u krye me sukses!", Toast.LENGTH_SHORT).show();
                // Pas verifikimit të suksesshëm, lexo se si të vazhdosh:
                // këtu po e drejtojmë përdoruesin te ResetPasswordActivity; mund të jetë edhe një aktivitet kryesor sipas logjikës së aplikacionit.
                Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();
            }
        });
    }
}
