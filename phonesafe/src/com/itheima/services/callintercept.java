package com.itheima.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.itheima.db.dao.mySQL;

public class callintercept extends Service {
	private TelephonyManager tm;
	private myPhoneStateListener listener;
	private mySQL ms;
	private ContentObserver observer;
	private BroadcastReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class smsintercept extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
				String address = sms.getOriginatingAddress();
				if(address==null){
					return;
				}
				String mode = ms.select(address);
				if (mode != null) {
					if (mode.equals("全选") || mode.equals("短信")) {
						Log.i("callintercept", "短信被沙特当");
						abortBroadcast();
					}
					
				}
			}
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("flag", "ok");
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new myPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new smsintercept();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);
		registerReceiver(receiver, intentFilter);
	}

	private class myPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				ms = new mySQL(callintercept.this);
				String mode = ms.select(incomingNumber);
				if (mode != null)
					if (mode.equals("全选") || mode.equals("电话")) {
						Log.i("call", mode);
						try {
							// ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
							Class<?> clazz = getClassLoader().loadClass(
									"android.os.ServiceManager");
							Method method = clazz.getDeclaredMethod(
									"getService", String.class);

							IBinder iBinder = (IBinder) method.invoke(null,
									Context.TELEPHONY_SERVICE);
							ITelephony itelephony = ITelephony.Stub
									.asInterface(iBinder);
							boolean stateCall = itelephony.endCall();
							Log.i("callintercept", stateCall + "");
							final Uri uri = Uri
									.parse("content://call_log/calls");
							final ContentResolver resolver = getContentResolver();
							observer = new ContentObserver(new Handler()) {
								@Override
								public void onChange(boolean selfChange) {
									// TODO Auto-generated method stub
									super.onChange(selfChange);
									resolver.delete(uri, "number=?",
											new String[] { incomingNumber });
									resolver.unregisterContentObserver(observer);
									Log.i("callintercept", "已经拦截黑名单电话");
								}
							};
							resolver.registerContentObserver(uri, true,
									observer);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				break;
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		listener = null;
		unregisterReceiver(receiver);
		receiver = null;
	}

}
