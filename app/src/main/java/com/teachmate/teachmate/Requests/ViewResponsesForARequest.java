package com.teachmate.teachmate.Requests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teachmate.teachmate.R;
import com.teachmate.teachmate.Responses.ResponseDisplayActivity;
import com.teachmate.teachmate.TempDataClass;
import com.teachmate.teachmate.models.Requests;
import com.teachmate.teachmate.models.Responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ViewResponsesForARequest extends Fragment {

    FragmentActivity activity;

    Requests currentRequest;

    TextView requestString;
    TextView requestTime;

    ListView listViewResponses;
    ListAdapter listAdapter;
    ProgressDialog progressDialog;

    public ViewResponsesForARequest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (FragmentActivity) super.getActivity();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_view_responses_for_arequest, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        listViewResponses = (ListView) layout.findViewById(R.id.listViewMyRequestsResponsesDisplay);

        currentRequest = new Requests();

        Bundle args = getArguments();

        currentRequest.RequestID = args.getString("RequestID");
        currentRequest.RequesteUserId = args.getString("RequesteUserId");
        currentRequest.RequestUserName = args.getString("RequestUserName");
        currentRequest.RequestString = args.getString("RequestString");
        currentRequest.RequestUserProfession = args.getString("RequestUserProfession");
        currentRequest.RequestUserProfilePhotoServerPath= args.getString("RequestUserProfilePhotoServerPath");
        currentRequest.RequestTime = args.getString("RequestTime");

        requestString = (TextView) layout.findViewById(R.id.textViewMyRequestString);
        requestTime = (TextView) layout.findViewById(R.id.textViewMyRequestTime);

        requestString.setText(currentRequest.RequestString);
        requestTime.setText(currentRequest.RequestTime);

        HttpGetter getter = new HttpGetter();
        getter.execute("http://teach-mate.azurewebsites.net/Response/GetAllResponsesForARequest?id="+ TempDataClass.serverUserId+"&lastResponseId=0");

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
            List<Responses> list = GetObjectsFromResponse(result);
            if(list != null){
                populateListView(list);
            }
            progressDialog.dismiss();
        }
    }

    private void populateListView(List<Responses> list) {

        final Responses[] responsesArray = new Responses[list.size()];
        for(int i = 0; i < list.size(); i++){
            responsesArray[i] = list.get(i);
        }
        listAdapter = new ResponsesArrayAdapter(getActivity(), responsesArray);
        listViewResponses.setAdapter(listAdapter);

        listViewResponses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View
                    view, int position, long id) {

                try {
                    Intent i = new Intent(getActivity().getApplicationContext(), ResponseDisplayActivity.class);
                    i.putExtra("RequestId", responsesArray[position].RequestId);
                    i.putExtra("ResponseId", responsesArray[position].ResponseId);
                    i.putExtra("ResponseString", responsesArray[position].ResponseString);
                    i.putExtra("ResponseTime", responsesArray[position].ResponseTime);
                    i.putExtra("ResponseUserId", responsesArray[position].ResponseUserId);
                    i.putExtra("ResponseUserName", responsesArray[position].ResponseUserName);
                    i.putExtra("ResponseUserProfession", responsesArray[position].ResponseUserProfession);
                    i.putExtra("ResponseUserProfilePhotoServerPath", responsesArray[position].ResponseUserProfilePhotoServerPath);
                    startActivity(i);
                } catch (Exception ex) {
                    Toast.makeText(getActivity().getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    private List<Responses> GetObjectsFromResponse(String result) {
        try {
            JSONArray contacts = (new JSONObject(result)).getJSONArray("Responses");

            List<Responses> list = new ArrayList<Responses>();

            for(int i = 0; i < contacts.length(); i++){
                Responses response = new Responses();
                JSONObject temp = contacts.getJSONObject(i);

                response.RequestId = temp.getString("RequestId") != null ? temp.getString("RequestId") : null;
                response.ResponseId= temp.getString("ResponseId") != null ? temp.getString("ResponseId"): null;
                response.ResponseString= temp.getString("ResponseString") != null ? temp.getString("ResponseString"): null;
                response.ResponseUserId = temp.getString("ResponseUserId") != null ? temp.getString("ResponseUserId"): null;
                response.ResponseUserName = temp.getString("ResponseUserName") != null ? temp.getString("ResponseUserName"): null;
                response.ResponseUserProfession = temp.getString("ResponseUserProfession") != null ? temp.getString("ResponseUserProfession"): null;
                response.ResponseUserProfilePhotoServerPath = temp.getString("ResponseUserProfilePhotoServerPath") != null ? temp.getString("ResponseUserProfilePhotoServerPath"): null;

                list.add(response);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
