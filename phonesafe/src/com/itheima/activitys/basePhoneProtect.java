package com.itheima.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class basePhoneProtect extends Activity {
	private GestureDetector mGestureDetector;
	//初始化SharedPreferences 继承子类会使用到
	protected SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (velocityX < 50) {
							Toast.makeText(basePhoneProtect.this, "滑动过慢", 0)
									.show();
							return true;
						}
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 50) {
							Toast.makeText(basePhoneProtect.this, "在Y轴滑动的", 0)
									.show();
							return true;
						}
						float e1_e2 = e1.getRawX() - e2.getRawX();
						if (Math.abs(e1_e2) > 200) {
							if (e1_e2 < 0) {
								next();
							}
							if (e1_e2 > 0) {
								pre();
							}
							return true;
						} else {
							return super.onFling(e1, e2, velocityX, velocityY);
						}
					}

				});
	}

	/**
	 * GestureDetector(手势监听) 是否匹配 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public abstract void next();

	public abstract void pre();

	public void openNewActivity(Class<?> clazz) {
		// 知道为啥可以用这个this了不?因为getApplicationContext()是全局上下文
		Intent intent = new Intent(basePhoneProtect.this, clazz);
		startActivity(intent);
		finish();
	}

}
