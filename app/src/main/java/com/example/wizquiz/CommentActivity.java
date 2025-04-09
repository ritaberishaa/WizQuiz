package com.example.wizquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private EditText edtNewComment;
    private Button btnAddComment;
    private ListView listViewComments;

    private ArrayList<Comment> commentList;
    private CommentDatabaseHelper dbHelper;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Inicializo elementët e UI-së
        edtNewComment = findViewById(R.id.edtNewComment);
        btnAddComment = findViewById(R.id.btnAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        // Inicializo database helper për komentet
        dbHelper = new CommentDatabaseHelper(this);

        // Ngarko komentet nga DB
        loadCommentsFromDB();

        // Butoni "SHTO" për të shtuar një koment të ri
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCommentText = edtNewComment.getText().toString().trim();
                if (!newCommentText.isEmpty()) {
                    long insertedId = dbHelper.addComment(newCommentText);
                    if (insertedId != -1) {
                        Toast.makeText(CommentActivity.this, "Koment i shtuar!", Toast.LENGTH_SHORT).show();
                        edtNewComment.setText("");
                        loadCommentsFromDB();  // Rifresko listën e komenteve
                    } else {
                        Toast.makeText(CommentActivity.this, "Nuk u shtua komenti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Ju lutem shkruani një koment!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Kur klikohet në një element të ListView, ofro opsionet për UPDATE ose DELETE
        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Comment selectedComment = commentList.get(position);
                showUpdateDeleteDialog(selectedComment);
            }
        });
    }

    // Ngarkon komentet nga databaza dhe rifreskon ListView-në
    private void loadCommentsFromDB() {
        commentList = dbHelper.getAllComments();
        adapter = new CommentAdapter(this, commentList);
        listViewComments.setAdapter(adapter);
    }

    // Dialog me opsionet për përditësim ose fshirje të një komenti
    private void showUpdateDeleteDialog(final Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opsionet për komentin");

        String[] options = {"Përditëso Koment", "Fshi Koment"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showUpdateCommentDialog(comment);
                } else if (which == 1) {
                    deleteComment(comment);
                }
            }
        });
        builder.show();
    }

    // Dialog për përditësimin e një komenti
    private void showUpdateCommentDialog(final Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Përditëso Koment");

        final EditText input = new EditText(this);
        input.setText(comment.getText());
        builder.setView(input);

        builder.setPositiveButton("Ruaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString().trim();
                if (!newText.isEmpty()) {
                    int rowsAffected = dbHelper.updateComment(comment.getId(), newText);
                    if (rowsAffected > 0) {
                        Toast.makeText(CommentActivity.this, "Koment i përditësuar!", Toast.LENGTH_SHORT).show();
                        loadCommentsFromDB();
                    } else {
                        Toast.makeText(CommentActivity.this, "Nuk u përditësua komenti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Teksti nuk mund të jetë bosh!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Anulo", null);
        builder.show();
    }

    // Fshin një koment nga databaza
    private void deleteComment(Comment comment) {
        int rowsDeleted = dbHelper.deleteComment(comment.getId());
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Koment i fshirë!", Toast.LENGTH_SHORT).show();
            loadCommentsFromDB();
        } else {
            Toast.makeText(this, "Nuk u fshi komenti!", Toast.LENGTH_SHORT).show();
        }
    }
}
