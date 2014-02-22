package com.example.coolinstrument;

/**
 * Created by lam on 2/19/14.
 */
public class Recorder {
    Song song;
    boolean isRecording;

    public Recorder() {
        song = new Song();
        isRecording = false;
    }

    public void start() {
        isRecording = true;
    }

    public void pause() {
        isRecording = false;
    }

    public void save() {

    }

    public void clear() {
        song = new Song();
    }
}
