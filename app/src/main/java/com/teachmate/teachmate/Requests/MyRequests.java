package com.teachmate.teachmate.Requests;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.teachmate.teachmate.DBHandlers.RequestsDBHandler;
import com.teachmate.teachmate.R;
import com.teachmate.teachmate.TempDataClass;
import com.teachmate.teachmate.models.Requests;

import java.util.List;

public class MyRequests extends Fragment {

    ListView listViewRequests;
    ListAdapter listAdapter;

    FragmentActivity activity;

    public MyRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (FragmentActivity) super.getActivity();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_my_requests, container, false);

        listViewRequests = (ListView) layout.findViewById(R.id.listViewMyRequests);

        populateListView(RequestsDBHandler.GetAllRequests(getActivity().getApplicationContext()));
        return layout;
    }

    private void populateListView(final Requests[] requestsArray) {

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

                    Fragment individualRequestDisplayFragment = new ViewResponsesForARequest();
                    individualRequestDisplayFragment.setArguments(i);
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    TempDataClass.fragmentStack.push(currentFragment);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, individualRequestDisplayFragment)
                            .addToBackStack("MyRequestsStack")
                            .commit();

                }
                catch(Exception ex){
                    Toast.makeText(getActivity().getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

        });

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
        public void onFragmentInteraction(Uri uri);
    }

}
