package com.example.coolinstrument;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Stack;

/**
 * Created by lam on 2/19/14.
 */
public class Replayer {
    private Song song;
    private Timer playbackTimer = new Timer();
    private int currentIndex;
    private Piano piano;
    private Stack<Integer> notes;
    private int notePausedAt = -1;
    private HashMap<Integer, TextView> noteToTextview;

    public Replayer(Song song, Context context, HashMap<Integer, TextView> noteToTextview) {
        this(song, new Piano(context), noteToTextview);
    }

    public Replayer(Song song, Piano piano, HashMap<Integer, TextView> noteToTextview) {
        this.song = song;
        this.piano = piano;
        this.currentIndex = 0;
        this.notes = new Stack<Integer>();
        this.noteToTextview = noteToTextview;
    }

    public void reset() {
        currentIndex = 0;
    }

    public void start() {
        if (currentIndex == song.size()) {
            currentIndex = 0;
        }

        if (song.size() == 0) {
            return ;
        }

        _playNextNote();
    }

    public void _playNextNote() {
        if (currentIndex >= song.size())
            return;

        int temp;
        Note note = getCurrentNote();
        incrementCurrentIndex();

        if (noteToTextview.containsKey(note.getNoteNumber()) && note.isKeyboardDown()){
            notePausedAt = note.getNoteNumber();
            noteToTextview.get(note.getNoteNumber()).setBackgroundColor(Color.GREEN);
            while (getWaitTime() == 0){
                if (getCurrentNote().isKeyboardDown()){
                    temp = getCurrentNote().getNoteNumber();
                    notes.push(temp);
                    if (noteToTextview.containsKey(temp)){
                        noteToTextview.get(temp).setBackgroundColor(Color.BLUE);
                    }
                }
                incrementCurrentIndex();
            }
        }
        else {
            playbackTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    _playNextNote();
                }
            }, getWaitTime());
        }
    }

    public void pause() {
        playbackTimer.cancel();
        notePausedAt = -1;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void notePressed(int noteNumber){
        if (notePausedAt == noteNumber){
            if (noteToTextview.containsKey(noteNumber)){
                noteToTextview.get(noteNumber).setBackgroundColor(Color.BLACK);
            }

            int temp;
            while (!notes.empty()){
                temp = notes.pop();
                piano.playSound(temp);
                if (noteToTextview.containsKey(temp)){
                    noteToTextview.get(temp).setBackgroundColor(Color.BLACK);
                }
            }
            playbackTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    _playNextNote();
                }
            }, getWaitTime());
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void incrementCurrentIndex() { currentIndex += 1; }

    public Note getCurrentNote() {
        return song.getNote(currentIndex);
    }

    public int getWaitTime() {
        if (currentIndex == 0 || currentIndex == song.size()) {
            return 0;
        } else {
            return song.getNote(currentIndex).getTime() - song.getNote(currentIndex - 1).getTime();
        }
    }
}
