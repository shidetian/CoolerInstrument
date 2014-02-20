package com.example.coolinstrument;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lam on 2/19/14.
 */
public class Replayer {
    private Song song;
    private Timer playbackTimer = new Timer();
    private int currentIndex;
    private Piano piano;

    public Replayer(Song song, Context context) {
        this(song, new Piano(context));
    }

    public Replayer(Song song, Piano piano) {
        this.song = song;
        this.piano = piano;
        this.currentIndex = 0;
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
        playbackTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Note note = getCurrentNote();
                
                if (note.isKeyboardDown()) {
                    piano.playSound((getCurrentNote().getNoteNumber() % 12) + 45);
                }

                incrementCurrentIndex();
                _playNextNote();
            }
        }, getWaitTime());
    }

    public void pause() {
        playbackTimer.cancel();
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
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
