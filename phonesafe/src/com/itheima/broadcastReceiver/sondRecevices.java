package com.itheima.broadcastReceiver;

import com.example.phonesafe.R;
import com.itheima.services.getRelLocation;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;

public class sondRecevices extends BroadcastReceiver {
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		sp = context.getSharedPreferences("config", 0);
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(context, myAdministration.class);

		if (sp.getBoolean("isChecked", true)) {
			Log.i("sss", "短信来了");
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object object : pdus) {

				SmsMessage smsmessage = SmsMessage
						.createFromPdu((byte[]) object);
				String body = smsmessage.getMessageBody();
				if (body.equals("#*location*#")) {
					Intent serviceIntent = new Intent(context,
							getRelLocation.class);
					context.startService(serviceIntent);
					abortBroadcast();
				} else if (body.equals("#*alarm*#")) {
					MediaPlayer mediaplayer = MediaPlayer.create(context,
							R.raw.ylzs);
					AudioManager audiomanager = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);
					int maxStram = audiomanager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,
							maxStram, 0);
					mediaplayer.start();
					abortBroadcast();

				} else if (body.equals("#*wipedate*#")) {
					dpm = (DevicePolicyManager) context
							.getSystemService(Context.DEVICE_POLICY_SERVICE);
					dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					abortBroadcast();

				} else if (body.equals("#*lockscreen*#")) {
					if (dpm.isAdminActive(who)) {
						dpm.resetPassword(
								"1234567",
								DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
						dpm.lockNow();

					} else {
						Intent devicesintent = new Intent(
								DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
						devicesintent.putExtra(
								DevicePolicyManager.EXTRA_DEVICE_ADMIN,
								who);
						devicesintent.putExtra(
								DevicePolicyManager.EXTRA_ADD_EXPLANATION,
								"请开启管理员权限,开启后可以锁屏,不装扣500块钱");
						context.startActivity(devicesintent);

					}
					abortBroadcast();
				}
			}
		}
	}
}
