package com.teachmate.teachmate.Chat;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teachmate.teachmate.DBHandlers.ChatInfoDBHandler;
import com.teachmate.teachmate.R;
import com.teachmate.teachmate.TempDataClass;
import com.teachmate.teachmate.models.ChatInfo;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends ListActivity {


    ArrayList<Message> messages;
    ChatAdapter adapter;
    EditText text;
    Button send;
    boolean sentBy = true;
    Time currentTime;
    static String chatId;
    String receivedFrom;
    String receivedAt;
    String time;
    public static final int CHAT_NOTIFICATION = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acitivity);


        messages = new ArrayList<Message>();
        List<ChatInfo> previousChatMessages = ChatInfoDBHandler.GetPreviousChat(getApplicationContext(), chatId);
        if (previousChatMessages != null) {
            for (ChatInfo chatmessages : previousChatMessages) {
                messages.add(new Message(chatmessages.getMessage(), chatmessages.isSentBy()));
            }
        }
        adapter = new ChatAdapter(this, messages);
        setListAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //message bundle from intent service
        if (bundle != null && bundle.containsKey("Message")) {
            String receivedMessage = bundle.getString("Message");
            Log.d("Received Message",receivedMessage);
            addNewMessage(new Message(receivedMessage, false));
        }
        //chatId bundle from intent service
            if (bundle != null && bundle.containsKey("ChatId")) {
            chatId = bundle.getString("ChatId");
        }
        //username bundle from intent service
        if (bundle != null && bundle.containsKey("UserName")) {
            receivedFrom = bundle.getString("UserName");
        }
        //previousConversation bundle from previous chat
        if (bundle != null && bundle.containsKey("previousConversation")) {
            chatId = bundle.getString("previousConversation");
        }

        currentTime = new Time();
        text = (EditText) this.findViewById(R.id.text);
        send = (Button) this.findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage = text.getText().toString().trim();
                if (!TextUtils.isEmpty(newMessage)) {
                    text.setText("");
                    addNewMessage(new Message(newMessage, sentBy));
                    //sentBy = !sentBy;
                    new SendMessage().execute("http://teach-mate.azurewebsites.net/Chat/ChatMsg", newMessage);
                }
            }
        });

/*        messages = new ArrayList<Message>();
        List<ChatInfo> previousChatMessages = ChatInfoDBHandler.GetPreviousChat(getApplicationContext(), chatId);
        if (previousChatMessages != null) {
            for (ChatInfo chatmessages : previousChatMessages) {
                messages.add(new Message(chatmessages.getMessage(), chatmessages.isSentBy()));
            }
        }
        adapter = new ChatAdapter(this, messages);
        setListAdapter(adapter);*/


    }

    void addNewMessage(Message m) {
        messages.add(m);
        adapter.notifyDataSetChanged();
        getListView().setSelection(messages.size() - 1);



        if(m.isMine() == true) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();
            time = dateFormat.format(date);
        }
        else{
            time = receivedAt;
        }

        ChatInfo newMessage = new ChatInfo();
        newMessage.setMessage(m.getMessage());
        newMessage.setSentBy(m.isMine());
        newMessage.setTimeStamp(time);
        newMessage.setChatId(chatId);

        ChatInfoDBHandler.InsertChatInfo(getApplicationContext(), newMessage);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  String POST(String url, String message) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chatId", chatId);
            jsonObject.put("message", message);
            jsonObject.put("SenderId", Integer.parseInt(TempDataClass.serverUserId));
//            jsonObject.put("userName", TempDataClass.userName);
            jsonObject.put("type",CHAT_NOTIFICATION );
            jsonObject.put("SentOn", time);


            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            result = convertInputStreamToString(inputStream);

//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class SendMessage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0], urls[1]);
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
}