package com.example.coolinstrument;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by lam on 2/19/14.
 */
public class Piano {
    
    private SoundPool sp;
    private HashMap<Integer, Integer> soundList;
    
    public Piano(Context context) {

        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        int n1 = sp.load(context, R.raw.note1, 1);
        int n2 = sp.load(context, R.raw.note2, 1);
        int n3 = sp.load(context, R.raw.note3, 1);
        int n4 = sp.load(context, R.raw.note4, 1);
        int n5 = sp.load(context, R.raw.note5, 1);
        int n6 = sp.load(context, R.raw.note6, 1);
        int n7 = sp.load(context, R.raw.note7, 1);
        int n8 = sp.load(context, R.raw.note8, 1);
        int n9 = sp.load(context, R.raw.note9, 1);
        int n10 = sp.load(context, R.raw.note10, 1);
        int n11 = sp.load(context, R.raw.note11, 1);
        int n12 = sp.load(context, R.raw.note12, 1);
        int n13 = sp.load(context, R.raw.note13, 1);
        int n14 = sp.load(context, R.raw.note14, 1);
        int n15 = sp.load(context, R.raw.note15, 1);
        int n16 = sp.load(context, R.raw.note16, 1);

        soundList = new HashMap<Integer, Integer>();
        soundList.put(45, n1);
        soundList.put(46, n2);
        soundList.put(47, n3);
        soundList.put(48, n4);
        soundList.put(49, n5);
        soundList.put(50, n6);
        soundList.put(51, n7);
        soundList.put(52, n8);
        soundList.put(53, n9);
        soundList.put(54, n10);
        soundList.put(55, n11);
        soundList.put(56, n12);
        soundList.put(57, n13);
        soundList.put(58, n14);
        soundList.put(59, n15);
        soundList.put(60, n16); // 60 is the noteNumber for middle C
    }

    public void playSound(int noteNumber){
        sp.play(soundList.get(noteNumber), 1, 1, 0, 0, 1);
//        if (recording){
//            currentSong.put(System.currentTimeMillis() - startTime, note);
//        }
    }
}
