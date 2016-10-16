package com.itheima.testkaiqiang;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {
	private SoundPool sl;
	private int soundID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sl = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		soundID = sl.load(getResources().openRawResourceFd(R.raw.shoot), 0);
	}

	public void open(View v) {
		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					SystemClock.sleep(1000);
					sl.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
				}
			};
		}.start();
	}
}
