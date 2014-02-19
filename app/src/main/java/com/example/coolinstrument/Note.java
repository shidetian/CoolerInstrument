package com.example.coolinstrument;

/**
 * Created by lam on 2/19/14.
 */
public class Note {

    private int time; // millisecond
    private int noteNumber;

    public Note(int time, int noteNumber) {
        this.time = time;
        this.noteNumber = noteNumber;
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
