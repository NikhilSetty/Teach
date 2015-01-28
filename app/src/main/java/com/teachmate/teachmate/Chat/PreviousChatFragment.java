
package com.teachmate.teachmate.Chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teachmate.teachmate.DBHandlers.ChatIdMapDBHandler;
import com.teachmate.teachmate.FragmentTitles;
import com.teachmate.teachmate.MainActivity;
import com.teachmate.teachmate.R;
import com.teachmate.teachmate.models.ChatIdMap;

import java.util.ArrayList;
import java.util.List;

public class PreviousChatFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    private ListView                      mListView;

    private ArrayAdapter                  mAdapter;
    List<ChatIdMap>                       previousChat;

    public PreviousChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // seed db for testing

      /*  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // get current date time with Date()
        Date date = new Date();
        String time = dateFormat.format(date);
        ChatInfo newMessage = new ChatInfo();
        newMessage.setMessage("a");
        newMessage.setSentBy(true);
        newMessage.setTimeStamp(time);
        newMessage.setChatId("1");
        ChatInfoDBHandler.InsertChatInfo(getActivity().getApplicationContext(),newMessage);


        newMessage = new ChatInfo();
        newMessage.setMessage("b");
        newMessage.setSentBy(true);
        newMessage.setTimeStamp(time);
        newMessage.setChatId("1");
        ChatInfoDBHandler.InsertChatInfo(getActivity().getApplicationContext(),newMessage);

        newMessage = new ChatInfo();
        newMessage.setMessage("c");
        newMessage.setSentBy(false);
        newMessage.setTimeStamp(time);
        newMessage.setChatId("1");
        ChatInfoDBHandler.InsertChatInfo(getActivity().getApplicationContext(),newMessage);

        newMessage = new ChatInfo();
        newMessage.setMessage("d");
        newMessage.setSentBy(true);
        newMessage.setTimeStamp(time);
        newMessage.setChatId("1");
        ChatInfoDBHandler.InsertChatInfo(getActivity().getApplicationContext(),newMessage);

        ChatIdMap idMap = new ChatIdMap();
        idMap.userName = "g";
        idMap.userId = "123";
        idMap.chatId = "1";
        ChatIdMapDBHandler.InsertChatIdMap(getActivity().getApplicationContext(), idMap);

        idMap = new ChatIdMap();
        idMap.userName = "kj";
        idMap.userId = "124";
        idMap.chatId = "2";
        ChatIdMapDBHandler.InsertChatIdMap(getActivity().getApplicationContext(), idMap);

        idMap = new ChatIdMap();
        idMap.userName = "h";
        idMap.userId = "125";
        idMap.chatId = "3";
        ChatIdMapDBHandler.InsertChatIdMap(getActivity().getApplicationContext(), idMap);

        idMap = new ChatIdMap();
        idMap.userName = "k";
        idMap.userId = "123";
        idMap.chatId = "4";
        ChatIdMapDBHandler.InsertChatIdMap(getActivity().getApplicationContext(), idMap);

        idMap = new ChatIdMap();
        idMap.userName = "q";
        idMap.userId = "125";
        idMap.chatId = "5";
        ChatIdMapDBHandler.InsertChatIdMap(getActivity().getApplicationContext(), idMap);

        // TODO: Change Adapter to display your content
        String[] sample = { "a", "b", "c", "d", "e" };*/
        previousChat = ChatIdMapDBHandler.getPreviousChatRecords(getActivity()
                .getApplicationContext());
        List<String> previousChatUserNames = new ArrayList<String>();
        for (ChatIdMap map : previousChat) {
            previousChatUserNames.add(map.userName);
        }
        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, previousChatUserNames);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        if (mAdapter != null){
            setListAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(FragmentTitles.CHAT);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    // if (null != mListener) {
    // // Notify the active callbacks interface (the activity, if the
    // // fragment is attached to one) that an item has been selected.
    // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
    // }
    // }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String selectedConversation = previousChat.get(position).chatId;
        Intent intent = new Intent(getActivity().getApplicationContext(),
                ChatActivity.class);
        intent.putExtra("previousConversation", selectedConversation);
        startActivity(intent);
    }
}
