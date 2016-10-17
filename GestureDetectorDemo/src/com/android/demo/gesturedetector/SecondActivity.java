package com.android.demo.gesturedetector;

import android.os.Bundle;

public class SecondActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
	}

	@Override
	public void tofirst() {
		openNewActivity(MainActivity.class);
		//Ìí¼ÓActivityÌæ»»µÄ¶¯»­
		overridePendingTransition(R.anim.exittranstnim, R.anim.entertranstnim);
	}

	@Override
	public void tolast() {

	}
}
