package com.example.coolinstrument;

/**
 * Created by lam on 2/19/14.
 */
public class Replayer {
    private Song song;
    private int currentIndex;

    public Replayer(Song song) {
        this.song = song;
        this.currentIndex = 0;
    }

    public void start() {
        // TODO: refactor the instrument out
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
}
