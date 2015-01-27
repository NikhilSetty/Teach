package com.teachmate.teachmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teachmate.teachmate.Answers.Answer_Adapter;
import com.teachmate.teachmate.DBHandlers.AnswerModelDBHandler;
import com.teachmate.teachmate.DBHandlers.NotifsTableDbHandler;
import com.teachmate.teachmate.DBHandlers.QuestionModelDBHandler;
import com.teachmate.teachmate.models.Answer_Model;
import com.teachmate.teachmate.models.Question_Model;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by archon on 11-01-2015.
 */
public class clicked extends Fragment {
    ListView l;
    QuestionModelDBHandler addtodb=new QuestionModelDBHandler();

    Answer_Adapter answer_adapter;
    public String question,value,qid1,imageurl,asked_time,username,category,u;
    ArrayList<Answer_Model> answerlist;
    AnswerModelDBHandler addanswertodb=new AnswerModelDBHandler();
    String latestanswerid;

    Question_Model dbadd=new Question_Model();
    Answer_Model answer=new Answer_Model();
    TextView tvusername,tvquestion,tvaskedtime,tvquestion_id1;
    ImageView imageView;
    Button addtodbbtn,addreply;
    public String created_time;
    public String hour;
    public String minutes;
    public String month;
    public String day;
    public String seconds;
    public String year;
    public Calendar c=Calendar.getInstance();

    static FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity =(FragmentActivity)super.getActivity();
        RelativeLayout relativeLayout=(RelativeLayout)inflater.inflate(R.layout.clicked,container,false);
        tvusername=(TextView)relativeLayout.findViewById(R.id.tvusername);
        tvquestion=(TextView)relativeLayout.findViewById(R.id.tvquestion);
        tvaskedtime=(TextView)relativeLayout.findViewById(R.id.tvaskedtime);
        tvquestion_id1=(TextView)relativeLayout.findViewById(R.id.tvquestion_id);
        addreply=(Button)relativeLayout.findViewById(R.id.answerbutton);
        addtodbbtn=(Button)relativeLayout.findViewById(R.id.addtodb);

        Bundle bundle=getArguments();

        imageView=(ImageView)relativeLayout.findViewById(R.id.imageView);
        l = (ListView)relativeLayout. findViewById(R.id.listView2);
        answerlist = new ArrayList<>();

         username=bundle.getString("username");
         question=bundle.getString("question");
         asked_time=bundle.getString("asked_time");
         qid1=bundle.getString("question_id");
         u=bundle.getString("image");
        Picasso.with(this.getActivity()).load(u).into(imageView);

        new answerfeed().execute("http://teach-mate.azurewebsites.net/QuestionForum/RetrieveRepliesForAQuestion?id="+qid1);
        addreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle("Your Answer");
                alert.setMessage("Question"+question);


                final EditText input=new EditText(getActivity());

                alert.setView(input);

                alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        value=input.getText().toString();
                        if(answerlist.isEmpty()){
                            latestanswerid="1";
                        }
                        else{
                            latestanswerid=answerlist.get(l.getFirstVisiblePosition()).getAnswer_id();
                        }
                        minutes=Integer.toString(c.get(Calendar.MINUTE));
                        seconds=Integer.toString(c.get(Calendar.SECOND));
                        month=Integer.toString(c.get(Calendar.MONTH)+1);
                        hour=Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                        day=Integer.toString(c.get(Calendar.DAY_OF_MONTH));
                        year=Integer.toString(c.get(Calendar.YEAR));
                        created_time=day+"/"+month+"/"+year+" "+hour+":"+minutes+":"+seconds;
                        //do something with value
                        try {
                            new AddReply().execute("http://teach-mate.azurewebsites.net/QuestionForum/AddReply");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                alert.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //canceled
                    }
                });

                alert.show();

            }
        });
        addtodbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtodb.InsertQuestionModel(getActivity(), dbadd);
                addanswertodb.InsertAnswerList(getActivity().getApplicationContext(), answerlist);
                Toast.makeText(getActivity().getApplicationContext(),"added to db",Toast.LENGTH_SHORT).show();

            }
        });


        tvusername.setText(username);
        tvquestion.setText(question);
        tvquestion_id1.setText(qid1);
        tvaskedtime.setText(asked_time);
      //return super.onCreateView(inflater, container, savedInstanceState);

        dbadd.setUsername(username);
        dbadd.setQuestion(question);
        dbadd.setAsked_time(asked_time);
        dbadd.setQuestion_id(qid1);
       // dbadd.setImage(imageurl);
        dbadd.setCategory(category);

        //return inflater.inflate(R.layout.clicked,container,false);
        return  relativeLayout;
    }
   /* public void addtodb(View v) {

        addtodb.InsertQuestionModel(getActivity(), dbadd);
        addanswertodb.InsertAnswerList(getActivity().getApplicationContext(), answerlist);
        Toast.makeText(getActivity().getApplicationContext(),"added to db",Toast.LENGTH_SHORT).show();
    }*/


    /*public void answer_this_question(View v){
        *//*Intent x=new Intent(this.getActivity(),answer_question.class);
        x.putExtra("question_id",tvquestion_id.getText().toString());
        startActivity(x);*//*

        Bundle data=new Bundle();
        data.putString("question_id", tvquestion_id1.getText().toString());
        Fragment sendanswer=new answer_question();
        sendanswer.setArguments(data);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.layout.clicked,sendanswer);
        transaction.commit();

    }*/
   /* public void answer_this_question(View v){
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setTitle("Your Answer");
        alert.setMessage("Question"+question);


        final EditText input=new EditText(getActivity());

        alert.setView(input);

        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                value=input.getText().toString();
                if(answerlist.isEmpty()){
                    latestanswerid="1";
                }
                else{
                    latestanswerid=answerlist.get(l.getFirstVisiblePosition()).getAnswer_id();
                }
                minutes=Integer.toString(c.get(Calendar.MINUTE));
                seconds=Integer.toString(c.get(Calendar.SECOND));
                month=Integer.toString(c.get(Calendar.MONTH)+1);
                hour=Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                day=Integer.toString(c.get(Calendar.DAY_OF_MONTH));
                year=Integer.toString(c.get(Calendar.YEAR));
                created_time=day+"/"+month+"/"+year+" "+hour+":"+minutes+":"+seconds;
                //do something with value
                new AddReply().execute("http://teach-mate.azurewebsites.net/QuestionForum/AddReply");

            }
        });

        alert.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //canceled
            }
        });

        alert.show();

    }*/




    public class answerfeed extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse response = client.execute(httpGet);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    if(data.length()==0){
                        return false;
                    }

                    JSONObject jobj = new JSONObject(data);
                    JSONArray jarray = jobj.getJSONArray("Replies");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject real=jarray.getJSONObject(i);

                        Answer_Model answer = new Answer_Model();

                        answer.setActualanswer(real.getString("ReplyMessage"));
                        answer.setAnsweredby(real.getString("RepliedBy"));//jobj.getString("validate")
                        answer.setAnswer_id(real.getString("ReplyId"));
                        answer.setAnsweredtime(real.getString("RepliedTime"));
                        answer.setQuestion_id(qid1);
                       /* answer.setAnsweredby("me");
                        answer.setAnsweredtime("now");
                        answer.setAnswer_id("1");
                        answer.setQuestion_id(qid1);*/

                        answerlist.add(answer);


                    }
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean == false) {
                Toast.makeText(activity,"No answers for this question", Toast.LENGTH_LONG).show();
            } else {
                Answer_Adapter answer_adapter1 = new Answer_Adapter(getActivity(), R.layout.answer_single, answerlist);
                l.setAdapter(answer_adapter1);
                // question_adapter.notifyDataSetChanged();
            }
        }

    }
    public class AddReply extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost((params[0]));

                JSONObject replyobj = new JSONObject();
                replyobj.put("ReplyMessage", value);
                replyobj.put("TimeOfReply",created_time);
                replyobj.put("UserID",TempDataClass.serverUserId);
                replyobj.put("QuestionID",qid1);
                String replyjson="";
                replyjson=replyobj.toString();

                StringEntity se=new StringEntity(replyjson);
                httpPost.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";


            }catch (Exception e){
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            NotifsTableDbHandler notifsTableDbHandler=new NotifsTableDbHandler();
            notifsTableDbHandler.addtonotifsdb(getActivity().getApplicationContext(),qid1,latestanswerid);
            Toast.makeText(getActivity().getApplicationContext(),"Added to notifs db "+latestanswerid,Toast.LENGTH_SHORT).show();
        }

        private  String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

    }
}
