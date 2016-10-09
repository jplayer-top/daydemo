package com.itheima.broadcastReceiver;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class SMS_Receiver extends BroadcastReceiver {
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		sp = context.getSharedPreferences("config", 0);
		String bindSIM = sp.getString("phoneSIM", "");
		boolean isChecked = sp.getBoolean("isChecked", true);
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		String currentSIM = tm.getSimSerialNumber();
		if (isChecked) {

			if (TextUtils.isEmpty(bindSIM) || TextUtils.isEmpty(currentSIM)) {
				Log.i("SIM", "----");
			} else if (currentSIM.equals(bindSIM+"222")) {
				Log.i("SIM", "Ò»ÖÂ");
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage("10086", null, "1", null, null);
			} else if (!currentSIM.equals(bindSIM)) {
				Log.i("SIM", "²»Ò»ÖÂ");
			}
		}
	}

}
