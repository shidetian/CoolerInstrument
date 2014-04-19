package com.example.coolinstrument;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by lam on 2/19/14.
 */
public class Piano {
    
    private SoundPool sp;
    private HashMap<Integer, Integer> soundList;
    
    public Piano(Context context) {
        sp = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        soundList = new HashMap<Integer, Integer>();

        soundList.put(24, sp.load(context, R.raw.c1, 1));
        soundList.put(25, sp.load(context, R.raw.db1, 1));
        soundList.put(26, sp.load(context, R.raw.d1, 1));
        soundList.put(27, sp.load(context, R.raw.eb1, 1));
        soundList.put(28, sp.load(context, R.raw.e1, 1));
        soundList.put(29, sp.load(context, R.raw.f1, 1));
        soundList.put(30, sp.load(context, R.raw.gb1, 1));
        soundList.put(31, sp.load(context, R.raw.g1, 1));
        soundList.put(32, sp.load(context, R.raw.ab1, 1));
        soundList.put(33, sp.load(context, R.raw.a1, 1));
        soundList.put(34, sp.load(context, R.raw.bb1, 1));
        soundList.put(35, sp.load(context, R.raw.b1, 1));

        soundList.put(36, sp.load(context, R.raw.c2, 1));
        soundList.put(37, sp.load(context, R.raw.db2, 1));
        soundList.put(38, sp.load(context, R.raw.d2, 1));
        soundList.put(39, sp.load(context, R.raw.eb2, 1));
        soundList.put(40, sp.load(context, R.raw.e2, 1));
        soundList.put(41, sp.load(context, R.raw.f2, 1));
        soundList.put(42, sp.load(context, R.raw.gb2, 1));
        soundList.put(43, sp.load(context, R.raw.g2, 1));
        soundList.put(44, sp.load(context, R.raw.ab2, 1));
        soundList.put(45, sp.load(context, R.raw.a2, 1));
        soundList.put(46, sp.load(context, R.raw.bb2, 1));
        soundList.put(47, sp.load(context, R.raw.b2, 1));

        soundList.put(48, sp.load(context, R.raw.c3, 1));
        soundList.put(49, sp.load(context, R.raw.db3, 1));
        soundList.put(50, sp.load(context, R.raw.d3, 1));
        soundList.put(51, sp.load(context, R.raw.eb3, 1));
        soundList.put(52, sp.load(context, R.raw.e3, 1));
        soundList.put(53, sp.load(context, R.raw.f3, 1));
        soundList.put(54, sp.load(context, R.raw.gb3, 1));
        soundList.put(55, sp.load(context, R.raw.g3, 1));
        soundList.put(56, sp.load(context, R.raw.ab3, 1));
        soundList.put(57, sp.load(context, R.raw.a3, 1));
        soundList.put(58, sp.load(context, R.raw.bb3, 1));
        soundList.put(59, sp.load(context, R.raw.b3, 1));

        // 60 is the noteNumber for middle C
        soundList.put(60, sp.load(context, R.raw.c4, 1));
        soundList.put(61, sp.load(context, R.raw.db4, 1));
        soundList.put(62, sp.load(context, R.raw.d4, 1));
        soundList.put(63, sp.load(context, R.raw.eb4, 1));
        soundList.put(64, sp.load(context, R.raw.e4, 1));
        soundList.put(65, sp.load(context, R.raw.f4, 1));
        soundList.put(66, sp.load(context, R.raw.gb4, 1));
        soundList.put(67, sp.load(context, R.raw.g4, 1));
        soundList.put(68, sp.load(context, R.raw.ab4, 1));
        soundList.put(69, sp.load(context, R.raw.a4, 1));
        soundList.put(70, sp.load(context, R.raw.bb4, 1));
        soundList.put(71, sp.load(context, R.raw.b4, 1));

        soundList.put(72, sp.load(context, R.raw.c5, 1));
        soundList.put(73, sp.load(context, R.raw.db5, 1));
        soundList.put(74, sp.load(context, R.raw.d5, 1));
        soundList.put(75, sp.load(context, R.raw.eb5, 1));
        soundList.put(76, sp.load(context, R.raw.e5, 1));
        soundList.put(77, sp.load(context, R.raw.f5, 1));
        soundList.put(78, sp.load(context, R.raw.gb5, 1));
        soundList.put(79, sp.load(context, R.raw.g5, 1));
        soundList.put(80, sp.load(context, R.raw.ab5, 1));
        soundList.put(81, sp.load(context, R.raw.a5, 1));
        soundList.put(82, sp.load(context, R.raw.bb5, 1));
        soundList.put(83, sp.load(context, R.raw.b5, 1));

        soundList.put(84, sp.load(context, R.raw.c6, 1));
        soundList.put(85, sp.load(context, R.raw.db6, 1));
        soundList.put(86, sp.load(context, R.raw.d6, 1));
        soundList.put(87, sp.load(context, R.raw.eb6, 1));
        soundList.put(88, sp.load(context, R.raw.e6, 1));
        soundList.put(89, sp.load(context, R.raw.f6, 1));
        soundList.put(90, sp.load(context, R.raw.gb6, 1));
        soundList.put(91, sp.load(context, R.raw.g6, 1));
        soundList.put(92, sp.load(context, R.raw.ab6, 1));
        soundList.put(93, sp.load(context, R.raw.a6, 1));
        soundList.put(94, sp.load(context, R.raw.bb6, 1));
        soundList.put(95, sp.load(context, R.raw.b6, 1));

    }

    public void playSound(int noteNumber){
        Object[] methodArgs = new Object[3];
        methodArgs[0] = noteNumber;
        methodArgs[1] = new Date().toString();
        methodArgs[2] = "connectionId"; // TODO: find where to get the info

        Global.client.call("broadcastNotePlayed", methodArgs);

        if (soundList.containsKey(noteNumber))
            sp.play(soundList.get(noteNumber), 1, 1, 0, 0, 1);
//        if (recording){
//            currentSong.put(System.currentTimeMillis() - startTime, note);
//        }
    }

}
