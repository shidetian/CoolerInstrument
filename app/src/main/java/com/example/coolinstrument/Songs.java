package com.example.coolinstrument;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Songs extends ListActivity {
    private ProgressDialog pDialog;

    // URL to get contacts JSON
//    private static String url = "http://api.androidhive.info/contacts/";
    private static String url = "http://amusing3.meteor.com/songsApi";

    // JSON Node names
    private static final String TAG_TITLE = "title";
//    private static final String TAG_DESC = "desc";
    private static final String TAG_CREATED_AT = "createdAt";
    //    private static final String TAG_NUM_TRACKS = "number of tracks";
    private static final String TAG_ID = "_id";

    // contacts JSONArray
    JSONArray songs = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        ListView lv = getListView();

        new GetSongs().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetSongs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Songs.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            songList = new ArrayList<HashMap<String, String>>();

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    songs = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < songs.length(); i++) {
                        JSONObject songJson = songs.getJSONObject(i);

                        String id = songJson.getString(TAG_ID);
                        String title = songJson.getString(TAG_TITLE);
                        String createdAt = songJson.getString(TAG_CREATED_AT);

                        // tmp hashmap for single song
                        HashMap<String, String> song = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        song.put(TAG_ID, id);
                        song.put(TAG_TITLE, title);
                        song.put(TAG_CREATED_AT, createdAt);
//                        contact.put(TAG_EMAIL, email);
//                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding contact to contact list
                        songList.add(song);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Songs.this, songList,
                    R.layout.list_songs, new String[] { TAG_TITLE, TAG_CREATED_AT}, new int[] { R.id.title,
                    R.id.created_at});

            setListAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.songs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
