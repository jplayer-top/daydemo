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
	//��ʼ��SharedPreferences �̳������ʹ�õ�
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
							Toast.makeText(basePhoneProtect.this, "��������", 0)
									.show();
							return true;
						}
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 50) {
							Toast.makeText(basePhoneProtect.this, "��Y�Ử����", 0)
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
	 * GestureDetector(���Ƽ���) �Ƿ�ƥ�� �����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public abstract void next();

	public abstract void pre();

	public void openNewActivity(Class<?> clazz) {
		// ֪��Ϊɶ���������this�˲�?��ΪgetApplicationContext()��ȫ��������
		Intent intent = new Intent(basePhoneProtect.this, clazz);
		startActivity(intent);
		finish();
	}

}
