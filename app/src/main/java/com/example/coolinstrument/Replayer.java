package com.example.coolinstrument;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.EventListener;
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
    private Stack<Note> notes;
    private Stack<Note> notesPressed;

    public Replayer(Song song, Context context) {
        this(song, new Piano(context));
    }

    public Replayer(Song song, Piano piano) {
        this.song = song;
        this.piano = piano;
        this.currentIndex = 0;
        this.notes = new Stack<Note>();
        this.notesPressed = new Stack<Note>();
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

            Note note = getCurrentNote();
            incrementCurrentIndex();

            if (note.isKeyboardDown()){
                while (getWaitTime() == 0){
                    if (getCurrentNote().isKeyboardDown())
                        notes.push(getCurrentNote());
                    incrementCurrentIndex();
                }

                while (!isNotePressed(note)){
                    try{
                        this.wait();
                    } catch (InterruptedException e){
                    }
                }

                piano.playSound(getCurrentNote().getNoteNumber());
                while (!notes.empty())
                    piano.playSound(notes.pop().getNoteNumber());
            }
    }

    public void pause() {
        notesPressed.removeAllElements();
        playbackTimer.cancel();
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void addNotePressed(Note note){
        if (note != null)
            notesPressed.push(note);
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

    private boolean isNotePressed(Note note){
        while (!notesPressed.empty()){
            if (note.getNoteNumber() == notesPressed.pop().getNoteNumber()){
                notesPressed.removeAllElements();
                return true;
            }
        }
        return false;
    }
}
