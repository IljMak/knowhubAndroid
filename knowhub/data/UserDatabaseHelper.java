package com.example.knowhub.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Datenbank-Details
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Tabellennamen
    private static final String TABLE_USERS = "users";
    private static final String TABLE_QUIZ_RESULTS = "quiz_results";

    // Spaltennamen
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_QUIZ_TOPIC = "quiz_topic";
    private static final String COLUMN_QUESTIONS_ANSWERED = "questions_answered";
    private static final String COLUMN_CORRECT_ANSWERS = "correct_answers";
    private static final String COLUMN_DATE = "date";


    // Query zum Erstellen der Tabelle
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT UNIQUE, "
                    + COLUMN_EMAIL + " TEXT UNIQUE, "
                    + COLUMN_PASSWORD + " TEXT"
                    + ")";

    private static final String CREATE_TABLE_QUIZ_RESULTS =
            "CREATE TABLE " + TABLE_QUIZ_RESULTS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_QUIZ_TOPIC + " TEXT, "
                    + COLUMN_QUESTIONS_ANSWERED + " INTEGER, "
                    + COLUMN_CORRECT_ANSWERS + " INTEGER, "
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_QUIZ_RESULTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Bei Datenbank-Upgrade alte Tabelle löschen und neu erstellen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Methode zum Registrieren eines neuen Benutzers
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        // In einer produktiven App sollte das Passwort gehasht werden!

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Wenn result = -1, ist die Insertion fehlgeschlagen
        return result != -1;
    }

    // Methode zum Überprüfen, ob ein Benutzer existiert
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Methode zum Überprüfen, ob ein Benutzername bereits existiert
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Methode zum Überprüfen, ob eine E-Mail bereits existiert
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Method to save quiz results
    public boolean saveQuizResult(String username, String topic, int questionsAnswered, int correctAnswers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_QUIZ_TOPIC, topic);
        values.put(COLUMN_QUESTIONS_ANSWERED, questionsAnswered);
        values.put(COLUMN_CORRECT_ANSWERS, correctAnswers);

        long result = db.insert(TABLE_QUIZ_RESULTS, null, values);
        db.close();

        // Log the result for debugging
        System.out.println("Quiz result saved: " + username + ", " + topic + ", " + questionsAnswered + ", " + correctAnswers + ", result: " + result);

        return result != -1;
    }

    public Cursor getUserStats(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(TABLE_QUIZ_RESULTS,
                new String[]{COLUMN_QUIZ_TOPIC, "SUM(" + COLUMN_QUESTIONS_ANSWERED + ") as total_questions",
                        "SUM(" + COLUMN_CORRECT_ANSWERS + ") as total_correct"},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                COLUMN_QUIZ_TOPIC, null, null);
    }

    // Method to get overall leaderboard
    public Cursor getLeaderboard() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT " + COLUMN_USERNAME + ", "
                    + "SUM(" + COLUMN_QUESTIONS_ANSWERED + ") as total_questions, "
                    + "SUM(" + COLUMN_CORRECT_ANSWERS + ") as total_correct, "
                    + "ROUND((SUM(" + COLUMN_CORRECT_ANSWERS + ") * 100.0 / SUM(" + COLUMN_QUESTIONS_ANSWERED + ")), 1) as accuracy "
                    + "FROM " + TABLE_QUIZ_RESULTS + " "
                    + "GROUP BY " + COLUMN_USERNAME + " "
                    + "ORDER BY total_correct DESC";

            return db.rawQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Neue Methode für die Fortschrittsansicht eines einzelnen Benutzers
    // Remove the duplicate getUserProgress method in UserDatabaseHelper.java
// Keep only one instance of this method:
    public Cursor getUserProgress(String username) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT " + COLUMN_QUIZ_TOPIC + ", "
                    + "SUM(" + COLUMN_QUESTIONS_ANSWERED + ") as total_questions, "
                    + "SUM(" + COLUMN_CORRECT_ANSWERS + ") as total_correct, "
                    + "ROUND((SUM(" + COLUMN_CORRECT_ANSWERS + ") * 100.0 / SUM(" + COLUMN_QUESTIONS_ANSWERED + ")), 1) as accuracy "
                    + "FROM " + TABLE_QUIZ_RESULTS + " "
                    + "WHERE " + COLUMN_USERNAME + " = ? "
                    + "GROUP BY " + COLUMN_QUIZ_TOPIC + " "
                    + "ORDER BY " + COLUMN_QUIZ_TOPIC;

            return db.rawQuery(query, new String[]{username});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    }
