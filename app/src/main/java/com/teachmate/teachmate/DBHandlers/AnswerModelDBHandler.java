package com.teachmate.teachmate.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.teachmate.teachmate.DBClasses.DbHelper;
import com.teachmate.teachmate.DBClasses.DbTableStrings;
import com.teachmate.teachmate.models.Answer_Model;

/**
 * Created by ASreenivasa on 05-Jan-15.
 */
public class AnswerModelDBHandler {

    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static void InsertAnswerModel(Context context, Answer_Model answer_model){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTableStrings.ACTUAL_ANSWER, answer_model.actualanswer);
            contentValues.put(DbTableStrings.ANSWER_ID, answer_model.answer_id);
            contentValues.put(DbTableStrings.ANSWERED_BY, answer_model.answeredby);
            contentValues.put(DbTableStrings.ANSWERED_BY, answer_model.answeredtime);
            contentValues.put(DbTableStrings.QUESTION_ID, answer_model.question_id);

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
