package com.example.coolinstrument;

/**
 * Created by lam on 2/19/14.
 */
public class Note {

    private int time; // millisecond
    private int noteNumber;
    private int velocity;
    private boolean isKeyboardDown;
    private int keyCode;
    private int segmentId;


    public Note() {}

    public Note(int time, int noteNumber) {
        this.time = time;
        this.noteNumber = noteNumber;
    }

    public boolean isKeyboardDown() {
        return isKeyboardDown;
    }

    public void setKeyboardDown(boolean isKeyboardDown) {
        this.isKeyboardDown = isKeyboardDown;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(int noteNumber) {
        this.noteNumber = noteNumber;
    }
}
