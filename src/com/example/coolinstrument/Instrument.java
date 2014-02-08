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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

public class Instrument extends Activity {

	TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15,
			b16;
	HashSet<TextView> buttonList = new HashSet<TextView>();
	HashMap<String, Integer> soundList = new HashMap<String, Integer>();
	// ArrayList<MediaPlayer> mediaList = new ArrayList<MediaPlayer>();
	SoundPool sp;
	int n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16;
	static String lock = "";
	MediaPlayer mp1, mp2, mp3;
	private MediaPlayer mp;
	TextView test;
	Boolean noMove = false;
	HashSet<String> mainHash = new HashSet<String>();
	HashSet<String> subHash = new HashSet<String>();
	ArrayList<Integer> playingList = new ArrayList<Integer>();

	TreeMap<Long, Integer> currentSong = new TreeMap<Long, Integer>();
	long startTime = -1;
	boolean recording = false;
	
	Timer playbackTimer = new Timer();
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
		test = (TextView) findViewById(R.id.tvTest);

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
		for (Object thisButton : buttonList) {
			View targetButton = (View) thisButton;
			// thisButton.setOnClickListener(this);
			// thisButton.setOnTouchListener(this);
			targetButton.setSoundEffectsEnabled(false);
		}

		sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

		n1 = sp.load(this, R.raw.note1, 1);

		n2 = sp.load(this, R.raw.note2, 1);

		n3 = sp.load(this, R.raw.note3, 1);
		n4 = sp.load(this, R.raw.note4, 1);
		n5 = sp.load(this, R.raw.note5, 1);
		n6 = sp.load(this, R.raw.note6, 1);

		n7 = sp.load(this, R.raw.note7, 1);
		n8 = sp.load(this, R.raw.note8, 1);
		n9 = sp.load(this, R.raw.note9, 1);

		n10 = sp.load(this, R.raw.note10, 1);
		n11 = sp.load(this, R.raw.note11, 1);

		n12 = sp.load(this, R.raw.note12, 1);
		n13 = sp.load(this, R.raw.note13, 1);
		n14 = sp.load(this, R.raw.note14, 1);
		n15 = sp.load(this, R.raw.note15, 1);
		n16 = sp.load(this, R.raw.note16, 1);
		soundList.put("A", n1);
		soundList.put("Bb", n2);
		soundList.put("B", n3);
		soundList.put("C", n4);
		soundList.put("Db", n5);
		soundList.put("D", n6);
		soundList.put("Eb", n7);
		soundList.put("E", n8);
		soundList.put("F", n9);
		soundList.put("Gb", n10);
		soundList.put("G", n11);
		soundList.put("Ab", n12);
		soundList.put("A2", n13);
		soundList.put("Bb2", n14);
		soundList.put("B2", n15);
		soundList.put("C2", n16);
	}
	
	public void onPlaybackToggled(final View view){
		if (((ToggleButton) view).isChecked()){
			if (currentSong.isEmpty()){
				return;
			}
			//TODO: hacks, should fix
			Handler handler = new Handler();
			for (final Long time : currentSong.keySet()){
				playbackTimer.schedule(new TimerTask(){

					@Override
					public void run() {
						playSound(currentSong.get(time));
						//Toast.makeText(getApplicationContext(), currentSong.get(time), Toast.LENGTH_SHORT).show();
					}
					
				}, time);
			}
			handler.postDelayed(new Runnable(){

				@Override
				public void run() {
					((ToggleButton) view).setChecked(false);
				}
				
			}, currentSong.lastKey()+1000);
		}else{
			playbackTimer.cancel();
		}
	}
	
	public void onRecToggled(View view) {
	    if (((ToggleButton) view).isChecked()) {
	        // start record
	    	Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
	    	startTime = System.currentTimeMillis();
	    	recording = true;
	    	currentSong.clear();
	    } else {
	        // stop record, save
	    	Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
	    	recording = false;
	    }
	}
	
	//Wrapper function so we can capture notes
	private void playSound(int note){
		sp.play(note, 1, 1, 0, 0, 1);
		if (recording){
			currentSong.put(System.currentTimeMillis() - startTime, note);
		}
	}
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
										playSound(soundList.get(buttonText));
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
									playSound(soundList.get(buttonText));
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
						playSound(soundList.get(targetButton.getText()));
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
						playSound(soundList.get(targetButton.getText()));
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
