package com.example.coolinstrument;

import java.util.Observable;
import java.util.Observer;

public class NoteObserver implements Observer {
    private Piano piano;

    public NoteObserver(Piano piano) {
        this.piano = piano;
    }

    @Override
    public void update(Observable client, Object msg) {
        if (msg instanceof String && ((String) msg).contains("noteNumber")) {
            int begin = ((String) msg).indexOf("noteNumber") + 12;
            String note = ((String) msg).substring(begin);
            int end = note.indexOf(',');
            note = note.substring(0,end);
            piano.playSound(Integer.parseInt(note), false);
        }
    }

}
