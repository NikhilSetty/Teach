package com.teachmate.teachmate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NiRavishankar on 1/21/2015.
 */
public class CommonMethods {

    static boolean isFinished = false;
    static boolean isAvailable = false;

    public static boolean hasActiveInternetConnection(Context context) {
        HttpGetter getter = new HttpGetter();
        getter.execute(context);

        while(!isFinished){
        }

        if(isAvailable){
            return true;
        }
        else{
            return false;
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private static class HttpGetter extends AsyncTask<Context, Void, String> {

        @Override
        protected String doInBackground(Context... context) {
            if (isNetworkAvailable(context[0])) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    if(urlc.getResponseCode() == 200)
                        return "true";
                } catch (IOException e) {
                    Log.e("INTERNET", "Error checking internet connection", e);
                }
            } else {
                Log.d("INTERNET", "No network available!");
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("true")){
                isAvailable = true;
            }
            else{
                isAvailable = false;
            }
            isFinished = true;
        }
    }
}
