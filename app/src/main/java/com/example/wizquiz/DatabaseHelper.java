package com.example.wizquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// import android.database.sqlite.SQLiteOpenHelper; // e përdorim për ndërtim DB

// Shembull i thjeshtuar i një SQLiteOpenHelper (ose mund të kesh tjetër strukturë)
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WizQuizDB.db";
    private static final int DATABASE_VERSION = 1;

    // Emrat e tabelës së user-ëve
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Tabela OTP
    private static final String TABLE_OTP = "otp_data";
    private static final String COLUMN_OTP_EMAIL = "otp_email";
    private static final String COLUMN_OTP_CODE = "otp_code";
    private static final String COLUMN_OTP_EXPIRY = "otp_expiry";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Krijojmë tabelën e user-ëve
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Krijojmë tabelën e OTP
        String CREATE_OTP_TABLE = "CREATE TABLE " + TABLE_OTP + "("
                + COLUMN_OTP_EMAIL + " TEXT, "
                + COLUMN_OTP_CODE + " TEXT, "
                + COLUMN_OTP_EXPIRY + " LONG "
                + ")";
        db.execSQL(CREATE_OTP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Rimodelim - fshi tabelat e vjetra e i rikrijo
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTP);
        onCreate(db);
    }

    // Kthen cursor me rreshtin e user-it për email-in e dhënë (ose bosh/nul nëse s’ka)
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{email});
    }

    // Ruaj OTP në DB (nëse ekziston rresht, e fshin dhe e shton përsëri)
    public void saveOTP(String email, String code, long expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Së pari, fshijmë çdo rresht ekzistues për atë email
        db.delete(TABLE_OTP, COLUMN_OTP_EMAIL + "=?", new String[]{email});

        // Pastaj shtojmë rresht të ri
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_OTP_EMAIL, email);
        cv.put(COLUMN_OTP_CODE, code);
        cv.put(COLUMN_OTP_EXPIRY, expiry);

        db.insert(TABLE_OTP, null, cv);
        db.close();
    }

    // Merr info OTP për email-in
    public OTPData getOTPInfo(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OTP + " WHERE " + COLUMN_OTP_EMAIL + " = ?";
        Cursor c = db.rawQuery(query, new String[]{email});
        if (c != null && c.moveToFirst()) {
            String code = c.getString(c.getColumnIndexOrThrow(COLUMN_OTP_CODE));
            long exp = c.getLong(c.getColumnIndexOrThrow(COLUMN_OTP_EXPIRY));
            c.close();
            db.close();
            return new OTPData(code, exp);
        }
        if (c != null) c.close();
        db.close();
        return null;
    }

    // Përditëso password për user-in me email të caktuar
    public void updateUserPassword(String email, String hashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, hashedPassword);
        db.update(TABLE_USERS, cv, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
    }

    // (Opsionale) Shtojë user-in. E përdor SignUpActivity
    public void addUser(String email, String hashedPassword, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASSWORD, hashedPassword);
        // name, phone s’janë kolona në këtë strukturë të thjeshtë, do duhej t’i shtoje
        db.insert(TABLE_USERS, null, cv);
        db.close();
    }
}
