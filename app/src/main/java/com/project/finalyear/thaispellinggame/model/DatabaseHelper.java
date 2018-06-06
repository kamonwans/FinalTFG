package com.project.finalyear.thaispellinggame.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Namwan on 2/25/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "TFGdatabase.db";
    private static String DB_PATH = "/data/data/com.project.finalyear.thaispellinggame/databases/";
    private SQLiteDatabase mDatabase;
    private Context mContext = null;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        //DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
    }

    public void openDatabase() {
        String myPath = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void deleteDB(Context context){
        context.deleteDatabase(DB_NAME);
    }

    public void copyDatabase() throws IOException {
        try {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
                myOutput.write(buffer, 0, length);

            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (tempDB != null)
            tempDB.close();
        return tempDB != null ? true : false;
    }

    public void createDatabase() throws IOException {
        boolean isDatabaseExist = checkDatabase();
        if (!isDatabaseExist) { //(!isDatabaseExist) ถ้าไม่มี DB ให้เรียกเมธอด copyDatabase()

            this.getReadableDatabase();
            try {

                copyDatabase();
                Toast.makeText(mContext, "DB created!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //add new column
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE LearningQuiz ADD COLUMN image TEXT");
            //db.execSQL("CREATE TABLE LearningQuiz(id INTEGER primary key autoincrement, question TEXT, choiceA TEXT, choiceB TEXT, choiceC TEXT, answer TEXT);");
        }
    }

    //CRUD For Table
    public List<GameLearnModel> getListGameLearn() {
        List<GameLearnModel> gameLearnList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = db.rawQuery("SELECT * FROM LearningGame", null);
            if (cursor == null) return null;
            cursor.moveToFirst();

            do {

                String word = cursor.getString(cursor.getColumnIndex("word"));
                String type = cursor.getString(cursor.getColumnIndex("type"));

                GameLearnModel gameLearnModel = new GameLearnModel(word, type);
                gameLearnList.add(gameLearnModel);

            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return gameLearnList;
    }

    // Quiz List
    public List<LearningQuizModel> getListQuiz() {
        List<LearningQuizModel> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = db.rawQuery("SELECT * FROM LearningQuiz", null);
            if (cursor == null) return null;
            cursor.moveToFirst();

            do {

                String question = cursor.getString(cursor.getColumnIndex("question"));
                String choiceA = cursor.getString(cursor.getColumnIndex("choiceA"));
                String choiceB = cursor.getString(cursor.getColumnIndex("choiceB"));
                String choiceC = cursor.getString(cursor.getColumnIndex("choiceC"));
                String answer = cursor.getString(cursor.getColumnIndex("answer"));
                String image = cursor.getString(cursor.getColumnIndex("image"));

                LearningQuizModel quizModel = new LearningQuizModel(question, choiceA, choiceB, choiceC, answer, image);
                quizList.add(quizModel);

            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return quizList;
    }
}
