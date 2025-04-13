package com.example.wizquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.mindrot.jbcrypt.BCrypt;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText etNewPassword, etConfirmPassword;
    private MaterialButton btnSavePassword;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);
        databaseHelper = new DatabaseHelper(this);
        email = getIntent().getStringExtra("EMAIL");

        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = etNewPassword.getText().toString().trim();
                String pass2 = etConfirmPassword.getText().toString().trim();

                if (pass1.isEmpty() || pass2.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass1.equals(pass2)) {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String hashed = BCrypt.hashpw(pass1, BCrypt.gensalt());
                databaseHelper.updateUserPassword(email, hashed);
                Toast.makeText(ResetPasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
