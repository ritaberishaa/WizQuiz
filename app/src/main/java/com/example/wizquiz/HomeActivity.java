package com.example.wizquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;   // për të shkuar te WelcomeActivity
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvQuestionText;
    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestionText   = findViewById(R.id.tvQuestionText);

        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);

        // 1) Gjenerojmë pyetjet
        questionList = generateQuestions();

        // 2) Përziejmë listën dhe marrim vetëm 10, nëse ka më shumë
        Collections.shuffle(questionList, new Random());
        if (questionList.size() > 10) {
            questionList = questionList.subList(0, 10);
        }

        // 3) Ngarkojmë pyetjen e parë
        loadQuestion();

        // 4) Listener-at për opsionet
        btnOptionA.setOnClickListener(optionClickListener(0));
        btnOptionB.setOnClickListener(optionClickListener(1));
        btnOptionC.setOnClickListener(optionClickListener(2));
        btnOptionD.setOnClickListener(optionClickListener(3));
    }

    // Krijon një listë me pyetjet e mundshme (10 pyetje rreth kodimit)
    private List<Question> generateQuestions() {
        List<Question> list = new ArrayList<>();

        list.add(new Question(
                "Cila gjuhë programuese u përdor fillimisht për zhvillim Android?",
                new String[]{"Java", "Swift", "JavaScript", "Kotlin"},
                0
        ));
        list.add(new Question(
                "Cila është gjuha kryesore për zhvillim iOS?",
                new String[]{"Java", "Swift", "Kotlin", "Python"},
                1
        ));
        list.add(new Question(
                "Kush është themeluesi i Linux?",
                new String[]{"Linus Torvalds", "Bill Gates", "Steve Jobs", "John Doe"},
                0
        ));
        list.add(new Question(
                "Kush shpiku World Wide Web (WWW)?",
                new String[]{"Steve Jobs", "Mark Zuckerberg", "Tim Berners-Lee", "Elon Musk"},
                2
        ));
        list.add(new Question(
                "Cili version i HTML konsiderohet më i fundit?",
                new String[]{"HTML5", "HTML4", "HTML3", "HTML2"},
                0
        ));
        list.add(new Question(
                "Cila shtojcë përdoret zakonisht për skedarët Java?",
                new String[]{".java", ".py", ".js", ".kt"},
                0
        ));
        list.add(new Question(
                "Cili është roli i Java Virtual Machine (JVM)?",
                new String[]{"Interpreton kodin Python",
                        "Kompajlon skedarët .cpp",
                        "Ekzekuton bajtkodin e Java",
                        "Shfaq UI në Android"},
                2
        ));
        list.add(new Question(
                "Cili format është skedari i instalimit në Android?",
                new String[]{".exe", ".apk", ".zip", ".dex"},
                1
        ));
        list.add(new Question(
                "Cili framework ‘front-end’ del nga React?",
                new String[]{"Vue", "Django", "React Native", "Express"},
                2
        ));
        list.add(new Question(
                "Cili modul përdoret për menaxhimin e pakove në Node.js?",
                new String[]{"pip", "npm", "gradle", "yarn"},
                1
        ));

        return list;
    }

    // Ngarkon pyetjen e radhës në UI
    private void loadQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question q = questionList.get(currentQuestionIndex);

            // Shfaq p.sh. "01", "02", ...
            tvQuestionNumber.setText(String.format("%02d", currentQuestionIndex + 1));
            tvQuestionText.setText(q.getQuestionText());

            btnOptionA.setText("A) " + q.getOptions()[0]);
            btnOptionB.setText("B) " + q.getOptions()[1]);
            btnOptionC.setText("C) " + q.getOptions()[2]);
            btnOptionD.setText("D) " + q.getOptions()[3]);
        }
    }

    // Listener për secilin opsion
    private View.OnClickListener optionClickListener(final int selectedIndex) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(selectedIndex);
            }
        };
    }

    // Kontrollon përgjigjen dhe kalon në pyetjen tjetër
    private void checkAnswer(int selectedIndex) {
        Question currentQ = questionList.get(currentQuestionIndex);
        if (selectedIndex == currentQ.getCorrectIndex()) {
            score++;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            loadQuestion();
        } else {
            showResultDialog();
        }
    }

    // Shfaq një AlertDialog me dy butona: "Luaj Përsëri" dhe "Dil"
    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kuizi përfundoi!");
        builder.setIcon(R.drawable.ic_quiz_icon); // Sigurohu që e ke këtë ikonë në res/drawable

        String message = "Rezultati juaj: " + score + " / " + questionList.size()
                + "\n\nFaleminderit që luajtët!";
        builder.setMessage(message);

        // Butoni "Luaj Përsëri"
        builder.setPositiveButton("Luaj Përsëri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resetQuiz(); // rifillon kuizin pa dalë nga aktiviteti
            }
        });

        // Butoni "Dil"
        builder.setNegativeButton("Dil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Hap WelcomeActivity dhe mbyll HomeActivity
                startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                finish();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    // Rifillon kuizin nga e para (0 pikë, pyetje e parë, etj.)
    private void resetQuiz() {
        score = 0;
        currentQuestionIndex = 0;

        // Përziej përsëri pyetjet
        Collections.shuffle(questionList, new Random());
        loadQuestion();
    }
}
