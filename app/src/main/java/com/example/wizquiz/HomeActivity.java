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


        questionList = generateQuestions();

        Collections.shuffle(questionList, new Random());
        if (questionList.size() > 10) {
            questionList = questionList.subList(0, 10);
        }


        loadQuestion();


        btnOptionA.setOnClickListener(optionClickListener(0));
        btnOptionB.setOnClickListener(optionClickListener(1));
        btnOptionC.setOnClickListener(optionClickListener(2));
        btnOptionD.setOnClickListener(optionClickListener(3));
    }

    private List<Question> generateQuestions() {
        List<Question> list = new ArrayList<>();

        // 1. Which programming language is mostly used for Android development?
        list.add(new Question(
                "Which programming language is mostly used for Android development?",
                new String[]{"Java", "C#", "Python", "Ruby"},
                0
        ));

        // 2. What file extension is used for Java source files?
        list.add(new Question(
                "What file extension is used for Java source files?",
                new String[]{".java", ".kt", ".cpp", ".js"},
                0
        ));

        // 3. What is Android?
        list.add(new Question(
                "What is Android?",
                new String[]{"A mobile operating system", "A programming language", "A web browser", "A computer brand"},
                0
        ));

        // 4. Which file format is used to install Android apps?
        list.add(new Question(
                "Which file format is used to install Android apps?",
                new String[]{".apk", ".exe", ".zip", ".jar"},
                0
        ));

        // 5. Which Integrated Development Environment (IDE) is popular for Android development?
        list.add(new Question(
                "Which Integrated Development Environment (IDE) is popular for Android development?",
                new String[]{"Android Studio", "Xcode", "Visual Studio", "NetBeans"},
                0
        ));

        // 6. What does JVM stand for in Java development?
        list.add(new Question(
                "What does JVM stand for in Java development?",
                new String[]{"Java Virtual Machine", "Java Visual Machine", "Just Virtual Memory", "Joint Variable Module"},
                0
        ));

        // 7. What is the main purpose of Android Studio?
        list.add(new Question(
                "What is the main purpose of Android Studio?",
                new String[]{"To develop Android applications", "To design websites", "To manage databases", "To compile C++ code"},
                0
        ));

        // 8. Which of the following is not a Java keyword?
        list.add(new Question(
                "Which of the following is not a Java keyword?",
                new String[]{"class", "interface", "define", "public"},
                2
        ));

        // 9. What does debugging mean in programming?
        list.add(new Question(
                "What does debugging mean in programming?",
                new String[]{"Fixing errors in code", "Writing the code", "Compiling code", "Adding comments"},
                0
        ));

        // 10. Which of these is a common widget used in Android user interfaces?
        list.add(new Question(
                "Which of these is a common widget used in Android user interfaces?",
                new String[]{"Button", "Sheet", "Label", "Slider"},
                0
        ));

        return list;
    }


    private void loadQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question q = questionList.get(currentQuestionIndex);

            tvQuestionNumber.setText(String.format("%02d", currentQuestionIndex + 1));
            tvQuestionText.setText(q.getQuestionText());

            btnOptionA.setText("A) " + q.getOptions()[0]);
            btnOptionB.setText("B) " + q.getOptions()[1]);
            btnOptionC.setText("C) " + q.getOptions()[2]);
            btnOptionD.setText("D) " + q.getOptions()[3]);
        }
    }

    private View.OnClickListener optionClickListener(final int selectedIndex) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(selectedIndex);
            }
        };
    }

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


    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The quiz is over!");
        builder.setIcon(R.drawable.ic_quiz_icon); // Sigurohu që e ke këtë ikonë në res/drawable

        String message = "Your result: " + score + " / " + questionList.size()
                + "\n\nThank you for playing.";
        builder.setMessage(message);

        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resetQuiz();
            }
        });

        // Butoni "Dil"
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                finish();
            }
        });

     //CRUD
        builder.setNeutralButton("Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(HomeActivity.this, CommentActivity.class));
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    private void resetQuiz() {
        score = 0;
        currentQuestionIndex = 0;

        Collections.shuffle(questionList, new Random());
        loadQuestion();
    }
}
