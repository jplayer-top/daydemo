package com.android.demo.gesturedetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	// 定义全局变量 mGestureDecetor
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(getApplicationContext(),
				new SimpleOnGestureListener() {
					// 重写手势监听器的onFling()方法，用来监听滑动屏幕的手势
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// e1 :手指按下的移动事件.
						float e1X = e1.getRawX();
						float e1Y = e1.getRawY();
						// e2 : 手指移动的动作事件.
						float e2X = e2.getRawX();
						float e2Y = e2.getRawY();
						// 指滑动的X轴方向的如果小于59，无效果，单位是px
						if (Math.abs(e2X - e1X) < 50) {
							Toast.makeText(getApplicationContext(),
									"左右滑动小于50px", 0).show();
							return false;
							// 比较e2,e1得到的Y值，获取两数绝对值判断是否上下滑动
						} else if (Math.abs(e2Y - e1Y) > 200) {
							Toast.makeText(getApplicationContext(), "手势上下滑动", 0)
									.show();
							return false;
							// 比较e2,e1得到的Y值，获取两数绝对值判断是否左右滑动
						} else if (Math.abs(e2X - e1X) > 50) {
							// 判断是否左滑
							if ((e2X - e1X) > 0) {
								Toast.makeText(getApplicationContext(), "右滑", 0)
										.show();
								tolast();
								// 判断是否右滑
							} else {
								Toast.makeText(getApplicationContext(), "左滑", 0)
										.show();
								tofirst();
							}
							return true;
						} else {
							return super.onFling(e1, e2, velocityX, velocityY);
						}
					}

				});
	}

	// 重写OnTouchEvent()识别手势监听器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 左滑进入新的Activity
	 */
	public abstract void tofirst();

	/**
	 * 左滑进入新的Activity
	 */
	public abstract void tolast();

	/**
	 * open一个新的Activity
	 * 
	 * @param clazz
	 *            要进入得Activity.class
	 */
	public void openNewActivity(Class<?> clazz) {
		Intent intent = new Intent(getApplicationContext(), clazz);
		startActivity(intent);
	}
}
