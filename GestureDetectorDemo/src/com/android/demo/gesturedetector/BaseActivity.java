package com.android.demo.gesturedetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	// ����ȫ�ֱ��� mGestureDecetor
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(getApplicationContext(),
				new SimpleOnGestureListener() {
					// ��д���Ƽ�������onFling()��������������������Ļ������
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// e1 :��ָ���µ��ƶ��¼�.
						float e1X = e1.getRawX();
						float e1Y = e1.getRawY();
						// e2 : ��ָ�ƶ��Ķ����¼�.
						float e2X = e2.getRawX();
						float e2Y = e2.getRawY();
						// ָ������X�᷽������С��59����Ч������λ��px
						if (Math.abs(e2X - e1X) < 50) {
							Toast.makeText(getApplicationContext(),
									"���һ���С��50px", 0).show();
							return false;
							// �Ƚ�e2,e1�õ���Yֵ����ȡ��������ֵ�ж��Ƿ����»���
						} else if (Math.abs(e2Y - e1Y) > 200) {
							Toast.makeText(getApplicationContext(), "�������»���", 0)
									.show();
							return false;
							// �Ƚ�e2,e1�õ���Yֵ����ȡ��������ֵ�ж��Ƿ����һ���
						} else if (Math.abs(e2X - e1X) > 50) {
							// �ж��Ƿ���
							if ((e2X - e1X) > 0) {
								Toast.makeText(getApplicationContext(), "�һ�", 0)
										.show();
								tolast();
								// �ж��Ƿ��һ�
							} else {
								Toast.makeText(getApplicationContext(), "��", 0)
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

	// ��дOnTouchEvent()ʶ�����Ƽ�����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * �󻬽����µ�Activity
	 */
	public abstract void tofirst();

	/**
	 * �󻬽����µ�Activity
	 */
	public abstract void tolast();

	/**
	 * openһ���µ�Activity
	 * 
	 * @param clazz
	 *            Ҫ�����Activity.class
	 */
	public void openNewActivity(Class<?> clazz) {
		Intent intent = new Intent(getApplicationContext(), clazz);
		startActivity(intent);
	}
}
