package com.example.coolinstrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

public class Instrument extends Activity {
    public static final int SONG_ID_REQUEST = 1;

    Piano piano;
    Replayer replayer;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.songs:
                Intent songsIntent = new Intent(getApplicationContext(), Songs.class);
                startActivityForResult(songsIntent, SONG_ID_REQUEST);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SONG_ID_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("data", data.getStringExtra("_id"));
            }
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
