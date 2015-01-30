package com.teachmate.teachmate.questions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.teachmate.teachmate.DBHandlers.QuestionModelDBHandler;
import com.teachmate.teachmate.R;
import com.teachmate.teachmate.TempDataClass;
import com.teachmate.teachmate.models.Question_Model;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
public class Ask_question extends Fragment {
    ArrayList<String> myquestionids = new ArrayList<String>();
    Question_Model myquestionsdb = new Question_Model();
    public static EditText etquestiontitle,etcategorytitle;

    Button post;
    public String created_time;
    public String questionmessage,category;
    public String result;
    public String hour;
    public String minutes;
    public String month;
    public String day;
    public String seconds;
    public String year;
    public Calendar c=Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentActivity fragmentActivity=(FragmentActivity)super.getActivity();

        RelativeLayout relativeLayout=(RelativeLayout)inflater.inflate(R.layout.new_question_layout,container,false);
        etquestiontitle=(EditText)relativeLayout.findViewById(R.id.etquestiontitle);
        etcategorytitle=(EditText)relativeLayout.findViewById(R.id.etcategorytitle);

        post=(Button)relativeLayout.findViewById(R.id.buttonquestionpost);

        //Setting functionality for post button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes=Integer.toString(c.get(Calendar.MINUTE));//getting time from calendar object
                hour=Integer.toString(c.get(Calendar.HOUR));
                seconds=Integer.toString(c.get(Calendar.SECOND));
                month=Integer.toString(c.get(Calendar.MONTH)+1);
                day=Integer.toString(c.get(Calendar.DAY_OF_MONTH));
                year=Integer.toString(c.get(Calendar.YEAR));
                created_time=day+"/"+month+"/"+year+" "+hour+":"+minutes+":"+seconds;
                questionmessage=Ask_question.etquestiontitle.getText().toString();
                category=Ask_question.etcategorytitle.getText().toString();
                if(TextUtils.isEmpty(category)){
                    Toast.makeText(getActivity(),"Please add a Category to your question",Toast.LENGTH_LONG).show();
                }else {
                    ask_question_async ask = new ask_question_async();
                    ask.execute("http://teach-mate.azurewebsites.net/QuestionForum/AddQuestion");//My server url to hit
                }

            }
        });


        return relativeLayout;
    }


    public class ask_question_async extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost post= new HttpPost(params[0]);


            String json="";
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("UserId", TempDataClass.serverUserId);
                jsonObject.put("TimeOfQuestion",created_time);
                jsonObject.put("QuestionMessage",questionmessage);
                jsonObject.put("Category",category);
                jsonObject.put("QuestionBoardId","3");

                json=jsonObject.toString();
                StringEntity se=new StringEntity(json);
                post.setEntity(se);
                HttpResponse response=httpClient.execute(post);
                inputStream = response.getEntity().getContent();
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            myquestionsdb.setQuestion_id(s);
            myquestionsdb.setQuestion(questionmessage);
            myquestionsdb.setAsked_time(created_time);
            myquestionsdb.setUsername(TempDataClass.userName);
            myquestionsdb.setImage(TempDataClass.profilePhotoServerPath);
            myquestionsdb.setCategory(category);
            addtomyquestionsdb();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, TempDataClass.fragmentStack.lastElement());
            TempDataClass.fragmentStack.pop();
            ft.commit();

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
    public void addtomyquestionsdb(){
        QuestionModelDBHandler addtodb=new QuestionModelDBHandler();
        // public static final AnswerModelDBHandler addanswertodb=new AnswerModelDBHandler();
      //  addtodb.InsertQuestionModel(getActivity(),dbadd);
        addtodb.Insertintomyquestionstable(getActivity(),myquestionsdb);
      //  addanswertodb.InsertAnswerList(getApplicationContext(),answerlist);


        Toast.makeText(getActivity(),"added to db",Toast.LENGTH_SHORT).show();

    }

}
