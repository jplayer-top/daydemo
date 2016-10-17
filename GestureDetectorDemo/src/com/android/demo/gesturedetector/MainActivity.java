package com.android.demo.gesturedetector;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// 重写父类方法进入右滑界面
	@Override
	public void tolast() {
		openNewActivity(SecondActivity.class);
		// 添加Activity替换的动画
		overridePendingTransition(R.anim.entertranstnim, R.anim.exittranstnim);
	}

	@Override
	public void tofirst() {

	}

}
