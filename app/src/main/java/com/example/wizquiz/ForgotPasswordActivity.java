package com.example.wizquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private MaterialButton btnSubmit;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        databaseHelper = new DatabaseHelper(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                    return;
                }

                Cursor cursor = databaseHelper.getUserByEmail(email);
                if (cursor == null || cursor.getCount() == 0) {
                    if (cursor != null) cursor.close();
                    new AlertDialog.Builder(ForgotPasswordActivity.this)
                            .setTitle("Unknown email")
                            .setMessage("This email does not exist.")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
                cursor.close();

                String otpCode = generateOTP();
                long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minuta

                databaseHelper.saveOTP(email, otpCode, expirationTime);

                new SendOtpTask(email, otpCode).execute();

                Toast.makeText(ForgotPasswordActivity.this,
                        "The reset code was sent to your email.",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                i.putExtra("EMAIL", email);
                startActivity(i);
                finish();
            }
        });
    }

    private String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }

    private class SendOtpTask extends AsyncTask<Void, Void, Boolean> {

        private String recipient;
        private String otp;

        public SendOtpTask(String recipient, String otp) {
            this.recipient = recipient;
            this.otp = otp;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return OTPEmailSender.sendEmail(recipient, otp);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(ForgotPasswordActivity.this, "Sending email failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
