package com.example.coolinstrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Instrument extends Activity {
    public static final int SONG_ID_REQUEST = 1;
    private ProgressDialog pDialog;

    Piano piano;
    Replayer replayer;
    HashMap<Integer, Note> notes;

	TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15,
			b16;
	HashSet<TextView> buttonList = new HashSet<TextView>();
	HashMap<String, Integer> soundList = new HashMap<String, Integer>();
	// ArrayList<MediaPlayer> mediaList = new ArrayList<MediaPlayer>();
	SoundPool sp;
	int n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16;
	static String lock = "";
//	MediaPlayer mp1, mp2, mp3;
//	private MediaPlayer mp;
//	TextView test;
	Boolean noMove = false;
	HashSet<String> mainHash = new HashSet<String>();
	HashSet<String> subHash = new HashSet<String>();
	ArrayList<Integer> playingList = new ArrayList<Integer>();

//    Song song = new Song();
    Recorder recorder;

//	TreeMap<Long, Integer> currentSong = new TreeMap<Long, Integer>();
	long startTime = -1;
	boolean recording = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.instrument);
		b1 = (TextView) findViewById(R.id.B1);
		b2 = (TextView) findViewById(R.id.B2);
		b3 = (TextView) findViewById(R.id.B3);
		b4 = (TextView) findViewById(R.id.B4);
		b5 = (TextView) findViewById(R.id.B5);
		b6 = (TextView) findViewById(R.id.B6);
		b7 = (TextView) findViewById(R.id.B7);
		b8 = (TextView) findViewById(R.id.B8);
		b9 = (TextView) findViewById(R.id.B9);
		b10 = (TextView) findViewById(R.id.B10);
		b11 = (TextView) findViewById(R.id.B11);
		b12 = (TextView) findViewById(R.id.B12);
		b13 = (TextView) findViewById(R.id.B13);
		b14 = (TextView) findViewById(R.id.B14);
		b15 = (TextView) findViewById(R.id.B15);
		b16 = (TextView) findViewById(R.id.B16);
//		test = (TextView) findViewById(R.id.tvTest);

		buttonList.add(b1);
		buttonList.add(b2);
		buttonList.add(b3);
		buttonList.add(b4);
		buttonList.add(b5);
		buttonList.add(b6);
		buttonList.add(b7);
		buttonList.add(b8);
		buttonList.add(b9);
		buttonList.add(b10);
		buttonList.add(b11);
		buttonList.add(b12);
		buttonList.add(b13);
		buttonList.add(b14);
		buttonList.add(b15);
		buttonList.add(b16);

		for (View thisButton : buttonList) {
//			View targetButton = thisButton;
			thisButton.setSoundEffectsEnabled(false);
		}

        piano = new Piano(this);
        recorder = new Recorder();
        replayer = new Replayer(new Song(), piano);

	}

    public void onPlaybackToggled(final View view){
        if (((ToggleButton) view).isChecked()){
            replayer.start();
        } else {
            replayer.pause();
        }
    }
	
	public void onRecToggled(View view) {
	    if (((ToggleButton) view).isChecked()) {
	        // start record
	    	Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
	    	startTime = System.currentTimeMillis();
	    	recorder.clear();
            recorder.start();
	    } else {
	        // stop record, save
	    	Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            recorder.pause();
	    }
	}

    public void onSongsClick(View view) {
        Intent songsIntent = new Intent(getApplicationContext(), SongsActivity.class);
        startActivityForResult(songsIntent, SONG_ID_REQUEST);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId())
