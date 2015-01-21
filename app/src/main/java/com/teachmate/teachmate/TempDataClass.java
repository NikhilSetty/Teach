package com.teachmate.teachmate;

/**
 * Created by NiRavishankar on 1/19/2015.
 */

import android.support.v4.app.Fragment;

import java.util.Stack;

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

    public static Stack<Fragment> fragmentStack = new Stack<Fragment>();

    public static boolean isThroughSplash = false;
}