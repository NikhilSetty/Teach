package com.teachmate.teachmate.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.teachmate.teachmate.DBClasses.DbHelper;
import com.teachmate.teachmate.DBClasses.DbTableStrings;
import com.teachmate.teachmate.models.UserModel;

/**
 * Created by ASreenivasa on 05-Jan-15.
 */
public class UserModelDBHandler {


    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static void InsertProfile(Context context,UserModel userModel){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTableStrings.SERVERUSERID,userModel.ServerUserId);
            contentValues.put(DbTableStrings.FNAME,userModel.FirstName);
            contentValues.put(DbTableStrings.LNAME,userModel.LastName);
            contentValues.put(DbTableStrings.PHONENUMBER,userModel.PhoneNumber);
            contentValues.put(DbTableStrings.EMAILID,userModel.EmailId);
            contentValues.put(DbTableStrings.PROFESSION,userModel.Profession);
            contentValues.put(DbTableStrings.ADDRESS1,userModel.Address1);
            contentValues.put(DbTableStrings.PINCODE1,userModel.PinCode1);
            contentValues.put(DbTableStrings.ADDRESS2,userModel.Address2);
            contentValues.put(DbTableStrings.PINCODE2,userModel.PinCode2);

            dbHelper = new DbHelper((context.getApplicationContext()));
            db = dbHelper.getWritableDatabase();
            db.insert(DbTableStrings.TABLE_NAME_USER_MODEL,null,contentValues);
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean CheckIfUserDataExists(Context context){

        try {
            dbHelper = new DbHelper(context.getApplicationContext());
            db = dbHelper.getWritableDatabase();

            UserModel contact = new UserModel();

            Cursor c = db.rawQuery("Select * from " + DbTableStrings.TABLE_NAME_USER_MODEL, null);
            if (c.moveToFirst()) {
                return true;
            }
            else{
                return false;
            }

        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

