package com.itheima.broadcastReceiver;

import com.itheima.services.watchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class opensevice extends BroadcastReceiver {
private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", 0);
		Editor editor = sp.edit();
		editor.putBoolean("isBoot", true);
		editor.commit();
		System.out.println("¿ª»ú");
		Intent service = new Intent(context, watchdog.class);
		context.startService(service);
	}

}
