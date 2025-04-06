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

import java.util.Random;

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

                // Kontrollojmë nëse ekziston user-i
                Cursor cursor = databaseHelper.getUserByEmail(email);
                if (cursor == null || cursor.getCount() == 0) {
                    if (cursor != null) cursor.close();
                    new AlertDialog.Builder(ForgotPasswordActivity.this)
                            .setTitle("Email i panjohur")
                            .setMessage("Ky email nuk ekziston në databazë!")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
                cursor.close();

                // Gjenerojmë OTP
                String otpCode = generateRandomCode(6);
                long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minuta

                // Ruajmë OTP në DB
                databaseHelper.saveOTP(email, otpCode, expirationTime);

                // Dërgojmë email me kodin OTP (real)
                sendEmailWithCode(email, otpCode);

                Toast.makeText(ForgotPasswordActivity.this,
                        "Kodi i rivendosjes u dërgua në email.",
                        Toast.LENGTH_SHORT).show();

                // Kalojmë në VerifyCodeActivity
                Intent i = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                i.putExtra("EMAIL", email);
                startActivity(i);
                finish();
            }
        });
    }

    // Gjeneron një varg me gjatësi të caktuar (p.sh. 6 shifra)
    private String generateRandomCode(int length) {
        String digits = "0123456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(digits.charAt(r.nextInt(digits.length())));
        }
        return sb.toString();
    }

    // Metoda që dërgon email me kodin OTP duke përdorur JavaMail në një AsyncTask
    private void sendEmailWithCode(String recipientEmail, String code) {
        // Vendos këtu kredencialet e llogarisë tënde Gmail
        final String senderEmail = "yourgmail@gmail.com";
        final String senderPassword = "yourapppassword"; // përdor një app password nëse ke 2FA të aktivizuar

        final String subject = "Your OTP Code";
        final String body = "Your OTP code is: " + code;

        new SendMailTask().execute(senderEmail, senderPassword, recipientEmail, subject, body);
    }

    // AsyncTask për dërgimin e email-it në sfond
    private class SendMailTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String senderEmail = params[0];
                String senderPassword = params[1];
                String recipientEmail = params[2];
                String subject = params[3];
                String body = params[4];

                GMailSender sender = new GMailSender(senderEmail, senderPassword);
                sender.sendMail(subject, body, senderEmail, recipientEmail);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Failed to send email.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
