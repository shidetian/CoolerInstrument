package com.example.coolinstrument;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import android.widget.Button;

public class GameObserver implements Observer {

    @Override
    public void update(Observable client, Object msg) {
        try {
            JSONObject info = new JSONObject((String) msg);

            System.out.println(msg);

            if (info.has("result")) {
                Global.gameID = info.getString("result");
            }

            if (((String) msg).contains("connected")) {
                Global.pID = info.getString("session");
            }

            if (info.has("collection") && info.getString("collection").equals("games")) {
                String type = info.getString("msg");
                if (type.equals("added")) {
                    String gameID = info.getString("id");
                    int users = info.has("fields") ?
                            info.getJSONObject("fields").getJSONArray("playerIds").length() : 0;
                    System.out.println("Game " + gameID + " has " + users + " users");
                    Lobby.addGame(gameID, users);
                } else if (type.equals("changed")) {
                    String gameID = info.getString("id");
                    int users = info.getJSONObject("fields").getJSONArray("playerIds").length();
                    System.out.println("Game " + gameID + " has " + users + " users");
                    if (Global.games.containsKey(gameID)) {
                        Lobby.changeGame(gameID, users);
                    } else {
                        Lobby.addGame(gameID, users);
                    }
                } else {
                    String gameID = info.getString("id");
                    if (Global.games.containsKey(gameID)) {
                        Lobby.removeGame(gameID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
