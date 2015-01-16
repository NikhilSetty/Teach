package com.teachmate.teachmate.Requests;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teachmate.teachmate.MainActivity;
import com.teachmate.teachmate.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class RequestsDisplayActivity extends Fragment {

    ListView listViewRequests;
    ListAdapter listAdapter;

    String newRequestString;
    boolean isCurrentLocation;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = (FragmentActivity) super.getActivity();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_requests_display, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        listViewRequests = (ListView) layout.findViewById(R.id.listViewRequests);

        //Debug Code
        String result = "{'UserId':1,'Requests':[{'RequestId':1,'RequesteUserId':'2', 'RequestUserName':'Umang', 'RequestMessage':'Help me, baby!', 'RequestUserProfession':'Software Engineer', 'RequestUserProfilePhotoServerPath':'C:/profile.png', 'RequestedTime':'12/13/14 9.48 a.m.'},{'RequestId':2,'RequesteUserId':3, 'RequestUserName':'Anuj', 'RequestMessage':'Get me out of here', 'RequestUserProfession':'Priest', 'RequestUserProfilePhotoServerPath':'C:/profile.png', 'RequestedTime':'12/14/14 8.48 a.m.'}]}";
        List<Requests> list = GetObjectsFromResponse(result);
        if(list != null){
            populateListView(list);
            progressDialog.dismiss();
        }

        /*HttpGetter getter = new HttpGetter();
        getter.execute("http://10.163.180.110/doctool/Main/GetHelpRequests?id=1");*/

        return layout;

    }

    private void populateListView(List<Requests> list) {

        final Requests[] requestsArray = new Requests[list.size()];
        for(int i = 0; i < list.size(); i++){
            requestsArray[i] = list.get(i);
        }
        listAdapter = new RequestsArrayAdapter(getActivity(), requestsArray);
        listViewRequests.setAdapter(listAdapter);

        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View
                    view, int position, long id) {

                try {
                    Bundle i = new Bundle();
                    i.putString("RequestID", requestsArray[position].RequestID);
                    i.putString("RequesteUserId", requestsArray[position].RequesteUserId);
                    i.putString("RequestUserName", requestsArray[position].RequestUserName);
                    i.putString("RequestString", requestsArray[position].RequestString);
                    i.putString("RequestUserProfession", requestsArray[position].RequestUserProfession);
                    i.putString("RequestUserProfilePhotoServerPath", requestsArray[position].RequestUserProfilePhotoServerPath);
                    i.putString("RequestTime", requestsArray[position].RequestTime);

                    Fragment individualRequestDisplayFragment = new RequestDisplayActivity();
                    individualRequestDisplayFragment.setArguments(i);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, individualRequestDisplayFragment)
                            .addToBackStack("stack")
                            .commit();

                }
                catch(Exception ex){
                    Toast.makeText(getActivity().getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    private List<Requests> GetObjectsFromResponse(String response) {
        try {

            //JSONObject employee =(new JSONObject(response)).getJSONObject("Requests");
            JSONArray contacts = (new JSONObject(response)).getJSONArray("Requests");

            List<Requests> list = new ArrayList<Requests>();


            for(int i = 0; i < contacts.length(); i++){
                Requests request = new Requests();
                JSONObject temp = contacts.getJSONObject(i);

                request.RequestID = temp.getString("RequestId") != null ? temp.getString("RequestId") : null;
                request.RequestUserName = temp.getString("RequestUserName") != null ? temp.getString("RequestUserName"): null;
                request.RequestString = temp.getString("RequestMessage") != null ? temp.getString("RequestMessage"): null;
                request.RequesteUserId = temp.getString("RequesteUserId") != null ? temp.getString("RequesteUserId"): null;
                request.RequestTime = temp.getString("RequestedTime") != null ? temp.getString("RequestedTime"): null;
                request.RequestUserProfession = temp.getString("RequestUserProfession") != null ? temp.getString("RequestUserProfession"): null;
                request.RequestUserProfilePhotoServerPath = temp.getString("RequestUserProfilePhotoServerPath") != null ? temp.getString("RequestUserProfilePhotoServerPath"): null;

                list.add(request);

            }

            return list;
        }
        catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
        //TODO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_request) {
            GenerateNewRequest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GenerateNewRequest() {
        LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
        View promptsView = li.inflate(R.layout.alert_prompt_new_request, null);

        ArrayList<String> array = new ArrayList<String>();
        array.add("Registered Locations");
        array.add("Current Locations");
        final Spinner spinner1;
        ArrayAdapter<String> mAdapter;
        spinner1= (Spinner) promptsView.findViewById(R.id.spinnerLocationSelector);
        final EditText requestEditText = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, array);
        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(mAdapter);


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setMessage("Generate New Request!");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(requestEditText.getText().toString().equals("") || requestEditText.getText() == null){
                            Toast.makeText(getActivity().getApplicationContext(), "Please enter a Request Message!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            newRequestString = requestEditText.getText().toString();
                            if(spinner1.getSelectedItem().toString().equals("Registered Locations")){
                                isCurrentLocation = false;
                            }
                            else{
                                isCurrentLocation = true;
                            }
                            HttpAsyncTaskPOST newPost = new HttpAsyncTaskPOST();
                            newPost.execute("http://10.163.180.110/doctool/Main/SendPushRequests");
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserId", "1");
            jsonObject.put("RequestMessage", newRequestString);
            if(isCurrentLocation){
                jsonObject.put("isCurrentLocation", "true");
                jsonObject.put("Location", 123 + "," + 122);
            }
            else {
                jsonObject.put("isCurrentLocation", "false");
                jsonObject.put("Location", "");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH/mm/ss");
            String currentDateandTime = sdf.format(new Date());
            jsonObject.put("TimeOfRequest", currentDateandTime);
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

    private class HttpAsyncTaskPOST extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), "Data Sent! -" + result.toString(), Toast.LENGTH_LONG).show();
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
            List<Requests> list = GetObjectsFromResponse(result);
            if(list != null){
                populateListView(list);
            }
            progressDialog.dismiss();
        }
    }

}