package com.example.wizquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

        edtNewComment = findViewById(R.id.edtNewComment);
        btnAddComment = findViewById(R.id.btnAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        dbHelper = new CommentDatabaseHelper(this);

        loadCommentsFromDB();

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCommentText = edtNewComment.getText().toString().trim();
                if (!newCommentText.isEmpty()) {
                    long insertedId = dbHelper.addComment(newCommentText);
                    if (insertedId != -1) {
                        Toast.makeText(CommentActivity.this, "New comment added!", Toast.LENGTH_SHORT).show();
                        edtNewComment.setText("");
                        loadCommentsFromDB();
                    } else {
                        Toast.makeText(CommentActivity.this, "No comment added!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Please text a comment!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Comment selectedComment = commentList.get(position);
                showUpdateDeleteDialog(selectedComment);
            }
        });
    }

    private void loadCommentsFromDB() {
        commentList = dbHelper.getAllComments();
        adapter = new CommentAdapter(this, commentList);
        listViewComments.setAdapter(adapter);
    }

    private void showUpdateDeleteDialog(final Comment comment) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comment_options, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.tvUpdateComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showUpdateCommentDialog(comment);
            }
        });

        dialogView.findViewById(R.id.tvDeleteComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteComment(comment);
            }
        });

        dialog.show();
    }

    private void showUpdateCommentDialog(final Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Comment");

        final EditText input = new EditText(this);
        input.setText(comment.getText());
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString().trim();
                if (!newText.isEmpty()) {
                    int rowsAffected = dbHelper.updateComment(comment.getId(), newText);
                    if (rowsAffected > 0) {
                        Toast.makeText(CommentActivity.this, "Updated comment!", Toast.LENGTH_SHORT).show();
                        loadCommentsFromDB();
                    } else {
                        Toast.makeText(CommentActivity.this, "Comment is not updated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Text cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Discard", null);
        builder.show();
    }

    private void deleteComment(Comment comment) {
        int rowsDeleted = dbHelper.deleteComment(comment.getId());
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Deleted comment!", Toast.LENGTH_SHORT).show();
            loadCommentsFromDB();
        } else {
            Toast.makeText(this, "Comments can not be deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}
