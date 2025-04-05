package com.example.wizquiz;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Këtu mund të ngarkosh pyetjet e quiz‑it, të shfaqësh mesazhe mirëseardhjeje, etj.
        // Për shembull, nëse dëshiron të dërgosh një notifikim:
        NotificationHelper notificationHelper = new NotificationHelper(this);
        NotificationCompat.Builder nb = notificationHelper.getNotification("Mirësevini", "Faleminderit që përdor WizQuiz!");
        notificationHelper.getManager().notify(1, nb.build());
    }
}
