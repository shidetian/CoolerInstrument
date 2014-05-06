package com.example.coolinstrument;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Collections;
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
    private Activity act = new Activity();
    private Stack<NoteColor> UINotes;
    private int previousIndex = 0;

    public Replayer(Song song, Context context, HashMap<Integer, TextView> noteToTextview) {
        this(song, new Piano(context), noteToTextview);
    }

    public Replayer(Song song, Piano piano, HashMap<Integer, TextView> noteToTextview) {
        this.song = song;
        this.piano = piano;
        this.currentIndex = 0;
        this.notes = new Stack<Integer>();
        this.UINotes = new Stack<NoteColor>();
        this.noteToTextview = noteToTextview;
    }

    public class NoteColor {
        int note;
        int color;

        public NoteColor (int note, int color){
            this.note = note;
            this.color = color;
        }
    }

    public void reset() {
        currentIndex = 0;
        previousIndex = 0;
    }

    public void start() {
        if (currentIndex == song.size()) {
            currentIndex = 0;
            return;
        }

        if (song.size() == 0) {
            return ;
        }

        currentIndex = previousIndex;
        _playNextNote();
    }

    public void _playNextNote() {
        if (currentIndex >= song.size())
            return;

        previousIndex = getCurrentIndex();

        int temp;
        int max = -1;
        Note note = getCurrentNote();
        incrementCurrentIndex();

        if (note.isKeyboardDown()){
            if (noteToTextview.containsKey(note.getNoteNumber())){
                max = note.getNoteNumber();
            }
            else{
                notes.push(note.getNoteNumber());
            }
            while (getWaitTime() == 0){
                temp = getCurrentNote().getNoteNumber();
                if (noteToTextview.containsKey(temp)){
                    if (temp > max){
                        if (noteToTextview.containsKey(max))
                            UINotes.push(new NoteColor(max, Color.BLUE));
                        notes.push(max);
                        max = temp;
                    }
                    else{
                        notes.push(temp);
                        UINotes.push(new NoteColor(temp, Color.BLUE));
                    }
                }
                incrementCurrentIndex();
            }
            if (max > -1){
                notePausedAt = max;
                UINotes.push(new NoteColor(max, Color.GREEN));
                updateUI();
                return;
            }

            dumpNotes(true);
        }
        playbackTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                _playNextNote();
            }
        }, getWaitTime());
    }

    public void pause() {
        playbackTimer.cancel();
        playbackTimer = new Timer();
        if (noteToTextview.containsKey(notePausedAt))
            UINotes.push(new NoteColor(notePausedAt, Color.BLACK));
        dumpNotes(false);
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
            UINotes.push(new NoteColor(noteNumber, Color.BLACK));
            dumpNotes(true);

            playbackTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    _playNextNote();
                }
            }, getWaitTime());
        }
    }

    public void dumpNotes(boolean play){
        int temp;
        while (!notes.empty()){
            temp = notes.pop();
            if (play)
                piano.playSound(temp, true);
            if (noteToTextview.containsKey(temp))
                UINotes.push(new NoteColor(temp, Color.BLACK));
        }
        updateUI();
    }

    public void updateUI(){
        act.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                for (NoteColor n : UINotes){
                    noteToTextview.get(n.note).setBackgroundColor(n.color);
                }
                UINotes.clear();
            }
        });
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
