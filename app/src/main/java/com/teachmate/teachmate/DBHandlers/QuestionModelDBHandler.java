package com.teachmate.teachmate.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.teachmate.teachmate.DBClasses.DbHelper;
import com.teachmate.teachmate.DBClasses.DbTableStrings;
import com.teachmate.teachmate.models.Question_Model;

/**
 * Created by ASreenivasa on 05-Jan-15.
 */
public class QuestionModelDBHandler {

    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static void InsertQuestionModel(Context context, Question_Model question_model){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTableStrings.USERNAME, question_model.username);
            contentValues.put(DbTableStrings.QUESTION, question_model.question);
            contentValues.put(DbTableStrings.IMAGE, question_model.image);
            contentValues.put(DbTableStrings.QUESTION_ID, question_model.question_id);
            contentValues.put(DbTableStrings.CATEGORY, question_model.Category);
            contentValues.put(DbTableStrings.ASKED_TIME, question_model.asked_time);

            dbHelper = new DbHelper((context.getApplicationContext()));
            db = dbHelper.getWritableDatabase();
            db.insert("",null,contentValues);
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
