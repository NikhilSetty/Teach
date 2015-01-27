package com.teachmate.teachmate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.teachmate.teachmate.Chat.PreviousChatFragment;
import com.teachmate.teachmate.Requests.MyRequests;
import com.teachmate.teachmate.Requests.RequestDisplayActivity;
import com.teachmate.teachmate.Requests.RequestsDisplayActivity;
import com.teachmate.teachmate.Responses.ResponseDisplayActivity;

import java.util.Stack;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,PreviousChatFragment.OnFragmentInteractionListener {

    Fragment initialFragment;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean isThroughNotification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = "";
        try {
            type = getIntent().getStringExtra("type");
            isThroughNotification = true;
        }catch(Exception ex){
            type = "";
            isThroughNotification = false;
        }
        if(type == null) {
            type = "";
            isThroughNotification = false;
        }
        setContentView(R.layout.activity_main);

        Bundle extras = new Bundle();

        switch (type){
            case "request":
                extras.putString("NotificationRequestId", getIntent().getStringExtra("NotificationRequestId"));
                initialFragment = new RequestDisplayActivity();
                initialFragment.setArguments(extras);
                break;
            case "response":
                extras.putString("NotificationResponseId", getIntent().getStringExtra("NotificationResponseId"));
                extras.putString("NotificationRequestId", getIntent().getStringExtra("NotificationRequestId"));
                extras.putString("NotificationResponseUserId", getIntent().getStringExtra("NotificationResponseUserId"));
                extras.putString("NotificationResponseUserName", getIntent().getStringExtra("NotificationResponseUserName"));
                extras.putString("NotificationResponseMessage", getIntent().getStringExtra("NotificationResponseMessage"));
                extras.putString("NotificationResponseUserProfession", getIntent().getStringExtra("NotificationResponseUserProfession"));
                initialFragment = new ResponseDisplayActivity();
                initialFragment.setArguments(extras);
                break;
            default:
                initialFragment = new HomeFragment();
                break;
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (isThroughNotification) {
            replaceFragment();
        }
    }

    @Override
    public void onBackPressed(){
        if(!(TempDataClass.fragmentStack.size() == 0)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, TempDataClass.fragmentStack.lastElement());
            TempDataClass.fragmentStack.pop();
            ft.commit();
        }
        else{
            mNavigationDrawerFragment.openDrawer();
        }
    }

    private void replaceFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment temp = initialFragment;
        if(!TempDataClass.fragmentStack.lastElement().equals(new HomeFragment()) && TempDataClass.fragmentStack.size() != 1) {
            TempDataClass.fragmentStack.push(temp);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, initialFragment)
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        TempDataClass.fragmentStack = new Stack<Fragment>();
        TempDataClass.fragmentStack.push(new HomeFragment());
        switch(position) {
            case 0:
                initialFragment = new HomeFragment();
                break;
            case 1:
                initialFragment = new RequestsDisplayActivity();
                break;
            case 2:
                initialFragment = new MyRequests();
                break;
            case 3:
                initialFragment = new PreviousChatFragment();
                break;
            default:
                initialFragment = new HomeFragment();
                break;
        }

        if(TempDataClass.isThroughSplash && !isThroughNotification) {
            replaceFragment();
        }

    }

    public void onSectionAttached(String title) {
        switch (title) {
            case FragmentTitles.REQUESTS:
                mTitle = FragmentTitles.REQUESTS;
                break;
            case FragmentTitles.MY_REQUESTS:
                mTitle = FragmentTitles.MY_REQUESTS;
                break;
            case FragmentTitles.HOME:
                mTitle = FragmentTitles.HOME;
                break;
            case FragmentTitles.CHAT:
                mTitle = FragmentTitles.CHAT;
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void onFragmentInteraction(String id) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
