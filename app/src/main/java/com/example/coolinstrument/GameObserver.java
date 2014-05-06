package com.example.coolinstrument;

import org.json.JSONObject;

import java.util.Date;
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
                Object[] methodArgs = new Object[2];
                methodArgs[0] = Global.gameID;
                methodArgs[1] = Global.pID;
                Global.client.call("addPlayer", methodArgs);
                Global.client.call("removeDatas", new Object[]{});
                synchronized(Lobby.obj) {
                    Lobby.obj.notify();
                }
            }

            if (((String) msg).contains("connected")) {
                Global.pID = info.getString("session");
            }

            if (info.has("collection") && info.getString("collection").equals("games")) {
                String type = info.getString("msg");
                if (type.equals("added")) {
                    String gameID = info.getString("id");
                    int users = 0;
                    if (info.has("fields")) {
                        JSONObject fields = info.getJSONObject("fields");
                        String title = fields.getString("title");
                        String artist = fields.getString("artist");
                        Global.songs.put(gameID, title + " - " + artist);
                        users = fields.getJSONArray("playerIds").length();
                    }
                    System.out.println("Game " + gameID + " has " + users + " users");
                    Lobby.addGame(gameID, users);
                } else if (type.equals("changed")) {
                    String gameID = info.getString("id");
                    int users = info.getJSONObject("fields").getJSONArray("playerIds").length();
                    System.out.println("Game " + gameID + " has " + users + " users");
                    Lobby.changeGame(gameID, users);
                } else if (type.equals("removed")) {
                    String gameID = info.getString("id");
                    Lobby.removeGame(gameID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
