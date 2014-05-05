package com.example.coolinstrument;

import me.kutrumbos.DdpClient;
import android.widget.Button;
import java.util.HashMap;

/**
 * Created by lam on 4/19/14.
 */
public class Global {
    public static DdpClient client;
    public static String serverUrl = "10.32.5.69";
    public static int port = 3000;
    public static String gameID;
    public static String pID;
    public static HashMap<String, Button> games = new HashMap<String, Button>();
    public static HashMap<Button, String> IDs = new HashMap<Button, String>();
}
