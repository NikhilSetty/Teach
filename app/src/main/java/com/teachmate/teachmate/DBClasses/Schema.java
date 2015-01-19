package com.teachmate.teachmate.DBClasses;

public class Schema {
    public static final String CREATE_TABLE_Profile = "create table if not exists " + DbTableStrings.TABLE_NAME_USER_MODEL +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.SERVERUSERID + " string, "
            + DbTableStrings.FNAME + " string, "
            + DbTableStrings.LNAME + " string, "
            + DbTableStrings.PHONENUMBER + " string, "
            + DbTableStrings.EMAILID + " string, "
            + DbTableStrings.PROFESSION + " string, "
            + DbTableStrings.ADDRESS1 + " string, "
            + DbTableStrings.PINCODE1 + " string, "
            + DbTableStrings.ADDRESS2 + " string, "
            + DbTableStrings.PINCODE2 + " string) ";

    public static final String CREATE_TABLE_REQUESTS = "create table if not exists " + DbTableStrings.TABLE_NAME_REQUESTS +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.REQUEST_ID + " string, "
            + DbTableStrings.REQUEST_EUSER_ID + " string, "
            + DbTableStrings.REQUEST_USERNAME + " string, "
            + DbTableStrings.REQUEST_STRING + " string, "
            + DbTableStrings.REQUEST_TIME + " string, "
            + DbTableStrings.REQUEST_USER_PROFESSION + " string, "
            + DbTableStrings.REQUEST_USER_PROFILE_PHOTO_SERVER_PATH + " string) ";

    public static final String CREATE_TABLE_QUESTION_MODEL = "create table if not exists " + DbTableStrings.TABLE_NAME_QUESTION_MODEL +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.USERNAME + " string, "
            + DbTableStrings.QUESTION + " string, "
            + DbTableStrings.IMAGE + " string, "
            + DbTableStrings.QUESTION_ID + " string, "
            + DbTableStrings.CATEGORY + " string, "
            + DbTableStrings.ASKED_TIME + " string) ";

    public static final String CREATE_TABLE_ANSWER_MODEL = "create table if not exists " + DbTableStrings.TABLE_NAME_ANSWER_MODEL +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.ACTUAL_ANSWER + " string, "
            + DbTableStrings.ANSWERED_BY + " string, "
            + DbTableStrings.ANSWERED_TIME + " string, "
            + DbTableStrings.ANSWER_ID + " string, "
            + DbTableStrings.QUESTION_ID + " string) ";

    public static final String CREATE_TABLE_DEVICE_INFO = "create table if not exists " + DbTableStrings.TABLE_NAME_DEVICE_INFO +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.KEY + " string, "
            + DbTableStrings.VALUE+ " string) ";
}


