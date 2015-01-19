package com.teachmate.teachmate;

import com.teachmate.teachmate.DBHandlers.UserModelDBHandler;
import com.teachmate.teachmate.models.UserModel;
import com.teachmate.teachmate.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends Activity {

    EditText editTextEmailId;
    EditText editTextPassword;

    String _editTextEmail = "";
    String _editTextPassword = "";

    ProgressDialog progressDialog;

    String serverRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editTextEmailId = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextLoginPassword);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    public void SignUpAction(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void AuthenticateUser(View v){

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        _editTextEmail = editTextEmailId.getText().toString();
        if(_editTextEmail.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
            return;
        }

        _editTextPassword = editTextPassword.getText().toString();
        if(_editTextPassword.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Enter Password Id", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpAuthenticateUser authenticateUser = new HttpAuthenticateUser();
        authenticateUser.execute("http://teach-mate.azurewebsites.net/User/CheckUser");

    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EmailId", _editTextEmail);
            jsonObject.put("Password", _editTextPassword);
            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            return result;

        } catch (Exception e) {
            Log.v("Getter", e.getLocalizedMessage());
        }

        return result;
    }
    private class HttpAuthenticateUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent! -" + result.toString(), Toast.LENGTH_LONG).show();
            convertJsonToObject(result);
            if(!TempDataClass.deviceRegId.equals(serverRegId)){
                HttpPostRegIdToServer regIdPost = new HttpPostRegIdToServer();
                regIdPost.execute("http://teach-mate.azurewebsites.net/User/UpdateRegId");
            }
            else if(TempDataClass.deviceRegId.equals(serverRegId)){
                progressDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }
    }

    private void convertJsonToObject(String result) {
        try {
            String status = (new JSONObject(result)).get("Status").toString();

            if(status.equals("Success")){
                JSONObject userDataJson = (new JSONObject(result)).getJSONObject("UserDetails");

                UserModel userData = new UserModel();
                String[] temp = new String[2];
                int i = 0;
                for (String retval: userDataJson.getString("Name").split(" ", 2)){
                    if(i == 0)
                        temp[0] = retval;
                    else
                        temp[1] = retval;

                    i++;
                }
                userData.FirstName = temp[0];
                userData.LastName = temp[1];
                userData.ServerUserId = userDataJson.get("Id").toString();
                TempDataClass.serverUserId = userData.ServerUserId;
                userData.PhoneNumber = userDataJson.get("PhoneNumber").toString();
                userData.Profession = userDataJson.get("Profession").toString();
                userData.EmailId = userDataJson.get("EmailId").toString();
                userData.Address1 = userDataJson.get("Address1").toString();
                userData.PinCode1 = userDataJson.get("PinCode1").toString();
                userData.Address2 = userDataJson.get("Address2").toString();
                userData.PinCode2 = userDataJson.get("Pincode2").toString();

                UserModelDBHandler.InsertProfile(getApplicationContext(), userData);

                serverRegId = userDataJson.get("RegistrationId").toString();
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Wrong EmailId or Password. Try Again", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public String POSTRegID(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", TempDataClass.serverUserId);
            jsonObject.put("RegistrationId", TempDataClass.deviceRegId);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            return result;

        } catch (Exception e) {
            Log.v("Getter", e.getLocalizedMessage());
        }

        return result;
    }
    private class HttpPostRegIdToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POSTRegID(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("OK")){
                Toast.makeText(getApplicationContext(), "Registration Successfull.", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        }
    }

}
