package com.example.coolinstrument;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Observer;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import me.kutrumbos.DdpClient;


public class Lobby extends Activity {
    private static ScrollView scroll;
    private static LinearLayout inner;
    private static Activity act = new Activity();
    private static View.OnClickListener listener;

    private static Intent goToInstrument;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        new GetPort().execute();

        scroll = new ScrollView(this);
        inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(inner);

        Button create = new Button(this);
        create.setText("Create New Game");
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Global.client.call("createGame", new Object[]{});
                while (Global.gameID == null) {
                    try { Thread.sleep(200); }
                    catch (Exception e) { e.printStackTrace(); }
                }
                Object[] methodArgs = new Object[2];
                methodArgs[0] = Global.gameID;
                methodArgs[1] = Global.pID;
                Global.client.call("addPlayer", methodArgs);
                startActivity(goToInstrument);
            }

        });

        inner.addView(create);

        listener = new View.OnClickListener() {
            public void onClick(View v) {
                Global.gameID = Global.IDs.get((Button) v);
                Object[] methodArgs = new Object[2];
                methodArgs[0] = Global.gameID;
                methodArgs[1] = Global.pID;
                Global.client.call("addPlayer", methodArgs);
                startActivity(goToInstrument);
            }
        };

        goToInstrument = new Intent(this, Instrument.class);
/*
        Set<Map.Entry<String, Integer>> games = Global.games.entrySet();
        Iterator iter = games.iterator();



        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry) iter.next();
            Button b = new Button(this);
            b.setText("Song:\n# Players: " + ((String) pairs.getValue()));
            inner.addView(b);
        } */

        setContentView(scroll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lobby, menu);
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

    public static void addGame(final String gameID, final int n){
        act.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                Button b = new Button(inner.getContext());
                b.setText("Song:\n# Players: " + n);
                b.setOnClickListener(listener);
                inner.addView(b);
                Global.games.put(gameID, b);
                Global.IDs.put(b, gameID);
            }
        });
    }

    public static void changeGame(final String gameID, final int n){
        act.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                Button b = Global.games.get(gameID);
                b.setText("Song:\n# Players " + n);
            }
        });
    }

    public static void removeGame(final String gameID){
        act.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                Button b = Global.games.get(gameID);
                inner.removeView(b);
                Global.games.remove(gameID);
            }
        });
    }

    private class GetPort extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Lobby.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... songUrl) {
            ServiceHandler sh = new ServiceHandler();
            String url = "http://" + Global.serverUrl + ":8000";

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject info = new JSONObject(jsonStr);
                    String host = info.getString("host");
                    Global.port = info.getInt("port");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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
            if (pDialog.isShowing()) pDialog.dismiss();

            try {
                Global.client = new DdpClient(Global.serverUrl, Global.port);
                Observer obs = new GameObserver();
                Global.client.addObserver(obs);
                Global.client.connect();

                Thread.sleep(500);

                Global.client.subscribe("games", new Object[]{});

                System.out.println("calling remote method...");

                // TODO: pick and join a game
                Object[] methodArgs = new Object[1];
                methodArgs[0] = new Date().toString();
                Global.client.call("update_time", methodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
