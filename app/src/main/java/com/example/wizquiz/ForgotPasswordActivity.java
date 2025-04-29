package com.example.wizquiz;
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

//rrjedha:
// 1. perdoruesi fut emailin dhe klikon submit.
// 2. sistemi verifikon ekzistencen e emailit → gjeneron dhe ruan OTP-n 5-minuteshe.
// 3. OTPja dergohet me email ne sfond, perdoruesi kalon ne VerifyCodeActivity per
// ta futur kodin dhe me pas mund ta rivendos fjalekalimin.
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
                // lexohet dhe kontrollohet email nese eshte bosh
                String email = etEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                    return;
                }

                //kerkon perdoruesin ne db, nese nuk ka shfaq nje alert..
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

                String otpCode = generateOTP();  // gjeneron otp kodin
                long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minuta - koha e skadimit te kodit

                databaseHelper.saveOTP(email, otpCode, expirationTime); // ruajtja ne databaze

                new SendOtpTask(email, otpCode).execute(); // klase qe sherben per me dergu emailin bashk me otp te perdoruesi

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
        int otp = secureRandom.nextInt(900000) + 100000; // gjenerimi i nje numri 6 shifror prej 100000 deri 900000
        return String.valueOf(otp);
    }

    private class SendOtpTask extends AsyncTask<Void, Void, Boolean> {

        private String recipient;
        private String otp;


        // konstruktori qe merr dmth emailin dhe otp qe dergohet
        // ketu degohet kodi ne email ne prapaskenë pa blloku aplikacioni
        public SendOtpTask(String recipient, String otp) {
            this.recipient = recipient;
            this.otp = otp;
        }

        // nese dergimi eshte i sukseshem
        @Override
        protected Boolean doInBackground(Void... voids) {
            return OTPEmailSender.sendEmail(recipient, otp);
        }

        // nese deshton
        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(ForgotPasswordActivity.this, "Sending email failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
