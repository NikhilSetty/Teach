package com.teachmate.teachmate;

/**
 * Created by NiRavishankar on 1/19/2015.
 */

/**
 * The purpose of this class is to avoid DB hits all the time in the app.
 */
public class TempDataClass {

    public static String deviceRegId;

    public static String serverUserId;
    public static String userProfession;
    public static String userName;

    public static String currentLattitude;
    public static String currentLongitude;

    public static boolean isThroughSplash = false;
}