//        {
//            case R.id.songs:
//                Intent songsIntent = new Intent(getApplicationContext(), SongsActivity.class);
//                startActivityForResult(songsIntent, SONG_ID_REQUEST);
//                break;
//        }
//        return true;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SONG_ID_REQUEST) {
            if (resultCode == RESULT_OK) {
                new GetSong().execute(data.getStringExtra("songUrl"));
            }
        }
    }

    /**
     * Async task class to get notes of a song by making HTTP call
     * */
    private class GetSong extends AsyncTask<String, Void, Void> {
        Song song;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Instrument.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... songUrl) {

            song = new Song();

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(songUrl[0], ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONObject songJson = new JSONObject(jsonStr);

                    JSONArray notesJson = songJson.getJSONArray("notes");
                    notes = new HashMap<Integer, Note>(notesJson.length(), 1);

                    for (int j = 0; j < notesJson.length(); j++) {
                        Note note = new Note();
                        JSONObject noteJson = notesJson.getJSONObject(j);
                        note.setTime(noteJson.getInt("time"));
                        note.setNoteNumber(noteJson.getInt("note"));

                        note.setKeyCode(noteJson.getInt("keyCode"));
                        note.setSegmentId(noteJson.getInt("segmentId"));

                        // this property require special treatment as it can be null
                        boolean isKeyboardDown = false;
                        if (noteJson.has("isKeyboardDown") && noteJson.getBoolean("isKeyboardDown")) {
                            isKeyboardDown = true;
                        }
                        note.setKeyboardDown(isKeyboardDown);
                        notes.put(note.getNoteNumber(), note);

                        song.addNote(note);
                    }

                    replayer.setSong(song);
                    replayer.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }


//    public void registerKeyDownListener()

	/*
	 * // button onclick gesture
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub
	 * 
	 * test.setText("" + event.getActionMasked()); if (event.getActionMasked()
	 * == MotionEvent.ACTION_DOWN) { int i = buttonList.indexOf(v);
	 * sp.play(soundList.get(i), 1, 1, 0, 0, 1); } return true; }
	 */
	// swiping gesture
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() < 3) {
			int action = event.getActionMasked();

			if (action == MotionEvent.ACTION_MOVE) {
				int pressCount = event.getPointerCount();
				if (pressCount > 1) {
					for (int i = 0; i < pressCount; i++) {
						float xVal = event.getX(i);
						float yVal = event.getY(i);

						int[] location = new int[2];
						Boolean breakout = false;
						Boolean hashLock = false;
						for (TextView targetButton : buttonList) {
                            int noteNumber = Integer.parseInt((String) targetButton.getTag());
							String buttonText = targetButton.getText()
									.toString();
							if ((!subHash.contains(buttonText))
									&& (!mainHash.contains(buttonText))) {
								if (lock != buttonText) {
									targetButton.getLocationOnScreen(location);

									float rectX = location[0];
									float rectY = location[1];
									RectF buttonRect = new RectF(rectX, rectY,
											rectX + targetButton.getWidth(),
											rectY + targetButton.getHeight());
									if (buttonRect.contains(xVal, yVal)) {
										/*sp.play(soundList.get(buttonText), 1,
												1, 0, 0, 1);*/
                                        piano.playSound(noteNumber);
                                        Note n = notes.get(noteNumber);
                                       // replayer.addNotePressed(notes[noteNumber]);
//										playSound(soundList.get(buttonText));
										breakout = true;
										subHash.add(buttonText);
									}
									if (breakout) {
										lock = buttonText;
										break;
									}
								}
							} else {
								hashLock = true;
							}
							if (subHash.size() > 2) {
								subHash.clear();
							}
						}

					}
				} else {

					float xVal = event.getX();
					float yVal = event.getY();

					int[] location = new int[2];
					Boolean breakout = false;
					Boolean hashLock = false;
					for (TextView targetButton : buttonList) {
                        int noteNumber = Integer.parseInt((String) targetButton.getTag());
						String buttonText = targetButton.getText().toString();

						if ((!mainHash.contains(buttonText))
								&& (!subHash.contains(buttonText))) {
							if (lock != buttonText) {
								targetButton.getLocationOnScreen(location);

								float rectX = location[0];
								float rectY = location[1];
								RectF buttonRect = new RectF(rectX, rectY,
										rectX + targetButton.getWidth(), rectY
												+ targetButton.getHeight());
								if (buttonRect.contains(xVal, yVal)) {
									/*sp.play(soundList.get(buttonText), 1, 1, 0,
											0, 1);*/
                                    piano.playSound(noteNumber);
                                    Note n = notes.get(noteNumber);
                                    //replayer.addNotePressed(notes[noteNumber]);
//                                    playSound(soundList.get(buttonText));
									breakout = true;
									mainHash.add(buttonText);
								}
								if (breakout) {
									lock = buttonText;
									break;
								}
							}
						} else {
							hashLock = true;
						}
					}
					if (hashLock)
						mainHash.clear();
				}
			}
			if (action == MotionEvent.ACTION_DOWN) {

				float xVal = event.getX();
				float yVal = event.getY();

				int[] location = new int[2];
				Boolean breakout = false;
				for (TextView targetButton : buttonList) {
                    int noteNumber = Integer.parseInt((String) targetButton.getTag());
					String buttonText = targetButton.getText().toString();
					targetButton.getLocationOnScreen(location);

					float rectX = location[0];
					float rectY = location[1];
					RectF buttonRect = new RectF(rectX, rectY, rectX
							+ targetButton.getWidth(), rectY
							+ targetButton.getHeight());
					if (buttonRect.contains(xVal, yVal)) {
						/*sp.play(soundList.get(targetButton.getText()), 1, 1, 0,
								0, 1);*/
                        piano.playSound(noteNumber);
                        Note n = notes.get(noteNumber);
                        //replayer.addNotePressed(notes[noteNumber]);
//                        playSound(soundList.get(targetButton.getText()));
						breakout = true;
						mainHash.add(buttonText);
					}
					if (breakout) {
						lock = buttonText;
						break;
					}

				}
			}
			if (action == MotionEvent.ACTION_POINTER_DOWN) {

				int index = event.getActionIndex();

				float xVal = (int) MotionEventCompat.getX(event, index);
				float yVal = (int) MotionEventCompat.getY(event, index);

				int[] location = new int[2];
				Boolean breakout = false;
				int removeInt = -1;
				for (TextView targetButton : buttonList) {
                    int noteNumber = Integer.parseInt((String) targetButton.getTag());
					String buttonText = targetButton.getText().toString();
					targetButton.getLocationOnScreen(location);
					float rectX = location[0];
					float rectY = location[1];
					RectF buttonRect = new RectF(rectX, rectY, rectX
							+ targetButton.getWidth(), rectY
							+ targetButton.getHeight());

					if (buttonRect.contains(xVal, yVal)) {
						/*sp.play(soundList.get(targetButton.getText()), 1, 1, 0,
								0, 1);*/
                        piano.playSound(noteNumber);
                        Note n = notes.get(noteNumber);
                        //replayer.addNotePressed(notes[noteNumber]);
//                        playSound(soundList.get(targetButton.getText()));
						breakout = true;
						noMove = true;
						subHash.add(buttonText);
					}
					if (breakout) {
						lock = buttonText;
						break;

					}

				}
			}
            //replayer.notify();

			if (action == MotionEvent.ACTION_POINTER_UP) {
				int i = event.getPointerCount();
				if (event.getPointerCount() == 2)
					noMove = false;
				// subHash.clear();
			}

			if (action == MotionEvent.ACTION_UP) {
				noMove = false;
				// mainHash.clear();
			}

		}
		return true;
	}
}
