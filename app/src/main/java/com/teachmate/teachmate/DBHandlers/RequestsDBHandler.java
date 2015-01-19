package com.teachmate.teachmate.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.teachmate.teachmate.DBClasses.DbHelper;
import com.teachmate.teachmate.DBClasses.DbTableStrings;
import com.teachmate.teachmate.models.Requests;

/**
 * Created by ASreenivasa on 05-Jan-15.
 */
public class RequestsDBHandler {

    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static void InsertRequests(Context context,Requests requests){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTableStrings.REQUEST_ID, requests.RequestID);
            contentValues.put(DbTableStrings.REQUEST_EUSER_ID, requests.RequesteUserId);
            contentValues.put(DbTableStrings.REQUEST_USERNAME, requests.RequestUserName);
            contentValues.put(DbTableStrings.REQUEST_STRING, requests.RequestString);
            contentValues.put(DbTableStrings.REQUEST_USER_PROFESSION, requests.RequestUserProfession);
            contentValues.put(DbTableStrings.REQUEST_USER_PROFILE_PHOTO_SERVER_PATH, requests.RequestUserProfilePhotoServerPath);
            contentValues.put(DbTableStrings.REQUEST_TIME,requests.RequestTime);

            dbHelper = new DbHelper((context.getApplicationContext()));
            db = dbHelper.getWritableDatabase();
            db.insert(DbTableStrings.TABLE_NAME_REQUESTS,null,contentValues);
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Requests[] GetAllRequests(Context context)
    {
        dbHelper = new DbHelper(context.getApplicationContext());
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("Select * from " + DbTableStrings.TABLE_NAME_REQUESTS, null);

        Requests[] requests = new Requests[c.getCount()];

        if (c != null ) {
            int i = 0;
            if  (c.moveToFirst()) {
                do {
                    requests[i].RequesteUserId = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_EUSER_ID));
                    requests[i].RequestID = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_ID));
                    requests[i].RequestString = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_STRING));
                    requests[i].RequestTime = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_TIME));
                    requests[i].RequestUserName = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_USERNAME));
                    requests[i].RequestUserProfession = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_USER_PROFESSION));
                    requests[i].RequestUserProfilePhotoServerPath = c.getString(c.getColumnIndex(DbTableStrings.REQUEST_USER_PROFILE_PHOTO_SERVER_PATH));
                    i++;
                }while (c.moveToNext());
            }
            return requests;
        }
        return null;
    }
}
