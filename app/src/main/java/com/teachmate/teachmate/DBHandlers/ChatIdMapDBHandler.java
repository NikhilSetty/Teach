package com.teachmate.teachmate.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.teachmate.teachmate.DBClasses.DbHelper;
import com.teachmate.teachmate.DBClasses.DbTableStrings;
import com.teachmate.teachmate.models.ChatIdMap;

/**
 * Created by ASreenivasa on 17-Jan-15.
 */
public class ChatIdMapDBHandler {

    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static void InsertChatIdMap(Context context, ChatIdMap chatIdMap) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTableStrings.CHATID, chatIdMap.chatId);
            contentValues.put(DbTableStrings.USERID, chatIdMap.userId);
            contentValues.put(DbTableStrings.USERNAME, chatIdMap.userName);

            dbHelper = new DbHelper((context.getApplicationContext()));
            db = dbHelper.getWritableDatabase();
            db.insert(DbTableStrings.TABLE_NAME_CHAT_ID_MAPPING, null, contentValues);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public static int CheckUserIdAndReturnChatId(Context context, String userID){

        try {
            dbHelper = new DbHelper(context.getApplicationContext());
            db = dbHelper.getWritableDatabase();


            Cursor c = db.rawQuery("Select * from " + DbTableStrings.TABLE_NAME_CHAT_ID_MAPPING + " where "+ DbTableStrings.USERID +" = '" + userID + "'", null);
            if (c != null ) {

                int val  = Integer.parseInt(c.getString(c.getColumnIndex(DbTableStrings.VALUE)));
                return val;
            }
            else{
                return 0;
            }

        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            return 0;
        }
    }
}
