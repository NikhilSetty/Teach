package com.teachmate.teachmate.Responses;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teachmate.teachmate.R;
import com.teachmate.teachmate.models.Requests;
import com.teachmate.teachmate.models.Responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResponseDisplayActivity extends Fragment {

    Responses currentResponse;
    Requests currentRequest;

    Button acceptResponse;

    String notificationRequestId;

    TextView requestString;

    TextView responseUserName;
    TextView responseUserProfession;
    TextView responseString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = (FragmentActivity) super.getActivity();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_response_display, container, false);

        acceptResponse = (Button) layout.findViewById(R.id.buttonAccept);

        try {
            Bundle args = getArguments();
            notificationRequestId = args.getString("NotificationResponseId");
        }catch(Exception e){
            Log.e("Error", e.getMessage());
        }
        if(notificationRequestId == null){
            currentRequest = new Requests();

            Bundle args = getArguments();

            currentRequest.RequestID = args.getString("RequestID");
            currentRequest.RequestString = args.getString("RequestString");
            currentRequest.RequestTime = args.getString("RequestTime");

            requestString = (TextView) layout.findViewById(R.id.textViewResponseRequestString);
            responseUserName = (TextView) layout.findViewById(R.id.textViewResponseDisplayUserName);
            responseUserProfession = (TextView) layout.findViewById(R.id.textViewResponseDisplayUserProfession);
            responseString = (TextView) layout.findViewById(R.id.textViewResponseDisplayString);

            currentResponse.RequestId = args.getString("RequestId");
            currentResponse.ResponseId = args.getString("ResponseId");
            currentResponse.ResponseString = args.getString("ResponseString");
            currentResponse.ResponseTime = args.getString("ResponseTime");
            currentResponse.ResponseUserId = args.getString("ResponseUserId");
            currentResponse.ResponseUserName = args.getString("ResponseUserName");
            currentResponse.ResponseUserProfession = args.getString("ResponseUserProfession");
            currentResponse.ResponseUserProfilePhotoServerPath = args.getString("ResponseUserProfilePhotoServerPath");

            requestString.setText(currentRequest.RequestString);


        }

        acceptResponse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return layout;

    }

    private class HttpGetter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urls[0]);
            String line = "";

            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    Log.v("Getter", "Your data: " + builder.toString()); //response data
                } else {
                    Log.e("Getter", "Failed to get data");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            currentResponse = GetObjectsFromResponse(result);
            /*if(currentResponse != null){
                TextView responseUserName = (TextView) findViewById(R.id.textViewResponseUserName);
                TextView responseString = (TextView) findViewById(R.id.textViewRequestString);
                TextView responseUserProfession = (TextView) findViewById(R.id.textViewResponseUserProfession);

                responseUserName.setText(currentResponse.ResponseUserName);
                responseString.setText(currentResponse.ResponseString);
                responseUserProfession.setText(currentResponse.ResponseUserProfession);
            }*/
        }
    }

    private Responses GetObjectsFromResponse(String result) {
        try {

            //JSONObject employee =(new JSONObject(response)).getJSONObject("Requests");
            JSONObject temp = (new JSONObject(result)).getJSONObject("Response");

            Responses response = new Responses();

            response.RequestId = temp.getString("RequestId") != null ? temp.getString("RequestId") : null;
            response.ResponseId= temp.getString("ResponseId") != null ? temp.getString("ResponseId"): null;
            response.ResponseString= temp.getString("ResponseString") != null ? temp.getString("ResponseString"): null;
            response.ResponseUserId = temp.getString("ResponseUserId") != null ? temp.getString("ResponseUserId"): null;
            response.ResponseUserName = temp.getString("ResponseUserName") != null ? temp.getString("ResponseUserName"): null;
            response.ResponseUserProfession = temp.getString("ResponseUserProfession") != null ? temp.getString("ResponseUserProfession"): null;
            response.ResponseUserProfilePhotoServerPath = temp.getString("ResponseUserProfilePhotoServerPath") != null ? temp.getString("ResponseUserProfilePhotoServerPath"): null;

            return response;
        }
        catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_response, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
