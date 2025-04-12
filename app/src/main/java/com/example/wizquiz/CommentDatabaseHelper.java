package com.example.wizquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
class CommentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wizquiz_comments.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXT = "text";

    public CommentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Krijo tabelën 'comments'
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMENTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEXT + " TEXT)";
        db.execSQL(CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Në rast ndryshimi versioni, mund të fshihet tabela e vjetër dhe të rikrijohet
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    public long addComment(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        long insertedId = db.insert(TABLE_COMMENTS, null, values);
        db.close();
        return insertedId;
    }

    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMMENTS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
                list.add(new Comment(id, text));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public int updateComment(int id, String newText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, newText);
        int rowsAffected = db.update(TABLE_COMMENTS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteComment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_COMMENTS, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}
