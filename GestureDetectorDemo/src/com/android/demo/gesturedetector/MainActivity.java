package com.android.demo.gesturedetector;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// ��д���෽�������һ�����
	@Override
	public void tolast() {
		openNewActivity(SecondActivity.class);
		// ���Activity�滻�Ķ���
		overridePendingTransition(R.anim.entertranstnim, R.anim.exittranstnim);
	}

	@Override
	public void tofirst() {

	}

}
