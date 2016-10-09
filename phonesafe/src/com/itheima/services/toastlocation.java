package com.itheima.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.db.dao.addressDao;

public class toastlocation extends Service {
	private TelephonyManager tm;
	private myListener listener;
	private BroadcastReceiver receiver;
	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams params;
	private SharedPreferences mSharedPreferences;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String number = getResultData();
			String location = addressDao.whereAddress(context, number);
			// Toast.makeText(context, location, 0).show();
			showToast(context, location);
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new myListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		filter.setPriority(1000);
		registerReceiver(receiver, filter);
		mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
	}

	private class myListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {

					wm.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				String location = addressDao.whereAddress(toastlocation.this,
						incomingNumber);
				Toast.makeText(toastlocation.this, location, 0).show();
				break;
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		unregisterReceiver(receiver);
		receiver = null;
	}

	private void showToast(Context context, String location) {
		view = View.inflate(toastlocation.this, R.layout.items_toast, null);
		view.setOnTouchListener(new myOntouchListener());
		view.setBackgroundResource(mSharedPreferences.getInt("bgwhich", 0));
		TextView tv_toastlocation = (TextView) view
				.findViewById(R.id.tv_toastlocation);
		tv_toastlocation.setText(location);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = 20;
		params.y = 20;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
	}

	private class myOntouchListener implements OnTouchListener {
		private int startX;
		private int startY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_DOWN:
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int newX = (int) event.getRawX();
				int newY = (int) event.getRawY();
				params.x += (newX - startX);
				params.y += (newY - startY);
				Log.i("toastlocation", params.x+"-----"+params.y);
				wm.updateViewLayout(v, params);
				startX = newX;
				startY = newY;
				break;
			}
			return true;
		}

	}
}
