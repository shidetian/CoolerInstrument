package com.example.coolinstrument;

import java.util.ArrayList;
import java.util.TreeMap;

public class Song {

    private ArrayList<Note> notes;
    private String title;
    private String desc;

	public Song(){
        notes = new ArrayList<Note>();
	}

    public boolean isEmpty() {
        return notes.size() == 0;
    }

    public Note getNote(int i) {
        return notes.get(i);
    }

    public int size() {
        return notes.size();
    }

    public void clear() {
        this.notes = new ArrayList<Note>();
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }
}
