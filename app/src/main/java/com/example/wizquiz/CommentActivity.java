package com.example.wizquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private EditText edtNewComment;
    private Button btnAddComment;
    private ListView listViewComments;

    private ArrayAdapter<String> adapter;
    private ArrayList<Comment> commentList;      // Mban objektet e komenteve
    private ArrayList<String> commentTexts;      // Mban tekstet për shfaqje në ListView

    private CommentDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        edtNewComment    = findViewById(R.id.edtNewComment);
        btnAddComment    = findViewById(R.id.btnAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        dbHelper = new CommentDatabaseHelper(this);

        // 1) Merr komentet ekzistuese nga DB
        loadCommentsFromDB();

        // 2) Kur shtypet "Shto Koment"
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                String newCommentText = edtNewComment.getText().toString().trim();
                if (!newCommentText.isEmpty()) {
                    // Shto komentin në DB
                    long insertedId = dbHelper.addComment(newCommentText);
                    if (insertedId != -1) {
                        Toast.makeText(CommentActivity.this, "Koment i shtuar!", Toast.LENGTH_SHORT).show();
                        edtNewComment.setText("");
                        loadCommentsFromDB();  // Rifresko listën
                    } else {
                        Toast.makeText(CommentActivity.this, "Nuk u shtua komenti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Ju lutem shkruani një koment!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3) Kur klikohet një artikull në listë – ofrojmë mundësinë e UPDATE/FDELETE
        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Comment selectedComment = commentList.get(position);
                // Dialog me opsion UPDATE ose DELETE
                showUpdateDeleteDialog(selectedComment);
            }
        });
    }

    private void loadCommentsFromDB() {
        // Merr të gjitha komentet
        commentList = dbHelper.getAllComments();
        commentTexts = new ArrayList<>();
        for (Comment c : commentList) {
            commentTexts.add("ID: " + c.getId() + " - " + c.getText());
        }

        // Krijo adapterin për ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, commentTexts);
        listViewComments.setAdapter(adapter);
    }

    private void showUpdateDeleteDialog(final Comment comment) {
        // Krijojmë një dialog me opsionet UPDATE ose DELETE
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opsionet për komentin");

        // Përmbajtja e dialogut - array me 2 opsione
        String[] options = {"Përditëso Koment", "Fshi Koment"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    // UPDATE
                    showUpdateCommentDialog(comment);
                } else if (which == 1) {
                    // DELETE
                    deleteComment(comment);
                }
            }
        });

        builder.create().show();
    }

    private void showUpdateCommentDialog(final Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Përditëso Koment");

        // Vendosim një EditText për të futur tekstin e ri
        final EditText input = new EditText(this);
        input.setText(comment.getText()); // Teksti ekzistues
        builder.setView(input);

        // Butonat
        builder.setPositiveButton("Ruaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString().trim();
                if (!newText.isEmpty()) {
                    int rows = dbHelper.updateComment(comment.getId(), newText);
                    if (rows > 0) {
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

        builder.create().show();
    }

    private void deleteComment(Comment comment) {
        int rows = dbHelper.deleteComment(comment.getId());
        if (rows > 0) {
            Toast.makeText(this, "Koment i fshirë!", Toast.LENGTH_SHORT).show();
            loadCommentsFromDB();
        } else {
            Toast.makeText(this, "Nuk u fshi komenti!", Toast.LENGTH_SHORT).show();
        }
    }
}
